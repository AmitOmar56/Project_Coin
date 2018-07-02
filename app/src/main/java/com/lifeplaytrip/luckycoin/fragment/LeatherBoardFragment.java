package com.lifeplaytrip.luckycoin.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lifeplaytrip.luckycoin.R;
import com.lifeplaytrip.luckycoin.activity.HomeActivity;
import com.lifeplaytrip.luckycoin.adapter.LeatherBoardAdapter;
import com.lifeplaytrip.luckycoin.model.LeatherBoard;
import com.lifeplaytrip.luckycoin.utils.MyProgressDialog;
import com.lifeplaytrip.luckycoin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import static com.lifeplaytrip.luckycoin.utils.Utils.dpToPx;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LeatherBoardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeatherBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeatherBoardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View view;
    private RecyclerView recyclerView;
    private LeatherBoardAdapter adapter = null;
    private List<LeatherBoard> leatherBoardList;
    private LeatherBoard leatherBoard;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String leaderboardApi = "http://farebizgames.com/admin_coin/users/leaderboard.php";
    private String winner_name;
    private String winning_rewards;

    public LeatherBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeatherBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeatherBoardFragment newInstance(String param1, String param2) {
        LeatherBoardFragment fragment = new LeatherBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_leather_board, container, false);
        ((HomeActivity) getActivity()).setActionBarTitle(Utils.LeatherBoard_Fragment);

        /*****************(Start) code For Card View*****************/

        recyclerView = (RecyclerView) view.findViewById(R.id.leatherboard_recyclerView);
        leatherBoardList = new ArrayList<>();
        adapter = new LeatherBoardAdapter(getContext(), leatherBoardList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Utils.GridSpacingItemDecoration(2, dpToPx(getContext(), 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
//        adapter.setOnClick(this);
        leaderboardApiCall();
        /*****************(End) code For Card View Vertical*****************/
        return view;
    }

    private void leaderboardApiCall() {
        MyProgressDialog.showPDialog(getContext());

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, leaderboardApi,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit", response);
                        try {
                            Object json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;
                            if (jsonObject.getString("status").equals("STATUS_SUCCESS") || jsonObject.getString("status").equals("1")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("jsonObject", jsonObject + "");
                                Log.d("jsonArray", jsonArray + "");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject profile = jsonArray.getJSONObject(i);
                                    winner_name = profile.getString("name");
                                    winning_rewards = profile.getString("reward");

                                    // winner_fName = winner_name.substring(0, winner_name.indexOf(' '));
                                    leatherBoard = new LeatherBoard(winner_name, Character.toUpperCase(winner_name.charAt(0)) + "", winning_rewards, i + 1, i + 1);
                                    leatherBoardList.add(leatherBoard);
                                    adapter.notifyDataSetChanged();

                                }
                                MyProgressDialog.hidePDialog();
                            } else {
                                MyProgressDialog.hidePDialog();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new

                DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void signupRequest() {
        LeatherBoard leatherBoard = new LeatherBoard("amit", "A", "20", 1, 1);
        leatherBoardList.add(leatherBoard);
        leatherBoard = new LeatherBoard("Sachin", "S", "10", 1, 2);
        leatherBoardList.add(leatherBoard);
        leatherBoard = new LeatherBoard("jayant", "J", "20", 1, 3);
        leatherBoardList.add(leatherBoard);
        leatherBoard = new LeatherBoard("Sachin", "S", "10", 1, 4);
        leatherBoardList.add(leatherBoard);
        leatherBoard = new LeatherBoard("amit", "A", "15", 1, 5);
        leatherBoardList.add(leatherBoard);
        leatherBoard = new LeatherBoard("Jayant", "J", "10", 1, 6);
        leatherBoardList.add(leatherBoard);
        adapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
