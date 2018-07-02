package com.lifeplaytrip.luckycoin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lifeplaytrip.luckycoin.R;
import com.lifeplaytrip.luckycoin.activity.HomeActivity;
import com.lifeplaytrip.luckycoin.adapter.GameAdapter;
import com.lifeplaytrip.luckycoin.adapter.Payment_History_Adapter;
import com.lifeplaytrip.luckycoin.model.Game;
import com.lifeplaytrip.luckycoin.model.PaymentStatus;
import com.lifeplaytrip.luckycoin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.lifeplaytrip.luckycoin.utils.Utils.dpToPx;

/**
 * Created by Priyanka on 2/27/2018.
 */

public class PaymentHistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private List<PaymentStatus> payment_history_List;
    private Payment_History_Adapter myadapter;
    int images = R.drawable.star_new;
    private SwipeRefreshLayout payment_swipe_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_history, container, false);


        ((HomeActivity) getActivity()).setActionBarTitle(Utils.PaymentHistoryFragment);
        payment_swipe_layout = (SwipeRefreshLayout) view.findViewById(R.id.payment_swipe_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.payment_history_recyclerview);
        payment_history_List = new ArrayList<>();
        myadapter = new Payment_History_Adapter(getContext(), payment_history_List);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Utils.GridSpacingItemDecoration(2, dpToPx(getContext(), 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myadapter);
//        adapter.setOnClick(this);
        payment_swipe_layout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        payment_swipe_layout.post(new Runnable() {
                                      @Override
                                      public void run() {
                                          payment_swipe_layout.setRefreshing(true);

                                          payment_historyApiRequest();
                                      }
                                  }
        );
        //payment_historyApiRequest();
        return view;


    }

    private void payment_historyApiRequest() {
        payment_swipe_layout.setRefreshing(true);
        PaymentStatus paymentStatus = new PaymentStatus("Sucess", images, "20", "Feb 10 2018", "02:00pm");
        payment_history_List.add(paymentStatus);
        paymentStatus = new PaymentStatus("Success", images, "120", "Feb 1 2018", "01:00pm");
        payment_history_List.add(paymentStatus);
        paymentStatus = new PaymentStatus("Success", images, "200", "Feb 23 2018", "12:00pm");
        payment_history_List.add(paymentStatus);
        paymentStatus = new PaymentStatus("Success", images, "400", "Jan 13 2018", "03:50pm");
        payment_history_List.add(paymentStatus);
        paymentStatus = new PaymentStatus("Success", images, "500", "Jan 14 2018", "06:30pm");
        payment_history_List.add(paymentStatus);
        paymentStatus = new PaymentStatus("Success", images, "120", "Feb 1 2018", "01:00pm");
        payment_history_List.add(paymentStatus);
        paymentStatus = new PaymentStatus("Success", images, "200", "Feb 23 2018", "12:00pm");
        payment_history_List.add(paymentStatus);
        paymentStatus = new PaymentStatus("Success", images, "400", "Jan 13 2018", "03:50pm");
        payment_history_List.add(paymentStatus);
        paymentStatus = new PaymentStatus("Success", images, "500", "Jan 14 2018", "06:30pm");
        payment_history_List.add(paymentStatus);

        myadapter.notifyDataSetChanged();
        payment_swipe_layout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        payment_historyApiRequest();
    }
}
