package net.nolanbecker.gamecloset;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RemoveGame {

    Activity activity;
    List<Game> list;
    ProgressBar refresh;
    WaveSwipeRefreshLayout swipeRefresh;
    ListView listView;
    int position;
    View view;

    public RemoveGame(Activity activity, View view, List<Game> list, ListView listView, int position) {
        this.activity = activity;
        this.list = list;
        this.listView = listView;
        this.position = position;
        this.view = view;
    }

    public RemoveGame(Activity activity, View view, List<Game> list, ListView listView, int position, ProgressBar refresh) {
        this.activity = activity;
        this.list = list;
        this.listView = listView;
        this.position = position;
        this.refresh = refresh;
        this.view = view;
    }

    public RemoveGame(Activity activity, View view, List<Game> list, ListView listView, int position, WaveSwipeRefreshLayout swipeRefresh) {
        this.activity = activity;
        this.list = list;
        this.listView = listView;
        this.position = position;
        this.swipeRefresh = swipeRefresh;
        this.view = view;
    }

    public void execute(int id) {
        try {
            OkHttpClient client = new OkHttpClient();

            String params = "&id=" + id;

            Request request = new Request.Builder().url(Api.URL_DELETE_GAME + params).build();
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
                                parseResponse(myResponse);
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

    private void parseResponse(String response) {
        try {
            String game = list.get(position).getName();
            if (response.isEmpty()) {
                Toast.makeText(activity, "Some sort of error occurred.", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject json = new JSONObject(response);
                if (json.getString("error").equals("true")) {
                    Toast.makeText(activity, json.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    list.clear();
                    if (refresh != null)
                        new GetGames(activity, view, list, listView, refresh).execute();
                    else if (swipeRefresh != null)
                        new GetGames(activity, view, list, listView, swipeRefresh).execute();
                    else
                        new GetGames(activity, view, list, listView).execute();
                    Toast.makeText(activity, game + " was deleted successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
