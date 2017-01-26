package com.komitiwatches.customer;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by anandparmar on 25/01/17.
 */

public class KomitiWatchesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("komiti.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"KomitiWatches");
            directory.mkdirs();
        }
    }
}
