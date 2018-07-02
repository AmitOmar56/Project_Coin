package com.lifeplaytrip.luckycoin.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lifeplaytrip.luckycoin.PaymentGateway.WebViewActivity;
import com.lifeplaytrip.luckycoin.R;
import com.lifeplaytrip.luckycoin.activity.HomeActivity;
import com.lifeplaytrip.luckycoin.activity.MainActivity;
import com.lifeplaytrip.luckycoin.activity.PaymentModeActivity;
import com.lifeplaytrip.luckycoin.activity.QuestionActivity;
import com.lifeplaytrip.luckycoin.payugateway.PayuActivity;
import com.lifeplaytrip.luckycoin.utils.AvenuesParams;
import com.lifeplaytrip.luckycoin.utils.MyProgressDialog;
import com.lifeplaytrip.luckycoin.utils.ServiceUtility;
import com.lifeplaytrip.luckycoin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import static com.lifeplaytrip.luckycoin.activity.HomeActivity.s_user_id;
import static com.lifeplaytrip.luckycoin.utils.Utils.isNetworkAvailable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContestFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //url
    private static final String contest_api_url = "http://farebizgames.com/admin_coin/users/sess_list.php";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String order_id;

    private OnFragmentInteractionListener mListener;
    private static View view;
    private Button play_morning, play_evening, play_mega;
    private TextView session_morning, morning_text, morning_price, session_evening, evening_text, evening_price;

    private String morning_session_id, morning_session_name, morning_session_price, morning_session_desc, evening_session_id,
            evening_session_name, evening_session_price, evening_session_text;
    private String morning_user_id;
    private String morning_p_status;
    private String evening_user_id;
    private String session_key_id;
    private String session_register_api_url = "http://farebizgames.com/admin_coin/users/sess_reg.php";
    private LinearLayout contest_layout;
    private int contest_time;

    public ContestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContestFragment newInstance(String param1, String param2) {
        ContestFragment fragment = new ContestFragment();
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
        view = inflater.inflate(R.layout.fragment_contest, container, false);
        ((HomeActivity) getActivity()).setActionBarTitle(Utils.ContestFragment);

        initId();
        call_contest_api();
        setListeners();

        return view;
    }

    private void setListeners() {
        play_evening.setOnClickListener(this);
        play_morning.setOnClickListener(this);
        play_mega.setOnClickListener(this);
    }

    private void call_contest_api() {
        Boolean result = isNetworkAvailable(getActivity());
        if (result) {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            final String response = null;
            final String finalResponse = response;
            StringRequest getRequest = new StringRequest(Request.Method.POST, contest_api_url
                    , new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("amit", response);
                    try {
                        Object json = new JSONTokener(response).nextValue();
                        JSONObject jsonObject = (JSONObject) json;

                        if (jsonObject.getString("status").equals("STATUS_SUCCESS") || jsonObject.getString("status").equals("1")) {
                            contest_time = jsonObject.getInt("time");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            JSONObject morning_session = jsonArray.getJSONObject(0);
                            morning_session_id = morning_session.getString("sessions_id");
                            morning_session_name = morning_session.getString("se_name");
                            morning_session_desc = morning_session.getString("se_desc");
                            morning_session_price = morning_session.getString("se_price");
                            morning_user_id = morning_session.getString("user_id");
                            if (morning_user_id.equalsIgnoreCase("null")) {
                                play_morning.setText("Play and win Rewards");
                            } else {
                                if (morning_session.getString("q_status").equals("0")) {
                                    play_morning.setText("Play and win Rewards");
                                } else if (morning_session.getString("p_status").equals("0")) {
                                    play_morning.setText("Complete Your Game");
                                } else {
                                    play_morning.setText("Already Played");
                                    play_morning.setFocusable(false);
                                }
                            }
                            session_morning.setText(morning_session_name);
                            morning_text.setText(morning_session_desc);
                            morning_price.setText(morning_session_price);


                            JSONObject evening_session = jsonArray.getJSONObject(1);
                            Log.d("evening", evening_session + "");
                            evening_session_id = evening_session.getString("sessions_id");
                            evening_session_name = evening_session.getString("se_name");
                            evening_session_text = evening_session.getString("se_desc");
                            evening_session_price = evening_session.getString("se_price");
                            evening_user_id = evening_session.getString("user_id");

                            if (evening_user_id.equalsIgnoreCase("null")) {
                                play_evening.setText("Play and win Rewards");
                            } else {
                                if (evening_session.getString("q_status").equals("0")) {
                                    play_evening.setText("Play and win Rewards");
                                } else if (evening_session.getString("p_status").equals("0")) {
                                    play_evening.setText("Complete Your Game");
                                } else {
                                    play_evening.setText("Already Played");
                                    play_evening.setFocusable(false);
                                }
                            }
                            session_evening.setText(evening_session_name);
                            evening_text.setText(evening_session_text);
                            evening_price.setText(evening_session_price);


                            Log.d("name->>>>", evening_session_name + morning_session_name);
                        } else {
                            MyProgressDialog.hidePDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           // Toast.makeText(getContext(), "Network Error", Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", s_user_id + "");
                    return params;
                }
            };
            getRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(getRequest);
        }
    }

    private void call_registerApi_api() {
        Boolean result = isNetworkAvailable(getActivity());
        if (result) {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            final String response = null;
            final String finalResponse = response;
            StringRequest getRequest = new StringRequest(Request.Method.POST, session_register_api_url
                    , new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("amit", response);
                    try {
                        Object json = new JSONTokener(response).nextValue();
                        JSONObject jsonObject = (JSONObject) json;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", s_user_id + "");
                    params.put("sessions_id", session_key_id + "");

                    return params;
                }
            };
            getRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(getRequest);
        } else {
            Snackbar.make(contest_layout, "Data Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    private void initId() {
        play_morning = (Button) view.findViewById(R.id.play_morning);
        play_evening = (Button) view.findViewById(R.id.play_evening);
        session_morning = view.findViewById(R.id.morning_session);
        morning_text = view.findViewById(R.id.morning_text);
        morning_price = view.findViewById(R.id.morning_price);
        session_evening = view.findViewById(R.id.evening_session);
        evening_text = view.findViewById(R.id.evening_text);
        evening_price = view.findViewById(R.id.evening_price);
        play_mega = (Button) view.findViewById(R.id.play_mega);
        contest_layout = (LinearLayout) view.findViewById(R.id.contest_layout);
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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.play_morning:
                session_key_id = morning_session_id;
                if (contest_time < 120000) {
                    if (play_morning.getText().equals("Play and win Rewards")) {
                        call_registerApi_api();
                        // paymentPage();
                        Intent intent = new Intent(getContext(), QuestionActivity.class);
                        intent.putExtra("key_id", session_key_id + "");
                        startActivity(intent);
                    } else if (play_morning.getText().equals("Complete Your Game")) {
                        Intent intent = new Intent(getContext(), PaymentModeActivity.class);
                        intent.putExtra("key_id", session_key_id + "");
                        startActivity(intent);
                    }
                } else {
                    Snackbar.make(contest_layout, "Session not Start......", Snackbar.LENGTH_LONG).show();
                }
                break;

            case R.id.play_evening:
                session_key_id = evening_session_id;
                if (contest_time > 120000) {
                    if (play_evening.getText().equals("Play and win Rewards")) {
                        call_registerApi_api();
                        // paymentPage();
                        Intent intent = new Intent(getContext(), QuestionActivity.class);
                        intent.putExtra("key_id", session_key_id);
                        startActivity(intent);
                    } else if (play_evening.getText().equals("Complete Your Game")) {
                        Intent intent = new Intent(getContext(), PaymentModeActivity.class);
                        intent.putExtra("key_id", session_key_id);
                        startActivity(intent);
                    }
                } else {
                    Snackbar.make(contest_layout, "Session not Start......", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.play_mega:
                Snackbar.make(contest_layout, "Coming Soon......", Snackbar.LENGTH_LONG).show();

                break;
        }
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

    private void paymentPage() {

        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull("AVTG76FB43BS37GTSB").toString().trim());
        intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(166015).toString().trim());
        intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(order_id).toString().trim());
        intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull("INR").toString().trim());
        intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull("1.00").toString().trim());

        intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull("http://esaymart.com/mobile/ccavResponseHandler.php").toString().trim());
        intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull("http://esaymart.com/mobile/ccavResponseHandler.php").toString().trim());
        intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull("http://esaymart.com/mobile/GetRSA.php").toString().trim());
        intent.putExtra("key_id", session_key_id);

        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        //generating new order number for every transaction
        Integer randomNum = ServiceUtility.randInt(0, 9999999);
        order_id = randomNum.toString();
    }
}
