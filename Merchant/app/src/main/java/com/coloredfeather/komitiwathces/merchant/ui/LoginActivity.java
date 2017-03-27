package com.coloredfeather.komitiwathces.merchant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.coloredfeather.komitiwathces.merchant.R;
import com.coloredfeather.komitiwathces.merchant.Utils.AppUtils;
import com.coloredfeather.komitiwathces.merchant.Utils.Preferences;
import com.coloredfeather.komitiwathces.merchant.components.OTPDialog;
import com.coloredfeather.komitiwathces.merchant.network.APIManager;
import com.coloredfeather.komitiwathces.merchant.network.Utils.APIConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anandparmar on 19/03/17.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.partner_code_login)
    EditText partnerCode;
    @BindView(R.id.partner_code_error_login)
    TextView partnerCodeError;
    @BindView(R.id.mobile_number_login)
    EditText mobileNumber;
    @BindView(R.id.mobile_number_error_login)
    TextView mobileNumberError;

    private OTPDialog otpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Preferences.getPartnerCode(this) != null){
            openMainActivity();
        }
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.entered_button_login)
    public void enteredButtonOnClick(){
        partnerCodeError.setVisibility(View.GONE);
        mobileNumberError.setVisibility(View.GONE);
        if(!"".equals(partnerCode.getText().toString()) && partnerCode.getText().toString().length() == 8){
            if(!("").equals(mobileNumber.getText().toString())
                    && (mobileNumber.getText().toString().startsWith("9")
                    || mobileNumber.getText().toString().startsWith("8")
                    || mobileNumber.getText().toString().startsWith("7"))
                    && mobileNumber.getText().toString().length() == 10){
                showOTPDialog();
            } else {
                mobileNumberError.setVisibility(View.VISIBLE);
            }
        } else {
            partnerCodeError.setVisibility(View.VISIBLE);
        }
    }

    private void showOTPDialog() {
        otpDialog = OTPDialog.newInstance("Enter OTP", 6);
        otpDialog.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.submit_otp) {
                    if (otpDialog.validate() && otpDialog.getEnteredOTP().equals("123456")) {
                        otpDialog.dismiss();
                        setPreferences();
                        openMainActivity();
                    } else {
                        AppUtils.showMessage(getApplicationContext(), "entered OTP is wrong", APIConstants.RESULT_FAIL);
                    }
                } else if(view.getId() == R.id.resend_otp) {
                    AppUtils.showMessage(getApplicationContext(), "resent otp", null);
                    otpDialog.dismiss();
                }
            }
        });
        otpDialog.show(getSupportFragmentManager(), "otp");
    }

    private void setPreferences(){
        Preferences.setMobileNumber(this, mobileNumber.getText().toString());
        Preferences.setPartnerCode(this, partnerCode.getText().toString());
    }

    private void openMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
