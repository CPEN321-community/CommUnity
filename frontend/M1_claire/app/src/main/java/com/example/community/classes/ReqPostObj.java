package com.example.community.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqPostObj {
    public int reqId;
    public String itemName;
    public String description;
    //Change to date type?
    public Object postDate;

    public ReqPostObj(JSONObject json) {
        try {
            this.reqId = json.getInt("requestId");
            this.itemName = json.getString("title");
            this.description = json.getString("description");
            this.postDate = json.get("creationDate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
