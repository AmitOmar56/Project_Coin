package com.lifeplaytrip.luckycoin.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.lifeplaytrip.luckycoin.CustomViews.CustomProgressView;
import com.lifeplaytrip.luckycoin.R;


/**
 * Created by user on 10/23/2017.
 */


public class MyProgressDialog {

    private static ProgressDialog pDialog = null;
    private static CustomProgressView progressView = null;

    public static void showPDialog(Context context) {

        progressView = new CustomProgressView(context);
        progressView.stopLoading();

        if (pDialog != null) {
            try {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            } catch (Exception e) {
            }
            pDialog = null;
        }

        pDialog = new ProgressDialog(context, R.style.MyProgressTheme);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
        pDialog.setContentView(progressView);
        progressView.startLoading();
    }

    public static void hidePDialog() {
        if (pDialog != null) {
            try {
                pDialog.dismiss();
            } catch (Exception e) {
            }
        }
        if (progressView != null) {
            progressView.stopLoading();
        }
    }
}
