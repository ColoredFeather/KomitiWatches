package com.coloredfeather.komitiwatches.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.coloredfeather.komitiwatches.customer.R;
import com.coloredfeather.komitiwatches.customer.components.OTPDialog;
import com.coloredfeather.komitiwatches.customer.network.Utils.APIConstants;
import com.coloredfeather.komitiwatches.customer.utils.AppConstance;
import com.coloredfeather.komitiwatches.customer.utils.AppUtils;
import com.coloredfeather.komitiwatches.customer.utils.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.mobile_number_login) EditText mobileNumber;
    @BindView(R.id.mobile_number_error_login) TextView mobileNumberError;

    private OTPDialog otpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Preferences.getMobileNumber(this) != null && Preferences.getPartnerCode(this) != null){
            openMainActivity();
        }

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.new_user_link_login)
    public void newUserOnClick(){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.entered_button_login)
    public void setEnterButton(){
        mobileNumberError.setVisibility(View.GONE);
        if(!("").equals(mobileNumber.getText().toString())
                && (mobileNumber.getText().toString().startsWith("9")
                    || mobileNumber.getText().toString().startsWith("8")
                    || mobileNumber.getText().toString().startsWith("7"))
                && mobileNumber.getText().toString().length() == 10){
            showOTPDialog();
        } else {
            mobileNumberError.setVisibility(View.VISIBLE);
        }
    }

    private void showOTPDialog() {
        otpDialog = OTPDialog.newInstance("Enter OTP", 6);
        otpDialog.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.submit_otp) {
                    if ( otpDialog.validate() && otpDialog.getEnteredOTP().equals("123456")) {
                        otpDialog.dismiss();
                        showProgress(null);
                        Preferences.setMobileNumber(LoginActivity.this, mobileNumber.getText().toString());
                        Preferences.setPartnerCode(LoginActivity.this, "KW010317");
                        openMainActivity();
                    } else {
                        AppUtils.showMessage(getApplicationContext(), "wrong otp please try again", APIConstants.RESULT_FAIL);
                        otpDialog.dismiss();
                    }
                } else if(view.getId() == R.id.resend_otp) {
                    AppUtils.showMessage(getApplicationContext(), "resent otp", null);
                    otpDialog.dismiss();
                }
            }
        });
        otpDialog.show(getSupportFragmentManager(), "otp");
    }

    private void openMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        hideProgress();
        finish();
    }
}
