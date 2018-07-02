package com.lifeplaytrip.luckycoin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lifeplaytrip.luckycoin.R;
import com.lifeplaytrip.luckycoin.model.Game;
import com.lifeplaytrip.luckycoin.model.PaymentStatus;

import java.util.List;

/**
 * Created by Priyanka on 2/27/2018.
 */

public class Payment_History_Adapter extends RecyclerView.Adapter<Payment_History_Adapter.MyViewHolder>
{
    private Context context;
    private List<PaymentStatus> payment_history_list;
    public Payment_History_Adapter(Context context, List<PaymentStatus> payment_history_list)
    {
        this.context = context;
        this.payment_history_list = payment_history_list;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView payment_status, remaining_points, payment_date, payment_time;
        public ImageView payment_status_image;


        public MyViewHolder(View view) {
            super(view);
            payment_status = (TextView) view.findViewById(R.id.payment_success);
            remaining_points = (TextView) view.findViewById(R.id.cash_remaining);
            payment_date = (TextView) view.findViewById(R.id.payment_date);
            payment_time = (TextView) view.findViewById(R.id.payment_time);
            payment_status_image = (ImageView) view.findViewById(R.id.success_payment_image);
        }
    }

    @Override
    public Payment_History_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_history, parent, false);

        return new Payment_History_Adapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final Payment_History_Adapter.MyViewHolder holder, int position)
    {
       PaymentStatus paymentStatus=payment_history_list.get(position);
        holder.payment_status.setText(paymentStatus.getPayment_status());
        holder.payment_time.setText(paymentStatus.getPayment_time());
        holder.remaining_points.setText(paymentStatus.getCash_remaining());
        holder.payment_date.setText(paymentStatus.getPayment_date());
        Glide.with(context).load(paymentStatus.getPayment_status_image()).into(holder.payment_status_image);
    }

    @Override
    public int getItemCount()
    {
        return payment_history_list.size();
    }
}
