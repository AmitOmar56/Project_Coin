package com.lifeplaytrip.luckycoin.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lifeplaytrip.luckycoin.R;
import com.lifeplaytrip.luckycoin.utils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import static com.lifeplaytrip.luckycoin.activity.HomeActivity.s_user_id;

public class QuestionActivity extends AppCompatActivity {

    TextView question;
    RadioButton a, b, c, d;
    private RadioGroup radioGroup;
    String checkBox = "";
    String check_answer;
    private String register_status;
    private String question_api_url = "http://farebizgames.com/admin_coin/users/que.php";
    String ques_tion, option_a, option_b, option_c, option_d, correct_option, question_id;
    private String session_key_id;
    private String question_register_api_url = "http://farebizgames.com/admin_coin/users/after_que.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataFromIntent();
        question = findViewById(R.id.play_question);
        a = findViewById(R.id.optionA);
        b = findViewById(R.id.optionB);
        c = findViewById(R.id.optionC);
        d = findViewById(R.id.optionD);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                if (null != radioButton && checkedId > -1) {
                    checkBox = radioButton.getText().toString();
                    Toast.makeText(QuestionActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        callQuestionsApi();

    }

    public void getDataFromIntent() {
        if (getIntent() != null) {
            session_key_id = getIntent().getStringExtra("key_id");
            Log.d("key_id", session_key_id);
        }
    }

    private void callQuestionsApi() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final String response = null;
        final String finalResponse = response;
        StringRequest getRequest = new StringRequest(Request.Method.GET, question_api_url
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("priyanka", response);
                try {
                    Object json = new JSONTokener(response).nextValue();
                    JSONObject jsonObject = (JSONObject) json;

//                    if (jsonObject.getString("status").equals("STATUS_SUCCESS") || jsonObject.getString("status").equals("1")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    JSONObject question_list = jsonArray.getJSONObject(0);
                    question_id = question_list.getString("question_id");
                    ques_tion = question_list.getString("question");
                    option_a = question_list.getString("a");
                    option_b = question_list.getString("b");
                    option_c = question_list.getString("c");
                    option_d = question_list.getString("d");
                    correct_option = question_list.getString("ans");

                    question.setText(ques_tion);
                    a.setText(option_a);
                    b.setText(option_b);
                    c.setText(option_c);
                    d.setText(option_d);


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
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
        };
        getRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest);
    }

    private void call_question_Api_api() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String response = null;
        final String finalResponse = response;
        StringRequest getRequest = new StringRequest(Request.Method.POST, question_register_api_url
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
                        Toast.makeText(QuestionActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", s_user_id+ "");
                params.put("sessions_id", session_key_id + "");
                params.put("question", check_answer + "");

                return params;
            }
        };
        getRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest);
    }

    public void submit_question(View view) {
        if (checkBox.equals("")) {
            Toast.makeText(this, "Please Click ", Toast.LENGTH_LONG).show();
        } else {
            if (checkBox.equals(correct_option)) {
                check_answer = "1";
                register_status = "You are Register in this session";
            } else {
                check_answer = "0";
                register_status = "You are not Register in this session";

            }
            call_question_Api_api();
            Intent intent=new Intent(this, PaymentModeActivity.class);
            intent.putExtra("key_id", session_key_id);
            startActivity(intent);
            Toast.makeText(this, register_status + "", Toast.LENGTH_LONG).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
