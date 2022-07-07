package com.example.community.classes;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    public Chat parentChat;
    public String postId;
    public String messageText;
    public String userId;
    public Date createdAt;

    public Message(JSONObject messageJSON, String postId, Chat parentChat) {
        try {
            this.messageText = messageJSON.getString("message");
            this.userId = messageJSON.getString("userId");
            this.postId = postId;
            this.parentChat = parentChat;
            String createdAtString = messageJSON.getString("createdAt");
            String dateFormat = "yyyy-mm-dd hh:mm:ss";
            this.createdAt = new SimpleDateFormat(dateFormat).parse(createdAtString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(JSONObject messageJSON) {
        try {
            this.messageText = messageJSON.getString("message");
            this.userId = messageJSON.getString("userId");
            this.postId = messageJSON.getString("postId");
            this.parentChat = Chat.getChat(this.postId);
            String createdAtString = messageJSON.getString("createdAt");
            String dateFormat = "yyyy-mm-dd hh:mm:ss";
            this.createdAt = new SimpleDateFormat(dateFormat).parse(createdAtString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
