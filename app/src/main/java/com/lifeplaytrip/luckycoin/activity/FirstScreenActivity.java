package com.lifeplaytrip.luckycoin.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lifeplaytrip.luckycoin.R;

import static com.lifeplaytrip.luckycoin.activity.HomeActivity.s_user_email;
import static com.lifeplaytrip.luckycoin.activity.HomeActivity.s_user_id;
import static com.lifeplaytrip.luckycoin.activity.HomeActivity.s_user_name;
import static com.lifeplaytrip.luckycoin.utils.Utils.isNetworkAvailable;

public class FirstScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private Intent intent;


    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;
    public static final int ITEM_DELAY = 300;

    private boolean animationStarted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.noActionBar);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                SharedPreferences pref = getSharedPreferences("ActivityPREF", 0);
                if (pref.getBoolean("activity_user_executed", false)) {
                    s_user_id = pref.getString("user_id", null);
                    s_user_name = pref.getString("name", null);
                    s_user_email = pref.getString("email", null);
                    // Log.d("first", s_user_id + s_user_name + s_user_email);
                    Boolean result = isNetworkAvailable(FirstScreenActivity.this);
                    if (result) {
                        intent = new Intent(FirstScreenActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        try {
                            AlertDialog alertDialog = new AlertDialog.Builder(FirstScreenActivity.this).create();

                            alertDialog.setTitle("Info");
                            alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

                            alertDialog.show();
                        } catch (Exception e) {
                            Log.d("Tag", "Show Dialog: " + e.getMessage());
                        }
                    }
                    // finish();
                } else {
                    intent = new Intent(FirstScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
//                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (!hasFocus || animationStarted) {
            return;
        }

        animate();

        super.onWindowFocusChanged(hasFocus);
    }

    private void animate() {
        ImageView logoImageView = (ImageView) findViewById(R.id.img_logo);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);

        ViewCompat.animate(logoImageView)
                .translationY(-250)
                .setStartDelay(STARTUP_DELAY)
                .setDuration(ANIM_ITEM_DURATION).setInterpolator(
                new DecelerateInterpolator(1.2f)).start();

        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            if (!(v instanceof Button)) {
                viewAnimator = ViewCompat.animate(v)
                        .translationY(50).alpha(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(1000);
            } else {
                viewAnimator = ViewCompat.animate(v)
                        .scaleY(1).scaleX(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(500);
            }

            viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
        }
    }
}
