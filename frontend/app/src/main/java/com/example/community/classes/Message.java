package com.example.community.classes;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    public String postId;
    public String messageText;
    public String userId;
    public Date createdAt;

    public Message(JSONObject messageJSON, String postId) {
        try {
            this.messageText = messageJSON.getString("message");
            this.userId = messageJSON.getString("userId");
            this.postId = postId;
            String createdAtString = messageJSON.getString("createdAt");
            this.createdAt = new SimpleDateFormat(DateImgUtil.dateFormatString).parse(createdAtString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(JSONObject messageJSON) {
        try {
            this.messageText = messageJSON.getString("message");
            this.userId = messageJSON.getString("userId");
            this.postId = messageJSON.getString("postId");
            String createdAtString = messageJSON.getString("createdAt");
            this.createdAt = new SimpleDateFormat(DateImgUtil.dateFormatString).parse(createdAtString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
