package com.example.community.classes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatRoom {
    private static final String TAG = "CHAT_ROOM";
    private ChatUser me;
    private ChatUser you;
    private String roomId;
    private ArrayList<Message> messages;

    public ChatRoom(ChatUser me, ChatUser you, String roomId) {
        this.me = me;
        this.you = you;
        this.roomId = roomId;
        this.messages = new ArrayList<>();
    }

    public ChatRoom(JSONObject chatJson) {
        this.messages = new ArrayList<>();
        try {
            JSONArray messageJSONArray = chatJson.getJSONArray("messages");
            this.roomId = chatJson.getString("postId");
            for (int i = 0; i < messageJSONArray.length(); i++) {
                this.messages.add(new Message(messageJSONArray.getJSONObject(i)));
            }
            Log.d(TAG, "Chat: " + this.messages);
            this.me = new ChatUser(chatJson.getString("senderFirstName"),
                    chatJson.getString("senderLastName"),
                    chatJson.getString("senderProfilePicture"));
            this.you = new ChatUser(chatJson.getString("receiverFirstName"),
                    chatJson.getString("receiverLastName"),
                    chatJson.getString("receiverProfilePicture")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMessages(ArrayList<Message> newMessages) {
        this.messages.addAll(newMessages);
    }

    public String getRoomId() {
        return this.roomId;
    }
}