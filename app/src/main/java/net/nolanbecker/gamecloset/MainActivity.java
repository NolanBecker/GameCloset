package net.nolanbecker.gamecloset;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

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

    public static Fragment currentFragment = null;

    ListView listView;
    WaveSwipeRefreshLayout swipeRefresh;
    public static FlowingDrawer drawerLayout;

    List<Game> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        listView = findViewById(R.id.listViewGames);
//        swipeRefresh = findViewById(R.id.swipeRefresh);
        drawerLayout = findViewById(R.id.drawerLayout);

        gameList = new ArrayList<>();

        setupMenu();

//        GetGames();
//
//        swipeRefresh.setWaveARGBColor(255, 63, 81, 181);
//        swipeRefresh.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                gameList.clear();
//                GetGames();
//            }
//        });
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                gameList.remove(position);
//                GameAdapter adapter = new GameAdapter(gameList, getApplicationContext(), swipeRefresh);
//                listView.setAdapter(adapter);
//            }
//        });

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

    private void loadFragment() {
        // create FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with the new Fragment
        fragmentTransaction.replace(R.id.mainFrameLayout, currentFragment);
        fragmentTransaction.commit();
    }

    private void setupMenu() {
        FragmentManager fm = getSupportFragmentManager();
        MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            mMenuFragment = new MenuListFragment();
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (drawerLayout.isMenuVisible()) {
            drawerLayout.closeMenu();
        } else if (fm.getBackStackEntryCount() > 0) {
            if (!fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().equals("games")) {
                fm.beginTransaction().remove(currentFragment).commit();
                fm.popBackStack();
            } else {
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }
}
