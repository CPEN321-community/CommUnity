package com.example.community.classes;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReqPostObj implements Serializable{
    private static final String TAG = "REQ_POST_CLASS";
    public String reqId;
    public String itemName;
    public String userId;
    public String description;
    public Date createdAt;
    private final String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public ReqPostObj(JSONObject json) {
        try {
            Log.d(TAG, "ReqPostObj: " + json);
            this.reqId = json.getString("requestId");
            this.itemName = json.getString("title");
            this.userId = json.getString("userId");
            this.description = json.getString("description");
            String createdAtString = json.getString("createdAt");
            Log.d(TAG, "ReqPostObj: " + createdAtString);
            this.createdAt = new SimpleDateFormat(dateFormat).parse(createdAtString);
            Log.d(TAG, "ReqPostObj: " + this.createdAt);

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}
