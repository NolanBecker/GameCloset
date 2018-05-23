package net.nolanbecker.gamecloset;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GamesFragment extends Fragment {

    ListView listView;
    WaveSwipeRefreshLayout swipeRefresh;

    List<Game> gameList;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_games, container, false);

        listView = view.findViewById(R.id.listViewGames);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        gameList = new ArrayList<>();

        GetGames();

        swipeRefresh.setWaveARGBColor(255, 63, 81, 181);
        swipeRefresh.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gameList.clear();
                GetGames();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gameList.remove(position);
                GameAdapter adapter = new GameAdapter(gameList, view.getContext(), swipeRefresh);
                listView.setAdapter(adapter);
            }
        });

        return view;
    }

    private void GetGames() {
        swipeRefresh.setRefreshing(true);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(Api.URL_READ_GAMES).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String myResponse = response.body().string();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(myResponse);
                                JSONArray games = json.getJSONArray("games");
                                for (int i=0; i<games.length(); i++) {
                                    JSONObject obj = games.getJSONObject(i);
                                    gameList.add(new Game(
                                            obj.getInt("id"),
                                            obj.getString("name"),
                                            obj.getString("thumb")
                                    ));
                                }
                                GameAdapter adapter = new GameAdapter(gameList, view.getContext(), swipeRefresh);
                                listView.setAdapter(adapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
