package com.coloredfeather.komitiwatches.customer.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by anandparmar on 14/02/17.
 */

public class MyFCMInstanceIDListnerService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("FCMInstanceIdListner", "Refreshed token: " + refreshedToken);

        updateFCMTokenToServer();
    }

    private void updateFCMTokenToServer() {
        Log.i("FCMInstanceIdListner", "Token sent to server.");
    }
}
