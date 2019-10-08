/*
    작성자        : 박건호
    용도          : 기업회원을 위한 회원가입 화면
    이전 레이아웃  : RegisterActivity
 */
package com.example.pssin.auction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterCompanyMemberActivity extends AppCompatActivity {
    /*
            idText                           : 아이디
            passwordText                     : 비밀번호
            phoneNumberTextLeft              : 휴대폰번호 앞 3자리 ( default : 010 )
            phoneNumberTextRight             : 휴대폰번호 뒷 8자리
            companyNameText                  : 회사 이름
            phoneNumberInquiryButton         : 휴대폰 번호 SMS 조회버튼 ( intent to FacebookAccountKitActivity )
            companyNumberInquiryButton       : 사업자등록번호조회 버튼 ( intent to CompanyInquiryActivity )
            registerButton                   : 등록 버튼 ( intent to LoginActivity )
            cancelButton                     : 취소 버튼 ( intent to RegisterActivity )
     */
    EditText idText;
    EditText passwordText;
    Spinner phoneNumberTextLeft;
    EditText phoneNumberTextRight;
    EditText companyNameText;
    Button phoneNumberInquiryButton;
    Button companyNumberInquiryButton;
    Button confirmButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_company_member);

        Log.i("테스트", "기업회원가입 화면 들어옴");
        // 각 변수들과 레이아웃의 id와 매칭
        getObject();

        // 인증 여부에 따른 버튼 활성화 비활성화 조절
        buttonControl();

        // 휴대폰 번호 인증버튼을 눌렀을 때 동작하는 부분
        phoneNumberInquiryButton.setOnClickListener(v -> clickPhoneNumberInquiryButton());

        // 사업자등록번호 조회버튼을 눌렀을 때 동작하는 부분
        companyNumberInquiryButton.setOnClickListener(v -> clickCompanyNumberInquiryBotton());

        // 확인 버튼을 눌렀을 때 동작하는 부분
        confirmButton.setOnClickListener(v -> clickConfirmButton());

        // 취소 버튼을 눌렀을 때 동작하는 부분
        cancelButton.setOnClickListener(v -> clickCancelButton());
    }

    private void getObject() {
        idText                      = findViewById(R.id.idText);
        passwordText                = findViewById(R.id.passwordText);


        // 휴대폰 번호 처리 시작
        // 1. 휴대폰번호 앞 3자리 처리부분
        ArrayList<String> phoneNumberTextLeftList = new ArrayList<>();
        ArrayAdapter<String> phoneNumberTextLeftAdapter;
        phoneNumberTextLeftList.add("010");
        phoneNumberTextLeftList.add("011");
        phoneNumberTextLeftList.add("016");
        phoneNumberTextLeftList.add("017");
        phoneNumberTextLeftList.add("018");
        phoneNumberTextLeftList.add("019");

        phoneNumberTextLeft  = findViewById(R.id.phoneNumberLeft);
        phoneNumberTextLeftAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                phoneNumberTextLeftList);
        phoneNumberTextLeft.setAdapter(phoneNumberTextLeftAdapter);
        phoneNumberTextLeft.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {}
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // 2. 휴대폰번호 뒤 8자리 처리부분
        phoneNumberTextRight        = findViewById(R.id.phoneNumberRight);
        // 휴대폰 번호 처리 끝


        phoneNumberInquiryButton    = findViewById(R.id.phoneNumberInquiryButton);
        companyNameText             = findViewById(R.id.companyNameText);
        companyNumberInquiryButton  = findViewById(R.id.companyNumberInquiryButton);
        confirmButton               = findViewById(R.id.confirmButton);
        cancelButton                = findViewById(R.id.cancelButton);
    }

    private void buttonControl() {
        /*
            forInquiry      : CompanyInquiryActibity에서 사업자등록번호 조회가 성공했는지에 대한 변수를 받기 위함
            inquirySuccess  : true : 사업자등록번호 조회성공, false : 사업자등록번호 조회실패
            setUsableButton : inquirySuccess 변수에 따라 확인 버튼을 활성화 or 비활성화시킴
         */
        idText.setText(getIntent().getStringExtra("idText"));
        passwordText.setText(getIntent().getStringExtra("passwordText"));
        phoneNumberTextLeft.setSelection(getIntent().getIntExtra("phoneNumberTextLeftPosition", 0));
        phoneNumberTextRight.setText(getIntent().getStringExtra("phoneNumberTextRight"));
        companyNameText.setText(getIntent().getStringExtra("companyNameText"));
        boolean phoneNumberInquirySuccess = getIntent().getBooleanExtra("phoneNumberInquirySuccess", false);
        boolean companyNumberInquirySuccess = getIntent().getBooleanExtra("companyNumberInquirySuccess", false);
        inquirySuccess(phoneNumberInquirySuccess, companyNumberInquirySuccess);
    }

    private void clickPhoneNumberInquiryButton() {
        String phoneNumberLeft = phoneNumberTextLeft.getSelectedItem().toString();
        String phoneNumberRight = phoneNumberTextRight.getText().toString();
        if(phoneNumberRight.length() != 8) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterCompanyMemberActivity.this);
            builder.setMessage(phoneNumberLeft + "포함 11자리를 입력해주세요.")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
        }
        else {
            Intent intent = new Intent(RegisterCompanyMemberActivity.this, FacebookAccountKitActivity.class);
            intent.putExtra("idText", idText.getText().toString());
            intent.putExtra("passwordText", passwordText.getText().toString());
            intent.putExtra("phoneNumberTextLeftPosition", phoneNumberTextLeft.getSelectedItemPosition());
            intent.putExtra("phoneNumberTextLeft", phoneNumberLeft);
            intent.putExtra("phoneNumberTextRight", phoneNumberRight);
            intent.putExtra("companyNameText", companyNameText.getText().toString());
            intent.putExtra("companyNumberInquirySuccess", getIntent().getBooleanExtra("companyNumberInquirySuccess", false));
            intent.putExtra("memberClassification", "company");
            RegisterCompanyMemberActivity.this.startActivity(intent);
        }
    }

    private void clickCompanyNumberInquiryBotton() {
        Intent intent = new Intent(RegisterCompanyMemberActivity.this, CompanyInquiryActivity.class);
        intent.putExtra("idText", idText.getText().toString());
        intent.putExtra("passwordText", passwordText.getText().toString());
        intent.putExtra("phoneNumberTextLeftPosition", phoneNumberTextLeft.getSelectedItemPosition());
        intent.putExtra("phoneNumberTextLeft", phoneNumberTextLeft.getSelectedItem().toString());
        intent.putExtra("phoneNumberTextRight", phoneNumberTextRight.getText().toString());
        intent.putExtra("companyNameText", companyNameText.getText().toString());
        intent.putExtra("phoneNumberInquirySuccess", getIntent().getBooleanExtra("phoneNumberInquirySuccess", false));
        RegisterCompanyMemberActivity.this.startActivity(intent);
    }

    private void clickConfirmButton() {
        String id           = idText.getText().toString();
        String password     = passwordText.getText().toString();
        String phoneNumber  = phoneNumberTextLeft.getSelectedItem().toString() + phoneNumberTextRight.getText().toString();
        String companyName  = companyNameText.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i(this.getClass().getName() + "건호 : ", "기업회원가입 시도, " + response);

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.optBoolean("success", false);
                    if (success) {
                        Log.i(this.getClass().getName(), "성공!!");

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterCompanyMemberActivity.this);
                        builder.setMessage("회원 등록에 성공했습니다.")
                                .setPositiveButton("확인", (dialog, which) -> {
                                    Intent intent = new Intent(RegisterCompanyMemberActivity.this, LoginActivity.class);
                                    RegisterCompanyMemberActivity.this.startActivity(intent);
                                })
                                .create()
                                .show();
                    } else {
                        Log.i(this.getClass().getName(), "실패 ㅜㅜ");

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterCompanyMemberActivity.this);
                        builder.setMessage("회원 등록에 실패했습니다.")
                                .setNegativeButton("다시 시도", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    Log.e(this.getClass().getName(), "에러");
                    e.printStackTrace();
                }
            }
        };

        RegisterCompanyMemberRequest registerRequestCompanyMember = new RegisterCompanyMemberRequest(id, password, companyName, phoneNumber, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterCompanyMemberActivity.this);
        queue.add(registerRequestCompanyMember);
    }

    private void clickCancelButton() {
        Intent intent = new Intent(RegisterCompanyMemberActivity.this, RegisterActivity.class);
        RegisterCompanyMemberActivity.this.startActivity(intent);
    }

    private void inquirySuccess(boolean phoneNumberInquirySuccess, boolean companyNumberInquirySuccess) {
        String phoneNumberInquiryBefore   = "인증";
        String companyNumberInquiryBefore = "사업자등록번호 조회";
        String inquiryAfter               = "완료";

        cancelButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_after));
        if(phoneNumberInquirySuccess && companyNumberInquirySuccess) {   // 둘 다 인증성공
            // 휴대폰 인증버튼 관련
            setUsableButton(phoneNumberInquiryButton, false);
            phoneNumberInquiryButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_after));
            phoneNumberInquiryButton.setText(inquiryAfter);
            phoneNumberTextRight.setEnabled(false);
            phoneNumberTextLeft.setEnabled(false);

            // 사업자등록번호 인증버튼 관련
            setUsableButton(companyNumberInquiryButton, false);
            companyNumberInquiryButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_after));
            companyNumberInquiryButton.setText(inquiryAfter);

            // 회원등록 버튼 관련
            setUsableButton(confirmButton, true);
            confirmButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_after));
            confirmButton.setText("등록");
        } else if(phoneNumberInquirySuccess) { // SMS 인증만 성공
            // 휴대폰 인증버튼 관련
            setUsableButton(phoneNumberInquiryButton, false);
            phoneNumberInquiryButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_after));
            phoneNumberInquiryButton.setText(inquiryAfter);
            phoneNumberTextRight.setEnabled(false);
            phoneNumberTextLeft.setEnabled(false);
            setUsableButton(companyNumberInquiryButton, true);
            companyNumberInquiryButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_before));
            companyNumberInquiryButton.setText(companyNumberInquiryBefore);

            // 회원등록 버튼 관련
            setUsableButton(confirmButton, false);
            confirmButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_before));
            confirmButton.setText("인증필요");
        } else if(companyNumberInquirySuccess) { // 사업자번호 인증만 성공
            // 휴대폰 인증버튼 관련
            setUsableButton(phoneNumberInquiryButton, true);
            phoneNumberInquiryButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_before));
            phoneNumberInquiryButton.setText(phoneNumberInquiryBefore);
            phoneNumberTextRight.setEnabled(true);
            phoneNumberTextLeft.setEnabled(true);

            // 사업자등록번호 인증버튼 관련
            setUsableButton(companyNumberInquiryButton, false);
            companyNumberInquiryButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_after));
            companyNumberInquiryButton.setText(inquiryAfter);

            // 회원등록 버튼 관련
            setUsableButton(confirmButton, false);
            confirmButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_before));
            confirmButton.setText("인증필요");
        } else { // 둘 다 인증하기 전
            // 휴대폰 인증버튼 관련
            setUsableButton(phoneNumberInquiryButton, true);
            phoneNumberInquiryButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_before));
            phoneNumberInquiryButton.setText(phoneNumberInquiryBefore);
            phoneNumberTextRight.setEnabled(true);
            phoneNumberTextLeft.setEnabled(true);

            // 사업자등록번호 인증버튼 관련
            setUsableButton(companyNumberInquiryButton, true);
            companyNumberInquiryButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_before));
            companyNumberInquiryButton.setText(companyNumberInquiryBefore);

            // 회원등록 버튼 관련
            setUsableButton(confirmButton, false);
            confirmButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_before));
            confirmButton.setText("인증필요");
        }
    }

    // 버튼 활성화, 비활성화 메소드
    private void setUsableButton(Button button, boolean usable) {
        button.setClickable(usable);
        button.setEnabled(usable);
        button.setFocusable(usable);
        button.setFocusableInTouchMode(usable);
    }
}