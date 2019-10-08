/*
    작성자    : 박건호
    용도      : "RegisterCompanyMemberActivity" or "RegisterNormalActivity"에서 입력한 회원정보를
                DB에 등록하기 위해 웹서버에 요청하는 화면
    이전 화면 : "RegisterCompanyMemberActivity" or "RegisterNormalActivity"
 */
package com.example.pssin.auction;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterNormalMemberRequest extends StringRequest {

    final static private String URL = "http://pssin1.cafe24.com/normalMemberRegister.php";
    private Map<String, String> parameters;

    public RegisterNormalMemberRequest(String id, String password, String phoneNumber, Response .Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        Log.i(this.getClass().getName() + "건호 : ", "DB에 일반회원을 등록요청");
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("password", password);
        parameters.put("phoneNumber", phoneNumber);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
