package com.coloredfeather.komitiwathces.merchant.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.coloredfeather.komitiwathces.merchant.R;
import com.securepreferences.SecurePreferences;

/**
 * Created by anandparmar on 19/03/17.
 */

public class Preferences {
    private static final String PREF_MOBILE_NUMBER = "MOBILE_NUMBER";
    private static final String PREF_SESSION_TOKEN = "SESSION_TOKEN";
    private static final String PREF_PARTNER_CODE = "PARTNER_CODE";

    private static SharedPreferences sharedPreferences;

    private static SharedPreferences getSharedPreferences(Context context){
        if(sharedPreferences == null){
            sharedPreferences = new SecurePreferences(context,
                    context.getResources().getString(R.string.secured_pref_paswword),
                    context.getResources().getString(R.string.shared_pref_xml));
        }
        return sharedPreferences;
    }

    public static void setMobileNumber(Context context, String mobileNumber){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }

    public static String getMobileNumber(Context context) {
        return getSharedPreferences(context).getString(PREF_MOBILE_NUMBER, null);
    }

    public static void setSessionToken(Context context, String sessionToken){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_SESSION_TOKEN, sessionToken);
        editor.commit();
    }

    public static String getSessionToken(Context context) {
        return getSharedPreferences(context).getString(PREF_SESSION_TOKEN, null);
    }

    public static void setPartnerCode(Context context, String partnerCode){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_PARTNER_CODE, partnerCode);
        editor.commit();
    }

    public static String getPartnerCode(Context context) {
        return getSharedPreferences(context).getString(PREF_PARTNER_CODE, null);
    }
}
