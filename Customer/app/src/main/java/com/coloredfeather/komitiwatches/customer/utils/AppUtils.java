package com.coloredfeather.komitiwatches.customer.utils;

import android.app.Application;
import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.coloredfeather.komitiwatches.customer.R;
import com.coloredfeather.komitiwatches.customer.network.Utils.APIConstants;

/**
 * Created by anandparmar on 09/02/17.
 */

public class AppUtils {
    public static void showMessage(Context context, String message, String status) {
        showMessage(context, message, Toast.LENGTH_SHORT, status);
    }

    public static void showLongMessage(Context context, String message, String status) {
        showMessage(context, message, Toast.LENGTH_LONG, status);
    }

    private static void showMessage(Context context, String message, int toastLength, String status) {

        final Toast toast = new Toast(context.getApplicationContext());

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.toast_message, null);

        TextView text = (TextView) layout.findViewById(R.id.text);
        if (status == null) {
            text.setBackgroundColor(context.getResources().getColor(R.color.colorSecondaryText));
        } else if (status.equals(APIConstants.RESULT_SUCCESS)) {
            text.setBackgroundColor(context.getResources().getColor(R.color.colorSuccess));
        } else if (status.equals(APIConstants.RESULT_FAIL)) {
            text.setBackgroundColor(context.getResources().getColor(R.color.colorFail));
        }
        text.setText(Html.fromHtml(message));

        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(toastLength);
        toast.setView(layout);
        toast.show();
    }
}
