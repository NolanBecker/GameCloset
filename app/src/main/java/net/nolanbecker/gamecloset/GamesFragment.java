package net.nolanbecker.gamecloset;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        new GetGames(getActivity(), view, gameList, listView, swipeRefresh).execute();

        swipeRefresh.setWaveARGBColor(255, 63, 81, 181);
        swipeRefresh.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gameList.clear();
                new GetGames(getActivity(), view, gameList, listView, swipeRefresh).execute();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), GamePageActivity.class).putExtra("Game", gameList.get(position)));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int paramId = gameList.get(position).getId();
                new RemoveGame(getActivity(), view, gameList, listView, position, swipeRefresh).execute(paramId);
                return true;
            }
        });

        return view;
    }

}
