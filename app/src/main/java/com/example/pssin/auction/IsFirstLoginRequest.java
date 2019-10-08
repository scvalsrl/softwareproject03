/*
    작성자 : 건호
    용도   : 현재 로그인한 유저가 처음 주소검색을 실시하는지 여부를 DB를 통해 체크
 */
package com.example.pssin.auction;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class IsFirstLoginRequest extends StringRequest {

    /*
        설명 : 기업회원이던 일반회원이던 첫 회원가입 후 로그인을 시도할 때 메인화면에서 주소검색받는 걸 체크할 목적
     */
    final static private String URL = "http://pssin1.cafe24.com/isFirstLogin.php";
    private Map<String, String> parameters;

    public IsFirstLoginRequest(String id, String password, String memberClassification, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        Log.i("건호", "첫 로그인인지 여부 확인 (IsFirstLoginRequest)");
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("password", password);
        parameters.put("memberClassification", memberClassification);
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}