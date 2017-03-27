package com.coloredfeather.komitiwathces.merchant;

import android.app.Application;

import com.squareup.picasso.Picasso;

import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by anandparmar on 21/02/17.
 */

public class MerchantApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        EasyImage.configuration(this).setImagesFolderName("MyShop");
        Picasso.with(this).setLoggingEnabled(true);
    }


}
