package com.lifeplaytrip.luckycoin.fragment;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lifeplaytrip.luckycoin.R;
import com.lifeplaytrip.luckycoin.activity.HomeActivity;
import com.lifeplaytrip.luckycoin.utils.MyProgressDialog;
import com.lifeplaytrip.luckycoin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RuleandRegulationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RuleandRegulationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RuleandRegulationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View linearLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView rules;

    private OnFragmentInteractionListener mListener;
    private String ruleApi = "http://farebizgames.com/admin_coin/users/rule.php";
    private String rule_name;
    private String rule_desc;

    public RuleandRegulationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RuleandRegulationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RuleandRegulationsFragment newInstance(String param1, String param2) {
        RuleandRegulationsFragment fragment = new RuleandRegulationsFragment();
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
        View view = inflater.inflate(R.layout.fragment_ruleand_regulations, container, false);
//        ((HomeActivity) getActivity()).setActionBarTitle(Utils.RuleandRegulationFragment);
        linearLayout = view.findViewById(R.id.info);
        ruleApiCall();
        return view;
    }

    private void ruleApiCall() {
        MyProgressDialog.showPDialog(getContext());

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, ruleApi,
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
                                    rule_name = profile.getString("rule_name");
                                    rule_desc = profile.getString("rule_descr");
                                    TextView valueTV = new TextView(getContext());
                                    TextView valueTV1 = new TextView(getContext());
                                    valueTV.setText(rule_name);
                                    valueTV.setGravity(1);
                                    valueTV.setTextSize(18);
                                    valueTV.setTextColor(getResources().getColor(R.color.white_greyish));
                                    valueTV1.setText(rule_desc);
                                    valueTV1.setGravity(1);
                                    valueTV1.setTextColor(getResources().getColor(R.color.white_greyish));

                                    valueTV.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    ((LinearLayout) linearLayout).addView(valueTV);
                                    ((LinearLayout) linearLayout).addView(valueTV1);
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
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
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
