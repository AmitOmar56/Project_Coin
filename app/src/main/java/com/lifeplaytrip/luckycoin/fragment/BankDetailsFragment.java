package com.lifeplaytrip.luckycoin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.lifeplaytrip.luckycoin.activity.QuestionActivity;
import com.lifeplaytrip.luckycoin.customtoast.CustomToast;
import com.lifeplaytrip.luckycoin.model.InputFilterMinMax;
import com.lifeplaytrip.luckycoin.utils.MyProgressDialog;
import com.lifeplaytrip.luckycoin.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lifeplaytrip.luckycoin.activity.HomeActivity.s_user_id;
import static com.lifeplaytrip.luckycoin.utils.Utils.isNetworkAvailable;

public class BankDetailsFragment extends Fragment implements View.OnClickListener {
    private EditText edit_acholder_name, edit_account_no, edit_ifsc_no, edit_pan_no, edit_amount_to_transfer;
    private Button button_transfer_money;
    private String s_acholder_name, s_account_no, s_ifsc_no, s_pan_no, s_amount_to_transfer;
    private static View view;
    private String transfer_api_url = "http://farebizgames.com/admin_coin/users/request.php";
    private String money;
    private LinearLayout bankDetailLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bank_details, container, false);
        ((HomeActivity) getActivity()).setActionBarTitle(Utils.BankDetailsFragment);

        initId();
        Bundle bundle = getArguments();
        money = String.valueOf(bundle.getString("money"));
        setListeners();
        edit_amount_to_transfer.setFilters(new InputFilter[]{new InputFilterMinMax("1", money)});
        edit_amount_to_transfer.setHint("Amount* " + "(Max Limit " + money + ")");
        return view;
    }

    private void initId() {
        edit_acholder_name = (EditText) view.findViewById(R.id.edit_acholder_name);
        edit_account_no = (EditText) view.findViewById(R.id.edit_account_no);
        edit_ifsc_no = (EditText) view.findViewById(R.id.edit_ifsc_no);
        edit_pan_no = (EditText) view.findViewById(R.id.edit_pan_no);
        edit_amount_to_transfer = (EditText) view.findViewById(R.id.edit_amount_to_transfer);
        button_transfer_money = (Button) view.findViewById(R.id.button_transfer_money);
        bankDetailLayout = (LinearLayout) view.findViewById(R.id.bankDetailLayout);
    }

    private void setListeners() {
        button_transfer_money.setOnClickListener(this);
    }

    private void checkValidation() {

        // Get all edittext texts
        s_acholder_name = edit_acholder_name.getText().toString();
        s_account_no = edit_account_no.getText().toString();
        s_ifsc_no = edit_ifsc_no.getText().toString();
        s_pan_no = edit_pan_no.getText().toString();
        s_amount_to_transfer = edit_amount_to_transfer.getText().toString();


        // Check if all strings are null or not
        if (s_acholder_name.equals("") || s_acholder_name.length() == 0
                || s_account_no.equals("") || s_account_no.length() == 0
                || s_ifsc_no.equals("") || s_ifsc_no.length() == 0
                || s_pan_no.equals("") || s_pan_no.length() == 0
                || s_amount_to_transfer.equals("")
                || s_amount_to_transfer.length() == 0)

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");

            //Check if mobile number is not valid
        else if (s_pan_no.length() < 10) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter Valid Pan Number");
        } else {
            tranfer_moneyApiCall();
            Toast.makeText(getActivity(), "Request Registered", Toast.LENGTH_SHORT)
                    .show();

        }
    }

    private void tranfer_moneyApiCall() {
        Boolean result = isNetworkAvailable(getActivity());
        if (result) {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            final String response = null;
            final String finalResponse = response;
            StringRequest postRequest = new StringRequest(Request.Method.POST, transfer_api_url
                    , new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("amit", response);
                    try {
                        Object json = new JSONTokener(response).nextValue();
                        JSONObject jsonObject = (JSONObject) json;
                        if (jsonObject.getString("status").equals("STATUS_SUCCESS") || jsonObject.getString("status").equals("1")) {
                            startActivity(new Intent(getContext(), HomeActivity.class));
                            Toast.makeText(getContext(), jsonObject.getString("data"), Toast.LENGTH_LONG).show();
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

                    params.put("user_id", s_user_id + "");
                    params.put("name", s_acholder_name);
                    params.put("ac_no", s_account_no);
                    params.put("ifsc", s_ifsc_no);
                    params.put("pan", s_pan_no);
                    params.put("rupee", s_amount_to_transfer);
                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(bankDetailLayout, "Data Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_transfer_money:
                checkValidation();
                break;
        }
    }
}
