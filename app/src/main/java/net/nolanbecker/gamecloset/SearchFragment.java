package net.nolanbecker.gamecloset;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {

    View view;
    List<Game> searchList;

    EditText editSearch;
    Button btnSearch;
    ListView listViewSearch;
    TextView txtError;
    ProgressBar searchRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        editSearch = view.findViewById(R.id.editSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        listViewSearch = view.findViewById(R.id.listView);
        txtError = view.findViewById(R.id.txtError);
        searchRefresh = view.findViewById(R.id.refresh);

        searchList = new ArrayList<>();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchList.clear();
                String search = editSearch.getText().toString();
                if (!search.isEmpty()) {
                    txtError.setText("");
                    new SearchGame(getActivity(), searchList, listViewSearch, searchRefresh).execute(search);
                    try {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    txtError.setText("Please enter the name of a game");
                }
            }
        });

        listViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), searchList.get(position).getDesc(), Toast.LENGTH_SHORT).show();
            }
        });

        listViewSearch.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int paramId = searchList.get(position).getId();
                String name = searchList.get(position).getName();
                String thumb = searchList.get(position).getThumb();
                int year = searchList.get(position).getYear();
                String desc = searchList.get(position).getDesc();
                String image = searchList.get(position).getImage();
                String players = searchList.get(position).getPlayers();
                String playtime = searchList.get(position).getPlaytime();
                int age = searchList.get(position).getAge();
                int rank = searchList.get(position).getRank();
                String difficulty = searchList.get(position).getDifficulty();

                HashMap hashMap = new HashMap();
                hashMap.put("id", String.valueOf(paramId));
                hashMap.put("name", name);
                hashMap.put("thumb", thumb);
                hashMap.put("year", String.valueOf(year));
                hashMap.put("desc", desc);
                hashMap.put("image", image);
                hashMap.put("players", players);
                hashMap.put("playtime", playtime);
                hashMap.put("age", String.valueOf(age));
                hashMap.put("rank", String.valueOf(rank));
                hashMap.put("difficulty", difficulty);
                new AddGame(getActivity(), searchList, listViewSearch, position, searchRefresh).execute(hashMap);
                return true;
            }
        });

        return view;
    }

}