package com.lifeplaytrip.luckycoin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lifeplaytrip.luckycoin.R;
import com.lifeplaytrip.luckycoin.model.Game;

import java.util.List;

/**
 * Created by LifePlayTrip on 2/26/2018.
 */

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {
    private Context context;
    private List<Game> gameList;

    //declare interface
    private GameAdapter.News_OnItemClicked onClick;

    //make interface like this
    public interface News_OnItemClicked {
        void news_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView win_status, win_points, win_date, win_time;
        public ImageView win_status_image;

        public MyViewHolder(View view) {
            super(view);
            win_status = (TextView) view.findViewById(R.id.win_status);
            win_points = (TextView) view.findViewById(R.id.win_points);
            win_date = (TextView) view.findViewById(R.id.win_date);
            win_time = (TextView) view.findViewById(R.id.win_time);
            win_status_image = (ImageView) view.findViewById(R.id.win_status_image);
        }
    }

    public GameAdapter(Context context, List<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
    }

    @Override
    public GameAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_history, parent, false);

        return new GameAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GameAdapter.MyViewHolder holder, final int position) {
        Game game = gameList.get(position);
        holder.win_status.setText(game.getWin_status());
        holder.win_points.setText(game.getWin_points());
        holder.win_date.setText(game.getWin_date());
        holder.win_time.setText(game.getWin_time());
        Log.d("this", game.getWin_status_image() + "");
        Glide.with(context).load(game.getWin_status_image()).into(holder.win_status_image);
    }

    public void setOnClick(GameAdapter.News_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }
}
