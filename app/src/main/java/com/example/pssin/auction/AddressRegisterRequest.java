package com.example.pssin.auction;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddressRegisterRequest extends StringRequest {
    /*
        설명 : 기업회원이던 일반회원이던 첫 회원가입 후 로그인을 시도할 때 메인화면에서 주소검색받는 걸 체크할 목적
     */
    final static private String URL = "http://pssin1.cafe24.com/addressRegister.php";
    private Map<String, String> parameters;

    public AddressRegisterRequest(String id, String password, String address, String fullAddress, String memberClassification, Double latitude, Double longitude, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        Log.i("건호", "주소를 등록하는 php (IsFirstLoginRequest)");
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("password", password);
        parameters.put("address", address);
        parameters.put("fullAddress", fullAddress);
        parameters.put("memberClassification", memberClassification);
        parameters.put("latitude", String.valueOf(latitude));
        parameters.put("longitude", String.valueOf(longitude));
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}
