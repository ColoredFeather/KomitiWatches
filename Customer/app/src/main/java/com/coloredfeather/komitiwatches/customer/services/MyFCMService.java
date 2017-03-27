package com.coloredfeather.komitiwatches.customer.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.coloredfeather.komitiwatches.customer.R;
import com.coloredfeather.komitiwatches.customer.ui.FilterActivity;
import com.coloredfeather.komitiwatches.customer.ui.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by anandparmar on 13/02/17.
 */

public class MyFCMService extends FirebaseMessagingService{
    private static final String LOG_TAG = "CF -> MyFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String type = remoteMessage.getData().get("type");

        if("text_notification".equals(type)){
            showTextNotification(remoteMessage);
        } else if("photo_notification".equals(type)){
            showPhotoNotification(remoteMessage);
        }
    }

    private void showTextNotification(RemoteMessage remoteMessage){
        Intent i= new Intent(this, MainActivity.class);
        PendingIntent pi= PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder bn= new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setContentIntent(pi);

        NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, bn.build());
    }

    private void showPhotoNotification(final RemoteMessage remoteMessage){
        new AsyncTask<Void, Void, Bitmap>(){
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap remotePhoto = null;
                try {
                    remotePhoto = BitmapFactory.decodeStream((InputStream) new URL((String) remoteMessage.getData().get("image_url")).getContent());
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
                return remotePhoto;
            }

            @Override
            protected void onPostExecute(Bitmap remotePhoto) {
                super.onPostExecute(remotePhoto);

                android.support.v7.app.NotificationCompat.BigPictureStyle notiStyle = new android.support.v7.app.NotificationCompat.BigPictureStyle();
                notiStyle.setSummaryText(remoteMessage.getNotification().getBody());
                notiStyle.bigPicture(remotePhoto);

                Intent i= new Intent(MyFCMService.this, MainActivity.class);
                PendingIntent pi= PendingIntent.getActivity(MyFCMService.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder bn= new NotificationCompat.Builder(MyFCMService.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(MyFCMService.this.getResources(), R.mipmap.ic_launcher))
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true)
                        .setContentIntent(pi);

                if(remotePhoto!=null){
                    bn.setStyle(notiStyle);
                }

                NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, bn.build());
            }
        }.execute();
    }
}
