package com.example.pssin.auction;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static java.sql.Types.NULL;

public class WriteBoardRequest extends StringRequest {

    final static private String URL = "http://pssin1.cafe24.com/boardRegister.php";
    private Map<String, String> parameters;

    public WriteBoardRequest(String id, String title, String category, String tradeLocation, String auctionTimeLimit, Response .Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("title", title);
        parameters.put("category", category);
        parameters.put("tradeLocation", tradeLocation);
        parameters.put("auctionTimeLimit", auctionTimeLimit);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
