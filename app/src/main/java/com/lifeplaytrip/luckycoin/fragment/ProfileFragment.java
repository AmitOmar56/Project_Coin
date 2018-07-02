package com.lifeplaytrip.luckycoin.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.lifeplaytrip.luckycoin.activity.MainActivity;
import com.lifeplaytrip.luckycoin.utils.MyProgressDialog;
import com.lifeplaytrip.luckycoin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.lifeplaytrip.luckycoin.activity.HomeActivity.s_user_id;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String profile_api_url = "http://farebizgames.com/admin_coin/users/prof.php";
    private static String update_api_url = "http://farebizgames.com/admin_coin/users/update.php";
    private View view;
    private EditText full_name, email_id, mobile_no, d_o_b;
    private TextView profile_name, profile_id;
    private Button update_profile;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String user_id, name, email, dob, wallet, pass, phone;
    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ((HomeActivity) getActivity()).setActionBarTitle(Utils.Profile_Fragment);

        initViews();
        callProfileApi();

        return view;

    }

    private void initViews() {

        profile_name = view.findViewById(R.id.user_profile_name);
        profile_id = view.findViewById(R.id.user_profile_short_bio);
        full_name = view.findViewById(R.id.fullName);
        email_id = view.findViewById(R.id.userEmailId);
        mobile_no = view.findViewById(R.id.mobileNumber);
        d_o_b = view.findViewById(R.id.date_of_birth);
        d_o_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
        update_profile = view.findViewById(R.id.update_Btn);
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (full_name.getText() != null && full_name.getText().length() > 0 && email_id.getText() != null && email_id.getText().length() > 0) {
                    callUpdateApi();
                } else {
                    Toast.makeText(getContext(), "Fields Cannot Be Empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        d_o_b.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        String dateOfBirth = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void callUpdateApi() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        final String response = null;
        final String finalResponse = response;
        StringRequest postRequest = new StringRequest(Request.Method.POST, update_api_url
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("amit", response);
                try {
                    Object json = new JSONTokener(response).nextValue();
                    JSONObject jsonObject = (JSONObject) json;
                    if (jsonObject.getString("status").equals("STATUS_SUCCESS") || jsonObject.getString("status").equals("1")) {

                        Toast.makeText(getContext(), "profile update successfully", Toast.LENGTH_LONG).show();
                        callProfileApi();
                        //new MainActivity().replaceLoginFragment();

                    } else {
                        Toast.makeText(getContext(), jsonObject.getString("data"), Toast.LENGTH_LONG).show();
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
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("name", full_name.getText().toString());
                params.put("email", email_id.getText().toString());
                params.put("dob", d_o_b.getText().toString());
                params.put("user_id", user_id);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void callProfileApi() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        final String response = null;
        final String finalResponse = response;
        StringRequest postRequest = new StringRequest(Request.Method.POST, profile_api_url
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("amit", response);
                try {
                    Object json = new JSONTokener(response).nextValue();
                    JSONObject jsonObject = (JSONObject) json;

//                    if (jsonObject.getString("status").equals("STATUS_SUCCESS") || jsonObject.getString("status").equals("1")) {


                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject profile = jsonArray.getJSONObject(0);
                    user_id = profile.getString("user_id");
                    name = profile.getString("name");
                    email = profile.getString("email");
                    phone = profile.getString("phone");
                    wallet = profile.getString("wallet");
                    dob = profile.getString("dob");
                    pass = profile.getString("pass");
                    Log.d("Priyanka", user_id);
                    Log.d("Priyanka", name);
                    Log.d("Priyanka", email);
                    Log.d("Priyanka", phone);
                    Log.d("Priyanka", wallet);
                    Log.d("Priyanka", dob);
                    Log.d("Priyanka", pass);

                    profile_name.setText(name);
                    profile_id.setText(email);
                    full_name.setText(name);
                    email_id.setText(email);
                    mobile_no.setText(phone);
                    d_o_b.setText(dob);
                    insert();

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

    public void insert() {
        SharedPreferences pref = this.getActivity().getSharedPreferences("ActivityPREF", 0);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString("name", name);
        edt.putString("email", email);
        edt.putBoolean("activity_user_executed", true);
        edt.commit();
    }
}
