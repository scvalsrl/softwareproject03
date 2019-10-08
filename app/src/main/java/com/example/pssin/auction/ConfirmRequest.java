package com.example.pssin.auction;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ConfirmRequest extends StringRequest {

    final static private String URL = "http://pssin1.cafe24.com/authentication.php";
    private Map<String, String> parameters;

    public ConfirmRequest(String email, String authentication ,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("authentication",authentication);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
