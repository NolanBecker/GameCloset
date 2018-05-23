package net.nolanbecker.gamecloset;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GameAdapter extends BaseAdapter {

    List<Game> gameList;
    Context context;
    String imgUrl;
    SwipeRefreshLayout swipeRefresh;
    private LayoutInflater layoutInflater;

    public GameAdapter(List<Game> gameList, Context context, SwipeRefreshLayout swipeRefresh) {
        layoutInflater = LayoutInflater.from(context);
        this.gameList = gameList;
        this.context = context;
        this.swipeRefresh = swipeRefresh;
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
        TextView textName, textId, textUpdate, textDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.game_list, null);
            holder = new ViewHolder();
            holder.imgThumb = convertView.findViewById(R.id.imgThumb);
            holder.textName = convertView.findViewById(R.id.textName);
            holder.textId = convertView.findViewById(R.id.textId);
            holder.textUpdate = convertView.findViewById(R.id.textUpdate);
            holder.textDelete = convertView.findViewById(R.id.textDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Game game = gameList.get(position);

        holder.textName.setText(game.getName());
        holder.textId.setText(String.valueOf(game.getId()));
        imgUrl = game.getThumb();
        if (!imgUrl.isEmpty()) {
            if (holder.imgThumb != null) {
//                Toast.makeText(context, imgUrl, Toast.LENGTH_SHORT).show();
                Picasso.get().load(imgUrl).into(holder.imgThumb);
            }
        } else {
            holder.imgThumb.setVisibility(View.GONE);
        }

        swipeRefresh.setRefreshing(false);

        return convertView;
    }
}
