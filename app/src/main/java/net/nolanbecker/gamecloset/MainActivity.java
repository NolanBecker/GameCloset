package net.nolanbecker.gamecloset;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    SwipeRefreshLayout swipeRefresh;

    List<Game> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewGames);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        gameList = new ArrayList<>();

        GetGames();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gameList.clear();
                GetGames();
            }
        });

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

                    MainActivity.this.runOnUiThread(new Runnable() {
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
                                GameAdapter adapter = new GameAdapter(gameList, getApplicationContext(), swipeRefresh);
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
