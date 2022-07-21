package com.example.community.classes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Chat implements Serializable {
    private static final String TAG = "CHAT_CLASS";
    private static final HashMap<String, Chat> chatMap = new HashMap<>();
    public ChatUser me;
    public ChatUser other;
    public String postId;
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

    public static synchronized void sendMessage(String roomId, String message) {
        JSONObject sendMessage = new JSONObject();
        try {
            sendMessage.put("postId", roomId);
            sendMessage.put("userId", Global.getAccount().getId());
            sendMessage.put("message", message);
            Global.getSocket().emit("sendMessage", sendMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void joinRooms() {
        Log.d(TAG, "joinRooms: Start");
        JSONObject joinRoomsMessage = new JSONObject();
        try {
            joinRoomsMessage.put("userId", Global.getAccount().getId());
            Global.getSocket().emit("joinRooms", joinRoomsMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void listenForMessages() {
        Global.getSocket().on("sendMessage", args -> {
            JSONObject data = (JSONObject) args[0];
            Message message = new Message(data);
            String postId = message.postId;
            Log.d(TAG, "listenForMessages: " + message.postId);
            if (!ChatMessageHandler.getChatMap().containsKey(postId)) return;
            ChatMessageHandler.AddMessage(postId, message);
            Log.d(TAG, "listenForMessages: Recieved Message: " + message.messageText);
        });
    }

    public static synchronized void createRoom(String roomId, boolean isOffer) {
        JSONObject createRoomMessage = new JSONObject();
        JSONObject senderData = new JSONObject();
        try {
            senderData.put("senderId", Global.getAccount().getId());
            senderData.put("senderFirstName", Global.userProfile.firstName);
            senderData.put("senderLastName", Global.userProfile.lastName);
            senderData.put("senderProfilePicture", Global.userProfile.profilePicture);
            createRoomMessage.put("postId", roomId);
            createRoomMessage.put("senderData", senderData);
            createRoomMessage.put("isOffer", isOffer);
        } catch (JSONException e) {
            Log.e(TAG, "createRoom: " + e);
            e.printStackTrace();
        }

        Global.getSocket().emit("createRoom", createRoomMessage);

    }

}
