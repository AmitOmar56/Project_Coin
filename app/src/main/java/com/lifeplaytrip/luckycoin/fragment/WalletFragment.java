package com.lifeplaytrip.luckycoin.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WalletFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String wallet_api_url = "http://farebizgames.com/admin_coin/users/wallet.php";
    private String wallet_balance, user_id;
    Button transfer_money;
    TextView tv_wallet_balance;
    private Button add_money;
    private String order_id;
    private static FragmentManager fragmentManager;

    private OnFragmentInteractionListener mListener;

    public WalletFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WalletFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WalletFragment newInstance(String param1, String param2) {
        WalletFragment fragment = new WalletFragment();
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
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        ((HomeActivity) getActivity()).setActionBarTitle(Utils.WalletFragment);

        tv_wallet_balance = view.findViewById(R.id.wallet_txt);
        add_money = (Button) view.findViewById(R.id.add_money);
        fragmentManager = getActivity().getSupportFragmentManager();

        call_wallet_api();
        transfer_money = view.findViewById(R.id.transfer_money);
        transfer_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to open new form for entering bank details
                openForm();
            }
        });
        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PayuActivity.class));
            }
        });
        return view;

    }

    private void call_wallet_api() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        final String response = null;
        final String finalResponse = response;
        StringRequest postRequest = new StringRequest(Request.Method.POST, wallet_api_url
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("priyanka", response);
                try {
                    Object json = new JSONTokener(response).nextValue();
                    JSONObject jsonObject = (JSONObject) json;

//                    if (jsonObject.getString("status").equals("STATUS_SUCCESS") || jsonObject.getString("status").equals("1")) {


                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject profile = jsonArray.getJSONObject(0);
                    user_id = profile.getString("user_id");

                    wallet_balance = profile.getString("wallet");
                    if (wallet_balance.equalsIgnoreCase("null")) {
                        tv_wallet_balance.setText("0.00");
                    } else
                        tv_wallet_balance.setText(wallet_balance + ".00");

                    Log.d("Priyanka", user_id);
                    Log.d("Priyanka", wallet_balance);


                    //  String tv_name=jsonObject.getString("")


                    // new MainActivity().replaceLoginFragment();

//                    } else {
//                        Toast.makeText(getContext(), jsonObject.getString("data"), Toast.LENGTH_LONG).show();
//                    }
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


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    private void openForm() {
        Fragment fragment = new BankDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("money", wallet_balance);
        fragment.setArguments(bundle);
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.container, fragment,
                        Utils.BankDetailsFragment).commit();
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
