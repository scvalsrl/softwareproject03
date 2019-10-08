/*
    작성자    : 박건호
    용도      : LoginActivity에서 입력한 아이디와 비밀번호가
                DB에 존재하는지 웹서버에 요청하는 화면
    이전 화면 : LoginActivity
 */
package com.example.pssin.auction;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginCompanyMemberRequest extends StringRequest {

    final static private String URL = "http://pssin1.cafe24.com/companyMemberLogin.php";
    private Map<String, String> parameters;

    public LoginCompanyMemberRequest(String id, String password, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        Log.i("건호", "기업 - 로그인 요청이 들어옴************************************");
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("password", password);
        Log.i("건호", "기업 - 다 집어넣어따*******************************************");
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
