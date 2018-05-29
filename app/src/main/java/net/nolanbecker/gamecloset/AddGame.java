package net.nolanbecker.gamecloset;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddGame {

    Activity activity;
    List<Game> searchList;
    ProgressBar searchRefresh;
    ListView listViewSearch;
    int position;

    public AddGame(Activity activity, List<Game> searchList, ListView listViewSearch, int position, ProgressBar searchRefresh) {
        this.activity = activity;
        this.searchList = searchList;
        this.listViewSearch = listViewSearch;
        this.position = position;
        this.searchRefresh = searchRefresh;
    }

    public void execute(HashMap params) {
        String id = null;
        String name = null;
        String thumb = "null";
        String year = "null";
        String desc = "null";
        String image = "null";
        String players = "null";
        String playtime = "null";
        String age = "null";
        String rank = "null";
        String difficulty = "null";
        if (params.containsKey("id"))
            id = params.get("id").toString();
        if (params.containsKey("name"))
            name = params.get("name").toString();
        if (params.containsKey("thumb"))
            thumb = params.get("thumb").toString();
        if (params.containsKey("year"))
            year = params.get("year").toString();
        if (params.containsKey("desc"))
            desc = params.get("desc").toString();
        if (params.containsKey("image"))
            image = params.get("image").toString();
        if (params.containsKey("players"))
            players = params.get("players").toString();
        if (params.containsKey("playtime"))
            playtime = params.get("playtime").toString();
        if (params.containsKey("age"))
            age = params.get("age").toString();
        if (params.containsKey("rank"))
            rank = params.get("rank").toString();
        if (params.containsKey("difficulty"))
            difficulty = params.get("difficulty").toString();

        if (searchRefresh != null)
            searchRefresh.setVisibility(View.VISIBLE);

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id", id)
                    .addFormDataPart("name", name)
                    .addFormDataPart("thumb", thumb)
                    .addFormDataPart("year", year)
                    .addFormDataPart("desc", desc)
                    .addFormDataPart("image", image)
                    .addFormDataPart("players", players)
                    .addFormDataPart("playtime", playtime)
                    .addFormDataPart("age", age)
                    .addFormDataPart("rank", rank)
                    .addFormDataPart("difficulty", difficulty)
                    .build();

            Request request = new Request.Builder().url(Api.URL_ADD_GAME).post(body).build();
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
            String game = searchList.get(position).getName();
            if (searchRefresh != null)
                searchRefresh.setVisibility(View.GONE);
            if (response.isEmpty()) {
                Toast.makeText(activity, "You already have that game.", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject json = new JSONObject(response);
                if (json.getString("error").equals("true")) {
                    Toast.makeText(activity, json.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, game + " was added successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshList() {
        GameAdapter adapter = new GameAdapter(searchList, activity, searchRefresh);
        listViewSearch.setAdapter(adapter);
    }

}
