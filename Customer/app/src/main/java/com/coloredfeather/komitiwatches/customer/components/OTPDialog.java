package com.coloredfeather.komitiwatches.customer.components;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.coloredfeather.komitiwatches.customer.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by anandparmar on 09/02/17.
 */

public class OTPDialog extends DialogFragment{
    private static String EXTRA_SERVICE_NAME = "SERVICE_NAME";
    private static String EXTRA_INTENT_NAME = "INTENT_NAME";
    private static String EXTRA_CLASS_OBJECT = "CLASS_OBJECT";
    private static String EXTRA_MESSAGE = "MESSAGE";
    private static String EXTRA_OTP_LENGTH = "OTP_LENGTH";

    private EditText otpEdittext;
    private TextView invalidOTPErrorTextView;
    private View.OnClickListener onclickListener;
    private int retryCount = 1;
    private int otpLength = -1;


    public OTPDialog() {
    }

    public static OTPDialog newInstance(String serviceName, String intentName, Object object) {

        Bundle args = new Bundle();
        args.putString(EXTRA_SERVICE_NAME, serviceName);
        args.putString(EXTRA_INTENT_NAME, intentName);
        if (object instanceof Parcelable) {
            args.putParcelable(EXTRA_CLASS_OBJECT, (Parcelable) object);
        }

        OTPDialog fragment = new OTPDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static OTPDialog newInstance() {
        OTPDialog fragment = new OTPDialog();
        return fragment;
    }

    public static OTPDialog newInstance(String message) {
        Bundle args = new Bundle();
        args.putString(EXTRA_MESSAGE, message);
        OTPDialog fragment = new OTPDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static OTPDialog newInstance(String message, int otpLength) {
        Bundle args = new Bundle();
        args.putString(EXTRA_MESSAGE, message);
        args.putInt(EXTRA_OTP_LENGTH, otpLength);
        OTPDialog fragment = new OTPDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private int elapsedTime = 30;
    private Timer timer;

    private TextView timerTextView;
    private Button resendOtpButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        final View rootView = inflater.inflate(R.layout.otp_dialog, container, false);
        TextView titleTextView = (TextView) rootView.findViewById(R.id.dialog_title);
        otpEdittext = (EditText) rootView.findViewById(R.id.otp_edit_text);
        invalidOTPErrorTextView = (TextView) rootView.findViewById(R.id.invalid_error_otp_dialog);
        timerTextView = (TextView) rootView.findViewById(R.id.timer);
        resendOtpButton = (Button) rootView.findViewById(R.id.resend_otp);
        Button cancelButton = (Button) rootView.findViewById(R.id.cancel);
        Button submitButton = (Button) rootView.findViewById(R.id.submit_otp);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        submitButton.setOnClickListener(onclickListener);
        resendOtpButton.setOnClickListener(onclickListener);
        Bundle arguments = getArguments();
        if (arguments.containsKey(EXTRA_MESSAGE)) {
            String title = arguments.getString(EXTRA_MESSAGE);
            titleTextView.setText(title);
        }

        if (arguments.containsKey(EXTRA_OTP_LENGTH)) {
            otpLength = arguments.getInt(EXTRA_OTP_LENGTH, -1);
        }
        startTimer();
        return rootView;
    }


    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                elapsedTime--; //increase every sec
                mHandler.sendEmptyMessage(elapsedTime);
            }
        }, 0, 1000);
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (elapsedTime > 0) {
                timerTextView.setText(msg.what > 9 ? "00:" + msg.what : "00:0" + msg.what);
            } else {
                elapsedTime = 0;
                timer.cancel();
                timerTextView.setVisibility(View.GONE);
                resendOtpButton.setVisibility(View.VISIBLE);
            }
        }
    };

    public void setOnclickListener(View.OnClickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    public String getEnteredOTP() {
        return otpEdittext.getText().toString();
    }

    public void increaseElapsedTime() {
        retryCount++;
        elapsedTime = retryCount * 30;
    }

    public boolean canRetry() {
        return elapsedTime <= 90 ? true : false;
    }

    public boolean validate() {
        invalidOTPErrorTextView.setVisibility(View.INVISIBLE);
        if (otpEdittext.getText().toString().length() == otpLength) {
            return true;
        } else {
            invalidOTPErrorTextView.setVisibility(View.VISIBLE);
            return false;
        }
    }

    public void setOTPLength(int length) {
        this.otpLength = length;
    }

    public void reStartTimer() {
        resendOtpButton.setVisibility(View.GONE);
        timerTextView.setVisibility(View.VISIBLE);
        startTimer();
    }
}
