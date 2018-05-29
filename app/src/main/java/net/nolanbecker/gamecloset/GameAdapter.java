package net.nolanbecker.gamecloset;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class GameAdapter extends BaseAdapter {

    List<Game> gameList;
    Context context;
    String imgUrl;
    WaveSwipeRefreshLayout swipeRefresh;
    ProgressBar progressBar;
    private LayoutInflater layoutInflater;

    public GameAdapter(List<Game> gameList, Context context, WaveSwipeRefreshLayout swipeRefresh) {
        layoutInflater = LayoutInflater.from(context);
        this.gameList = gameList;
        this.context = context;
        this.swipeRefresh = swipeRefresh;
    }

    public GameAdapter(List<Game> gameList, Context context, ProgressBar progressBar) {
        layoutInflater = LayoutInflater.from(context);
        this.gameList = gameList;
        this.context = context;
        this.progressBar = progressBar;
    }

    public GameAdapter(List<Game> gameList, Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.gameList = gameList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public Object getItem(int position) {
        return gameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView imgThumb;
        TextView textName, textYear, textUpdate, textDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.game_list, null);
            holder = new ViewHolder();
            holder.imgThumb = convertView.findViewById(R.id.imgThumb);
            holder.imgThumb.setVisibility(View.VISIBLE);
            holder.textName = convertView.findViewById(R.id.textName);
            holder.textYear = convertView.findViewById(R.id.textYear);
            holder.textUpdate = convertView.findViewById(R.id.textUpdate);
            holder.textDelete = convertView.findViewById(R.id.textDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Game game = gameList.get(position);

        holder.textName.setText(game.getName());
        if (game.getYear() == 0)
            holder.textYear.setText("");
        else
            holder.textYear.setText(String.valueOf(game.getYear()));
        imgUrl = game.getThumb();
        if (!imgUrl.equals("null") && !imgUrl.isEmpty()) {
            if (holder.imgThumb != null) {
                Picasso.get().load(imgUrl).into(holder.imgThumb);
                holder.imgThumb.setVisibility(View.VISIBLE);
            }
        } else {
            holder.imgThumb.setVisibility(View.GONE);
        }

        if (swipeRefresh != null)
            swipeRefresh.setRefreshing(false);
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);

        return convertView;
    }
}
