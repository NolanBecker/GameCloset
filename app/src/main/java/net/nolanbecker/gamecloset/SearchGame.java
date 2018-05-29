package net.nolanbecker.gamecloset;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchGame {

    Activity activity;
    List<Game> list;
    ProgressBar refresh;
    ListView listView;

    public SearchGame(Activity activity, List<Game> list, ListView listView, ProgressBar refresh) {
        this.activity = activity;
        this.list = list;
        this.listView = listView;
        this.refresh = refresh;
    }

    public void execute(String query) {
        refresh.setVisibility(View.VISIBLE);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://boardgamegeek.com/xmlapi2/search?type=boardgame&query="+query).build();
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
            JSONObject json = XML.toJSONObject(response).getJSONObject("items");
            JSONArray items = json.getJSONArray("item");
            for (int i=0; i<items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                int id = item.getInt("id");
                String name = item.getJSONObject("name").getString("value");
                list.add(new Game(
                        id,
                        name,
                        null,
                        null,
                        0,
                        null,
                        0,
                        null,
                        null,
                        null,
                        0,
                        null
                ));
//                Toast.makeText(activity, name, Toast.LENGTH_SHORT).show();
                if (i == items.length()-1)
                    getInfo(i, true);
                else
                    getInfo(i, false);
            }
            refreshList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshList() {
        GameAdapter adapter = new GameAdapter(list, activity, refresh);
        listView.setAdapter(adapter);
    }

    private void getInfo(final int position, final Boolean last) {
        refresh.setVisibility(View.VISIBLE);
        try {
            int id = list.get(position).getId();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://boardgamegeek.com/xmlapi2/thing?stats=1&id="+id).build();
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
                            parseInfo(myResponse, position, last);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseInfo(String response, int position, final Boolean last) {
        try {
            JSONObject json = XML.toJSONObject(response).getJSONObject("items");
            JSONObject item = json.getJSONObject("item");
            list.get(position).setThumb(item.getString("thumbnail"));
            list.get(position).setDesc(item.getString("description"));
            list.get(position).setYear(item.getJSONObject("yearpublished").getInt("value"));
            list.get(position).setImage(item.getString("image"));
            int minplayers = item.getJSONObject("minplayers").getInt("value");
            int maxplayers = item.getJSONObject("maxplayers").getInt("value");
            list.get(position).setPlayers(minplayers + "-" + maxplayers);
            list.get(position).setPlaytime(String.valueOf(item.getJSONObject("playingtime").getInt("value")));
            int minage = item.getJSONObject("minage").getInt("value");
            list.get(position).setAge(minage);
            JSONObject stats = item.getJSONObject("statistics").getJSONObject("ratings");
            try {
                JSONArray ranks = stats.getJSONObject("ranks").getJSONArray("rank");
                int rank = 0;
                for (int i=0; i<ranks.length(); i++) {
                    JSONObject rankObj = ranks.getJSONObject(i);
                    if (rankObj.getString("friendlyname").equals("Board Game Rank")) {
                        if (!rankObj.getString("value").equals("Not Ranked"))
                            rank = rankObj.getInt("value");
                    }
                }
                list.get(position).setRank(rank);
            } catch (Exception e) {
                JSONObject ranks = stats.getJSONObject("ranks").getJSONObject("rank");
                int rank = 0;
                if (ranks.getString("friendlyname").equals("Board Game Rank")) {
                    if (!ranks.getString("value").equals("Not Ranked"))
                        rank = ranks.getInt("value");
                }
                list.get(position).setRank(rank);
            }
            double difficulty = stats.getJSONObject("averageweight").getDouble("value");
            list.get(position).setDifficulty(String.valueOf(difficulty));
            if (!last) {
                GameAdapter adapter = new GameAdapter(list, activity);
                listView.setAdapter(adapter);
            } else {
                refreshList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
