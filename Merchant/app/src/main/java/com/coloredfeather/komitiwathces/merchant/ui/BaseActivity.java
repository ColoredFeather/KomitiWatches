package com.coloredfeather.komitiwathces.merchant.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.firebase.analytics.FirebaseAnalytics;

import static com.coloredfeather.komitiwathces.merchant.Utils.AnalyticsConstances.ACTIVITY_EVENT;
import static com.coloredfeather.komitiwathces.merchant.Utils.AnalyticsConstances.ACTIVITY_NAME;
import static com.coloredfeather.komitiwathces.merchant.Utils.AnalyticsConstances.ACTIVITY_STATE;
import static com.coloredfeather.komitiwathces.merchant.Utils.AnalyticsConstances.STATE_CLOSED;
import static com.coloredfeather.komitiwathces.merchant.Utils.AnalyticsConstances.STATE_STARTED;

/**
 * Created by anandparmar on 19/03/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected FirebaseAnalytics firebaseAnalytics;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        logActivityStateEvent(STATE_STARTED);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
    }

    protected void logActivityStateEvent(String state) {
        Bundle bundle = new Bundle();
        bundle.putString(ACTIVITY_NAME, this.getClass().getSimpleName());
        bundle.putString(ACTIVITY_STATE, state);
        firebaseAnalytics.logEvent(ACTIVITY_EVENT, bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logActivityStateEvent(STATE_CLOSED);
    }

    public void showProgress(String message) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            if (!TextUtils.isEmpty(message)) {
                progressDialog.setMessage(message);
            }
            progressDialog.show();
        }
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
