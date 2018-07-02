package com.lifeplaytrip.luckycoin.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lifeplaytrip.luckycoin.R;
import com.lifeplaytrip.luckycoin.fragment.AboutUsFragment;
import com.lifeplaytrip.luckycoin.fragment.ContestFragment;
import com.lifeplaytrip.luckycoin.fragment.ForgotPassword_Fragment;
import com.lifeplaytrip.luckycoin.fragment.HistoryFragment;
import com.lifeplaytrip.luckycoin.fragment.LeatherBoardFragment;
import com.lifeplaytrip.luckycoin.fragment.Login_Fragment;
import com.lifeplaytrip.luckycoin.fragment.OfferFragment;
import com.lifeplaytrip.luckycoin.fragment.PaymentHistoryFragment;
import com.lifeplaytrip.luckycoin.fragment.ProfileFragment;
import com.lifeplaytrip.luckycoin.fragment.RuleandRegulationsFragment;
import com.lifeplaytrip.luckycoin.fragment.SignUp_Fragment;
import com.lifeplaytrip.luckycoin.fragment.WalletFragment;
import com.lifeplaytrip.luckycoin.utils.MyProgressDialog;
import com.lifeplaytrip.luckycoin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.lifeplaytrip.luckycoin.utils.Utils.isNetworkAvailable;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static FragmentManager fragmentManager;
    private static android.support.v4.app.FragmentTransaction fragmentTransaction;
    android.support.v4.app.FragmentManager fm;
    private TextView wallet_price;
    private String wallet_balance, user_id;
    private String wallet_api_url = "http://farebizgames.com/admin_coin/users/wallet.php";
    public static String s_user_id, s_user_name, s_user_email;
    public static String s_user_wallet;
    private TextView header_name, header_email;
    private RelativeLayout homeRelative;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        wallet_price = (TextView) headerView.findViewById(R.id.wallet_price);
        header_name = (TextView) headerView.findViewById(R.id.header_name);
        header_email = (TextView) headerView.findViewById(R.id.header_email);
        header_name.setText(s_user_name);
        header_email.setText(s_user_email);
        navigationView.setNavigationItemSelectedListener(this);
        Boolean result = isNetworkAvailable(HomeActivity.this);
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.container, new ContestFragment(),
                        Utils.ContestFragment).commit();
        setActionBarTitle("Home");
        Log.d("amit->>>>>>>>>>", s_user_id + "");

        call_wallet_api();
    }

    // Replace Login Fragment with animation

    public void replaceContestFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.container, new ContestFragment(),
                        Utils.ContestFragment).commit();
        call_wallet_api();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // Find the tag of signup and forgot password fragment
            Fragment ProfileFragment = fragmentManager
                    .findFragmentByTag(Utils.Profile_Fragment);
            Fragment LeatherBoardFragment = fragmentManager
                    .findFragmentByTag(Utils.LeatherBoard_Fragment);
            Fragment WalletFragment = fragmentManager
                    .findFragmentByTag(Utils.WalletFragment);
            Fragment HistoryFragment = fragmentManager
                    .findFragmentByTag(Utils.HistoryFragment);
            Fragment RuleandRegulationFragment = fragmentManager
                    .findFragmentByTag(Utils.RuleandRegulationFragment);
            Fragment OfferFragment = fragmentManager
                    .findFragmentByTag(Utils.OfferFragment);
            Fragment AboutUsFragment = fragmentManager
                    .findFragmentByTag(Utils.AboutUsFragment);
            Fragment BankDetailsFragment = fragmentManager
                    .findFragmentByTag(Utils.BankDetailsFragment);
            // Check if both are null or not
            // If both are not null then replace login fragment else do backpressed
            // task

            if (ProfileFragment != null)
                replaceContestFragment();
            else if (LeatherBoardFragment != null)
                replaceContestFragment();
            else if (WalletFragment != null)
                replaceContestFragment();
            else if (HistoryFragment != null)
                replaceContestFragment();
            else if (RuleandRegulationFragment != null)
                replaceContestFragment();
            else if (OfferFragment != null)
                replaceContestFragment();
            else if (AboutUsFragment != null)
                replaceContestFragment();
            else if (BankDetailsFragment != null)
                replaceContestFragment();
            else
                super.onBackPressed();
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //include settings here

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.container, new ContestFragment(),
                            Utils.ContestFragment).commit();
        } else if (id == R.id.nav_profile) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.container, new ProfileFragment(),
                            Utils.Profile_Fragment).commit();
        } else if (id == R.id.nav_leaderboard) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.container, new LeatherBoardFragment(),
                            Utils.LeatherBoard_Fragment).commit();
        } else if (id == R.id.nav_rule) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.container, new RuleandRegulationsFragment(),
                            Utils.RuleandRegulationFragment).commit();
        } else if (id == R.id.nav_wallet) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.container, new WalletFragment(),
                            Utils.WalletFragment).commit();

        } else if (id == R.id.nav_contest_history) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.container, new HistoryFragment(),
                            Utils.HistoryFragment).commit();
        } else if (id == R.id.nav_share) {
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject test");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "FareBiz Games  " + "https://play.google.com/store/apps/details?id=com.lifeplaytrip.luckycoin");
            startActivity(Intent.createChooser(i, "Share via"));
        }
//        else if (id == R.id.nav_send) {
//            ApplicationInfo app = getApplicationContext().getApplicationInfo();
//            String filePath = app.sourceDir;
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("*/*");
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
//            startActivity(Intent.createChooser(intent, "Share app via"));
//        }
        else if (id == R.id.nav_about) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.container, new AboutUsFragment(),
                            Utils.AboutUsFragment)
                    .commit();
        }
//        else if (id == R.id.nav_payment_history) {
//            fragmentManager
//                    .beginTransaction()
//                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
//                    .replace(R.id.container, new PaymentHistoryFragment(),
//                            Utils.PaymentHistoryFragment)
//                    .commit();
//
//        }
        else if (id == R.id.nav_offer) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new OfferFragment(),
                            Utils.OfferFragment)
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .commit();
        } else if ((id == R.id.nav_logout)) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            insert();
                            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                            intent.setFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK));
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void call_wallet_api() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String response = null;
        final String finalResponse = response;
        StringRequest postRequest = new StringRequest(Request.Method.POST, wallet_api_url
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

                    wallet_balance = profile.getString("wallet");
                    if (wallet_balance.equalsIgnoreCase("null")) {
                        wallet_price.setText("0.00");
                        s_user_wallet = 0 + "";
                    } else
                        wallet_price.setText(wallet_balance + ".00");
                    s_user_wallet = wallet_balance + "";

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, "Network Error", Toast.LENGTH_LONG).show();
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

    public void gotoWalletPage(View view) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, new WalletFragment(),
                        Utils.WalletFragment)
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void insert() {
        SharedPreferences pref = getSharedPreferences("ActivityPREF", 0);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("activity_user_executed", false);
        edt.commit();
    }
}
