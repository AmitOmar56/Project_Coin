package com.lifeplaytrip.luckycoin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifeplaytrip.luckycoin.R;
import com.lifeplaytrip.luckycoin.model.LeatherBoard;

import java.util.List;

/**
 * Created by LifePlayTrip on 2/9/2018.
 */

public class LeatherBoardAdapter extends RecyclerView.Adapter<LeatherBoardAdapter.MyViewHolder> {
    private Context context;
    private List<LeatherBoard> leatherBoardList;

    //declare interface
    private LeatherBoardAdapter.News_OnItemClicked onClick;

    //make interface like this
    public interface News_OnItemClicked {
        void news_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView w_number, w_fname, w_name, w_price;

        public MyViewHolder(View view) {
            super(view);
            w_number = (TextView) view.findViewById(R.id.w_number);
            w_fname = (TextView) view.findViewById(R.id.w_fname);
            w_name = (TextView) view.findViewById(R.id.w_name);
            w_price = (TextView) view.findViewById(R.id.w_price);
        }
    }

    public LeatherBoardAdapter(Context context, List<LeatherBoard> leatherBoardList) {
        this.context = context;
        this.leatherBoardList = leatherBoardList;
    }

    @Override
    public LeatherBoardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_leaderboard, parent, false);

        return new LeatherBoardAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LeatherBoardAdapter.MyViewHolder holder, final int position) {
        LeatherBoard leatherBoard = leatherBoardList.get(position);
        holder.w_number.setText(leatherBoard.getWinner_rank() + ".");
        holder.w_fname.setText(leatherBoard.getWinner_fname());
        holder.w_name.setText(leatherBoard.getWinner_name());
        holder.w_price.setText(leatherBoard.getWinner_price());
    }

    public void setOnClick(LeatherBoardAdapter.News_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return leatherBoardList.size();
    }
}
