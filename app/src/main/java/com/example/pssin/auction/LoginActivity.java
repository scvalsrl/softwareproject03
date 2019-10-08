package com.example.pssin.auction;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    /*
     *   idText                   : 사용자가 입력한 ID값
     *   passwordText             : 사용자가 입력한 password값
     *   normalMemberRadioButton  : 사용자의 회원구분 ( 일반회원 )
     *   companyMemberRadioButton : 사용자의 회원구분 ( 기업회원 )
     *   loginButton              : 로그인버튼
     *   registreButton           : 회원가입버튼
     */
    EditText idText;
    EditText passwordText;
    RadioButton normalMemberRadioButton;
    RadioButton companyMemberRadioButton;
    Button loginButton;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 각 변수들을 레이아웃의 id들과 매칭
        getObject();

        // 로그인 버튼을 누르면 MainActivity로 인텐트 수행
        loginButton.setOnClickListener(view -> clickLoginButton());

        // 회원가입 버튼을 누르면 RegisterActivity로 인텐트 수행
        registerButton.setOnClickListener(v -> clickRegisterButton());
    }

    private void getObject() {
        idText                      = findViewById(R.id.idText);
        passwordText                = findViewById(R.id.passwordText);
        normalMemberRadioButton     = findViewById(R.id.normalMemberRadioButton);
        companyMemberRadioButton    = findViewById(R.id.companyMemberRadioButton);
        loginButton                 = findViewById(R.id.loginButton);
        registerButton              = findViewById(R.id.registerButton);
    }

    private void clickLoginButton() {
        Log.i(this.getClass().getName() + "건호", "로그인 버튼을 클릭하였다");

        final String id       = idText.getText().toString();
        final String password = passwordText.getText().toString();

        Response.Listener<String> responseListener = response -> {
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");
                if(success) // 로그인 성공!
                {
                    Log.i(this.getClass().getName() + "건호", "로그인 성공");
                    Intent intent    = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("password", password);
                    if(normalMemberRadioButton.isChecked())
                        intent.putExtra("memberClassification", "normal");
                    else if(companyMemberRadioButton.isChecked())
                        intent.putExtra("memberClassification", "company");
                    LoginActivity.this.startActivity(intent);
                }
                else // 로그인 실패...
                {
                    Log.e(this.getClass().getName() + "건호", "로그인 실패");
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("로그인에 실패했습니다.")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        };

        // 일반회원을 선택하고 로그인하면
        // 일반회원 테이블에서 아이디,패스워드 검사를 수행
        if(normalMemberRadioButton.isChecked())
        {
            Log.i(this.getClass().getName() + "건호", "일반회원 radio 버튼을 누름");
            LoginNormalMemberRequest loginNormalMemberRequest = new LoginNormalMemberRequest(id, password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginNormalMemberRequest);
        }
        // 기업회원을 선택하고 로그인하면
        // 기업회원 테이블에서 아이디,패스워드 검사를 수행
        else if(companyMemberRadioButton.isChecked())
        {
            Log.i(this.getClass().getName() + "건호", "기업회원 radio 버튼을 누름");
            LoginCompanyMemberRequest loginCompanyMemberRequest = new LoginCompanyMemberRequest(id, password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginCompanyMemberRequest);
        }
    }

    private void clickRegisterButton() {
        Log.i(this.getClass().getName() + "건호", "회원가입 버튼을 누름");
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(registerIntent);
    }
}
