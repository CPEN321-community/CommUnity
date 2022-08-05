package com.example.community.classes;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.community.exceptions.NoMessagesException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatRoom {
    private static final String TAG = "CHAT_ROOM";
    private ChatUser me;
    private ChatUser you;
    private String roomId;

    private final MutableLiveData<ArrayList<Message>> messages = new MutableLiveData<>(new ArrayList<>());

    public ChatRoom(ChatUser me, ChatUser you, String roomId) {
        this.me = me;
        this.you = you;
        this.roomId = roomId;
    }

    public ChatRoom(JSONObject chatJson) {
        try {
            JSONArray messageJSONArray = chatJson.getJSONArray("messages");
            this.roomId = chatJson.getString("postId");
            ArrayList<Message> msgs = new ArrayList<>();
            for (int i = 0; i < messageJSONArray.length(); i++) {
                msgs.add(new Message(messageJSONArray.getJSONObject(i)));
            }
            this.messages.postValue(msgs);
            this.me = new ChatUser(chatJson.getString("senderFirstName"),
                    chatJson.getString("senderLastName"),
                    chatJson.getString("senderProfilePicture"));
            this.you = new ChatUser(chatJson.getString("receiverFirstName"),
                    chatJson.getString("receiverLastName"),
                    chatJson.getString("receiverProfilePicture")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ChatUser getYou() {
        return this.you;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public ChatUser getMe() {
        return me;
    }

    public void setMe(ChatUser me) {
        this.me = me;
    }

    public MutableLiveData<ArrayList<Message>> getMessageData() {
        return this.messages;
    }

    public ArrayList<Message> getMessages() {
        return this.messages.getValue();
    }

    public void AddMessage(Message m) {
        ArrayList<Message> curr = this.messages.getValue();
        curr.add(m);
        this.messages.postValue(curr);
    }

    public Message getLastMessage() throws NoMessagesException {
        if (this.messages.getValue() == null) {
            Log.e(TAG, "getLastMessage: No Messages Available");
            throw new NoMessagesException();
        }
        int size = this.messages.getValue().size();
        if (size == 0) throw new NoMessagesException();
        return this.messages.getValue().get(size - 1);
    }
}