package net.nolanbecker.gamecloset;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class GetGames {

    Activity activity;
    List<Game> list;
    ProgressBar refresh;
    WaveSwipeRefreshLayout swipeRefresh;
    ListView listView;
    View view;

    public GetGames(Activity activity, View view, List<Game> list, ListView listView) {
        this.activity = activity;
        this.list = list;
        this.listView = listView;
        this.view = view;
    }

    public GetGames(Activity activity, View view, List<Game> list, ListView listView, ProgressBar refresh) {
        this.activity = activity;
        this.list = list;
        this.listView = listView;
        this.refresh = refresh;
        this.view = view;
    }

    public GetGames(Activity activity, View view, List<Game> list, ListView listView, WaveSwipeRefreshLayout swipeRefresh) {
        this.activity = activity;
        this.list = list;
        this.listView = listView;
        this.swipeRefresh = swipeRefresh;
        this.view = view;
    }

    public void execute() {
        if (refresh != null)
            refresh.setVisibility(View.VISIBLE);
        else if (swipeRefresh != null)
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
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(myResponse);
                                JSONArray games = json.getJSONArray("games");
                                if (games.length() > 0) {
                                    for (int i = 0; i < games.length(); i++) {
                                        JSONObject obj = games.getJSONObject(i);
                                        list.add(new Game(
                                                obj.getInt("id"),
                                                obj.getString("name"),
                                                obj.getString("thumb"),
                                                obj.getString("desc"),
                                                obj.getInt("year"),
                                                obj.getString("image"),
                                                obj.getInt("rank"),
                                                obj.getString("players"),
                                                obj.getString("playtime"),
                                                obj.getInt("age"),
                                                obj.getString("difficulty")
                                        ));
                                    }
                                    GameAdapter adapter;
                                    if (refresh != null)
                                        adapter = new GameAdapter(list, activity, refresh);
                                    else if (swipeRefresh != null)
                                        adapter = new GameAdapter(list, activity, swipeRefresh);
                                    else
                                        adapter = new GameAdapter(list, activity);
                                    listView.setAdapter(adapter);
                                } else {
                                    final Snackbar snackbar = Snackbar.make(view, "No Games Found", Snackbar.LENGTH_INDEFINITE);
                                    snackbar.setAction("DISMISS", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            snackbar.dismiss();
                                        }
                                    });
                                    snackbar.show();
                                    if (refresh != null)
                                        refresh.setVisibility(View.GONE);
                                    if (swipeRefresh != null)
                                        swipeRefresh.setRefreshing(false);
                                }
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
