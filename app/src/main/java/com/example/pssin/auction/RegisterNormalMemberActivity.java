package com.example.pssin.auction;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterNormalMemberActivity extends AppCompatActivity {
    /*
        idText               : 아이디 입력칸
        passwordText         : 비밀번호 입력칸
        phoneNumberTextLeft  : 휴대폰번호 앞 3자리 ( default : 010 )
        phoneNumberTextRight : 휴대폰번호 뒷 8자리
        inquiryButton        : Facebook Account Kit을 통한 휴대폰 SMS 인증버튼
        confirmButton        : 회원등록 버튼
        cancelButton         : 취소 버튼 ( 이전화면으로 인텐트 )
     */
    EditText idText;
    EditText passwordText;
    Spinner phoneNumberTextLeft;
    EditText phoneNumberTextRight;
    Button inquiryButton;
    Button confirmButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_normal_member);

        // 각 변수들을 레이아웃의 id와 매칭
        getObjects();

        // 인증버튼 누르기 전 입력값들 유지
        getTexts();

        // SMS 인증버튼과 회원등록 버튼을 제어
        buttonControl();

        // SMS 인증버튼을 눌렀을 떄 동작하는 부분
        inquiryButton.setOnClickListener(v -> clickInquiryButton());

        // 회원버튼을 눌렀을 때 동작하는 부분
        confirmButton.setOnClickListener(v -> clickConfirmButton());

        // 취소버튼을 눌렀을 때 동작하는 부분
        cancelButton.setOnClickListener(v -> clickCancelButton());
    }

    private void getObjects() {
        idText               = findViewById(R.id.idText);
        passwordText         = findViewById(R.id.passwordText);

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
        phoneNumberTextRight = findViewById(R.id.phoneNumberRight);
        // 휴대폰 번호 처리 끝

        inquiryButton        = findViewById(R.id.phoneNumberInquiryButton);
        confirmButton        = findViewById(R.id.confirmButton);
        cancelButton         = findViewById(R.id.cancelButton);
    }

    private void getTexts() {
        Intent intent = getIntent();
        idText.setText(intent.getStringExtra("idText"));
        passwordText.setText(intent.getStringExtra("passwordText"));
    }

    private void clickInquiryButton() {
        String phoneNumberLeft = phoneNumberTextLeft.getSelectedItem().toString();
        String phoneNumberRight = phoneNumberTextRight.getText().toString();
        if(phoneNumberRight.length() != 8) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterNormalMemberActivity.this);
            builder.setMessage(phoneNumberLeft + "포함 11자리를 입력해주세요.")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
        } else {
            Intent intent = new Intent(RegisterNormalMemberActivity.this, FacebookAccountKitActivity.class);
            intent.putExtra("memberClassification", "normal");
            intent.putExtra("idText", idText.getText().toString());
            intent.putExtra("passwordText", passwordText.getText().toString());
            intent.putExtra("phoneNumberTextLeftPosition", phoneNumberTextLeft.getSelectedItemPosition());
            intent.putExtra("phoneNumberTextLeft", phoneNumberLeft);
            intent.putExtra("phoneNumberTextRight", phoneNumberRight);
            RegisterNormalMemberActivity.this.startActivity(intent);
        }
    }

    private void clickConfirmButton() {
        String id           = idText.getText().toString();
        String password     = passwordText.getText().toString();
        String phoneNumber  = phoneNumberTextLeft.getSelectedItem().toString() + phoneNumberTextRight.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i(this.getClass().getName() + "건호", "회원가입 시도" + response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.optBoolean("success", false);

                    if (success) {
                        Log.i(this.getClass().getName() + "건호", "회원가입 성공");

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterNormalMemberActivity.this);
                        builder.setMessage("회원 등록에 성공했습니다.")
                                .setPositiveButton("확인", (dialog, which) -> {
                                    Intent Intent = new Intent(RegisterNormalMemberActivity.this, LoginActivity.class);
                                    RegisterNormalMemberActivity.this.startActivity(Intent);
                                })
                                .create()
                                .show();
                    } else {
                        Log.i(this.getClass().getName() + "건호", "회원가입 실패");

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterNormalMemberActivity.this);
                        builder.setMessage("회원 등록에 실패했습니다.")
                                .setNegativeButton("다시 시도", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    Log.e(this.getClass().getName() + "건호", "에러");
                    e.printStackTrace();
                }
            }
        };
        RegisterNormalMemberRequest registerRequestNormalMember = new RegisterNormalMemberRequest(id, password, phoneNumber, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterNormalMemberActivity.this);
        queue.add(registerRequestNormalMember);
    }

    private void clickCancelButton() {
        Intent intent = new Intent(RegisterNormalMemberActivity.this, RegisterActivity.class);
        RegisterNormalMemberActivity.this.startActivity(intent);
    }

    // SMS 인증버튼과 회원등록 버튼을 활성화, 비활성화
    private void buttonControl() {
        boolean success = getIntent().getBooleanExtra("phoneNumberInquirySuccess", false);

        Intent intent = getIntent();
        idText.setText(intent.getStringExtra("idText"));
        passwordText.setText(intent.getStringExtra("passwordText"));
        phoneNumberTextLeft.setSelection(getIntent().getIntExtra("phoneNumberTextLeftPosition", 0));
        phoneNumberTextRight.setText(getIntent().getStringExtra("phoneNumberTextRight"));

        inquirySuccess(success);
    }

    // Facebook Account Kit
    private void inquirySuccess(boolean success) {
        /*
            1. SMS 인증이 완료되었다면
            1-1. SMS 인증버튼 텍스트를 "SMS 인증 성공"로 변경
            1-2. SMS 인증버튼의 색깔을 파란색으로 변경
            1-3. SMS 인증버튼 비활성화
            1-4. 휴대폰번호 입력칸 비활성화
            1-5. 회원등록 버튼 활성화

            2. SMS 인증이 완료되지 않았다면
            2-1. SMS 인증버튼 텍스트를 "SMS 인증하기"로 변경
            2-2. SMS 인증버튼의 색깔을 빨간색으로 변경
            2-3. SMS 인증버튼 활성화
            2-4. 휴대폰번호 입력칸 활성화
            2-5. 회원등록 버튼 비활성화
         */
        String inquiryBefore = "인증";
        String inquiryAfter  = "완료";

        if(success) {   // SMS인증이 완료되었다면
            inquiryButton.setText(inquiryAfter);
            inquiryButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_after));
            setUsableButton(inquiryButton, false);
            setUsableButton(confirmButton, true);
            confirmButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_after));
            phoneNumberTextRight.setEnabled(false);
            phoneNumberTextLeft.setEnabled(false);
        }
        else {          // SMS인증이 완료되지 않았다면
            inquiryButton.setText(inquiryBefore);
            inquiryButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_before));
            setUsableButton(inquiryButton, true);
            setUsableButton(confirmButton, false);
            confirmButton.setBackground(ContextCompat.getDrawable(this, R.drawable.inquiry_before));
            phoneNumberTextRight.setEnabled(true);
            phoneNumberTextLeft.setEnabled(true);
        }
        setUsableButton(cancelButton, true);
    }

    // 버튼 활성화, 비활성화 메소드
    private void setUsableButton(Button button, boolean usable) {
        button.setClickable(usable);
        button.setEnabled(usable);
        button.setFocusable(usable);
        button.setFocusableInTouchMode(usable);
    }
}

