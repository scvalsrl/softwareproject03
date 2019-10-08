package com.example.pssin.auction;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static java.sql.Types.NULL;

public class RegisterRequest extends StringRequest {

    final static private String URL = "http://pssin1.cafe24.com/accountRegister.php";
    private Map<String, String> parameters;

    public RegisterRequest(String accountID, String accountPassword, String accountName, String accountPhone, String accountEmail, String accountClass,
                           String accountCompanyName, String accountCompanyLocation, Response .Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("accountID", accountID);
        parameters.put("accountPassword", accountPassword);
        parameters.put("accountName", accountName);
        parameters.put("accountPhone", accountPhone);
        parameters.put("accountEmail", accountEmail);
        parameters.put("accountClass", accountClass);        // 사장은 "1", 유저는 "0"
        parameters.put("accountCompanyName", accountCompanyName);
        parameters.put("accountCompanyLocation", accountCompanyLocation);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
