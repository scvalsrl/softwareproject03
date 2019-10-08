/*
    작성자        : 박건호
    용도          : 회원가입 시 문자인증하는 화면 ( Facebook Account Kit 이용 - 무료 )
    이전 레이아웃  : RegisterCompanyMemberActivity | RegisterNormalMemberActivity
 */

package com.example.pssin.auction;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;

public class FacebookAccountKitActivity extends AppCompatActivity {
    public static int APP_REQUEST_CODE = 3301;
    public static String APP_TAG = "AccountKit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProperties();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String responseMessage;
            if (loginResult.getError() != null) {
                responseMessage = loginResult.getError().getErrorType().getMessage();
            } else if (loginResult.wasCancelled()) {
                responseMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    responseMessage = "Success: " + loginResult.getAccessToken().getAccountId();
                } else {
                    responseMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                inquirySuccess();
            }
            log(responseMessage);
        }
    }

    private void setProperties() {
        AccountKit.initialize(getApplicationContext(), () -> { });

        goToLogin();

        if (AccountKit.getCurrentAccessToken() != null) {
            inquirySuccess();
        }
    }

    public void goToLogin() {
        LoginType loginType = LoginType.PHONE;

        Intent intent = new Intent(getApplicationContext(), AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );

        String phoneNumberTextLeft  = getIntent().getStringExtra("phoneNumberTextLeft");
        String phoneNumberTextRight = getIntent().getStringExtra("phoneNumberTextRight");
        PhoneNumber phoneNumber = new PhoneNumber("82", phoneNumberTextLeft + phoneNumberTextRight);
        configurationBuilder.setInitialPhoneNumber(phoneNumber);

        configurationBuilder.setUIManager(new SkinManager(
                        SkinManager.Skin.CONTEMPORARY,
                        getResources().getColor(R.color.colorBackground),
                        R.drawable.facebook_account_kit_bg,
                        SkinManager.Tint.BLACK,
                        0.10
                )
        );

        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    private void inquirySuccess() {
        // get PhoneNumber
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get phone number
                Intent intent;
                String memberClassification = getIntent().getStringExtra("memberClassification");
                PhoneNumber phoneNumber     = account.getPhoneNumber();
                String phoneNumberString    = "";
                String phoneNumberTextLeft  = "";
                String phoneNumberTextRight = "";

                if (phoneNumber != null) {
                    phoneNumberString    = phoneNumber.toString();
                    int totalLen = phoneNumberString.length();
                    phoneNumberString = "0" + phoneNumberString.substring(totalLen - 10, totalLen);
                    totalLen = phoneNumberString.length();
                    phoneNumberTextLeft  = phoneNumberString.substring(totalLen - 11, totalLen - 8);
                    phoneNumberTextRight = phoneNumberString.substring(totalLen - 8, totalLen);
                    log("Phone: " + phoneNumberString);

                    if(memberClassification.equals("normal")) {          // 일반회원가입시
                        AccountKit.logOut();
                        intent = new Intent(getApplicationContext(), RegisterNormalMemberActivity.class);
                        intent.putExtra("phoneNumberInquirySuccess", true);
                        intent.putExtra("phoneNumberTextLeftPosition", getIntent().getIntExtra("phoneNumberTextLeftPosition", 0));
                        intent.putExtra("phoneNumberTextLeft", phoneNumberTextLeft);
                        intent.putExtra("phoneNumberTextRight", phoneNumberTextRight);
                        intent.putExtra("idText", getIntent().getStringExtra("idText"));
                        intent.putExtra("passwordText", getIntent().getStringExtra("passwordText"));
                        startActivity(intent);
                        finish();

                    } else if(memberClassification.equals("company")) {  // 기업회원가입시
                        AccountKit.logOut();
                        intent = new Intent(getApplicationContext(), RegisterCompanyMemberActivity.class);
                        intent.putExtra("phoneNumberInquirySuccess", true);
                        intent.putExtra("phoneNumberTextLeftPosition", getIntent().getIntExtra("phoneNumberTextLeftPosition", 0));
                        intent.putExtra("phoneNumberTextLeft", phoneNumberTextLeft);
                        intent.putExtra("phoneNumberTextRight", phoneNumberTextRight);
                        intent.putExtra("idText", getIntent().getStringExtra("idText"));
                        intent.putExtra("passwordText", getIntent().getStringExtra("passwordText"));
                        intent.putExtra("companyNameText", getIntent().getStringExtra("companyNameText"));
                        intent.putExtra("companyNumberInquirySuccess", getIntent().getBooleanExtra("companyNumberInquirySuccess", false));
                        startActivity(intent);
                        finish();
                    }
                }
            }
            @Override
            public void onError(final AccountKitError error) {
                log("Error: " + error.toString());
            }
        });
    }

    private void log(String msj) {
        Log.println(Log.DEBUG, APP_TAG, msj);
    }


}
