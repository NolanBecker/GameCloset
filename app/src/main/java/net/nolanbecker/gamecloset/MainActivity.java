package net.nolanbecker.gamecloset;

import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.getbase.floatingactionbutton.AddFloatingActionButton;

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

public class MainActivity extends AppCompatActivity {

    ListView listView;
    WaveSwipeRefreshLayout swipeRefresh;
    AddFloatingActionButton fab;
    EditText editSearch;
    View dummyView;

    List<Game> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewGames);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        fab = findViewById(R.id.fab);
        editSearch = findViewById(R.id.editSearch);

        gameList = new ArrayList<>();

        GetGames();

        swipeRefresh.setWaveARGBColor(255, 63, 81, 181);
        swipeRefresh.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (editSearch.getVisibility() == View.VISIBLE) {
                    editSearch.setVisibility(View.INVISIBLE);
                    editSearch.clearFocus();
                }
                gameList.clear();
                GetGames();
            }
        });

        editSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (!hasFocus) {
                    imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
                } else {
                    imm.showSoftInput(editSearch, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editSearch.getVisibility() == View.INVISIBLE) {
                    editSearch.setVisibility(View.VISIBLE);
                    editSearch.setClickable(true);
                    editSearch.requestFocus();
                } else {
                    editSearch.setVisibility(View.INVISIBLE);
                    editSearch.setClickable(false);
                    editSearch.clearFocus();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gameList.remove(position);
                GameAdapter adapter = new GameAdapter(gameList, getApplicationContext(), swipeRefresh);
                listView.setAdapter(adapter);
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
