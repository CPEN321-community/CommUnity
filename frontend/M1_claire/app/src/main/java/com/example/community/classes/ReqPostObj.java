package com.example.community.classes;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReqPostObj {
    public String reqId;
    public String itemName;
    public String description;
    public Date createdAt;
    private final String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public ReqPostObj(JSONObject json) {
        try {
            this.reqId = json.getString("requestId");
            this.itemName = json.getString("title");
            this.description = json.getString("description");
            String createdAtString = json.getString("createdAt");
            this.createdAt = new SimpleDateFormat(dateFormat).parse(createdAtString);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}
