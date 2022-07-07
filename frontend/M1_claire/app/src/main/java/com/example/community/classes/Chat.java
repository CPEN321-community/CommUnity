package com.example.community.classes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Chat implements Serializable {
    private static final String TAG = "CHAT_CLASS";
    public ChatUser me;
    public ChatUser other;
    public String postId;
    private static final HashMap<String, Chat> chatMap = new HashMap<>();
    public ArrayList<Message> messages;

    public Chat(JSONObject chatJson) {
        this.messages = new ArrayList<>();
        try {
            JSONArray messageJSONArray = chatJson.getJSONArray("messages");
            this.postId = chatJson.getString("postId");
            for (int i = 0; i < messageJSONArray.length(); i++) {
                this.messages.add(new Message(messageJSONArray.getJSONObject(i), this.postId, this));
            }
            Log.d(TAG, "Chat: " + this.messages);
            this.me = new ChatUser(chatJson.getString("senderFirstName"),
                    chatJson.getString("senderLastName"),
                    chatJson.getString("senderProfilePicture"));
            this.other = new ChatUser(chatJson.getString("receiverFirstName"),
                    chatJson.getString("receiverLastName"),
                    chatJson.getString("receiverProfilePicture")
            );
            chatMap.put(this.postId, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Chat getChat(String postId) {
        return Chat.chatMap.getOrDefault(postId, null);
    }

}
