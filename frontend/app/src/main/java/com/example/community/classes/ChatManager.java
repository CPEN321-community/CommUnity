package com.example.community.classes;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatManager {
    private static final String TAG = "CHAT_MANAGER";
    private static Socket socket;
    private static MutableLiveData<HashMap<String, ChatRoom>> chats;

    public static void Connect() {
        try {
            socket = IO.socket(GlobalUtil.CHAT_URL);
            Log.d(TAG, "Socket Connected!");
        } catch (URISyntaxException e) {
            Log.e(TAG, "Failed to connect to socket" + e);
            e.printStackTrace();
        }
    }

    public static Socket GetSocket() {
        if (socket == null) {
            Connect();
        }
        return socket;
    }

    public static void JoinRooms() {
        Socket s = GetSocket();
        Log.d(TAG, "joinRooms: Start");
        JSONObject joinRoomsMessage = new JSONObject();
        try {
            joinRoomsMessage.put("userId", GlobalUtil.getId());
            s.emit("joinRooms", joinRoomsMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void FetchChats(Context ctx) {
        String url = GlobalUtil.CHAT_URL + "/chat/" + GlobalUtil.getId();
        RequestQueue queue = Volley.newRequestQueue(ctx);
        CustomJSONArrayRequest request = new CustomJSONArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "getChats: " + response);
                    if (response.length() == 0) {
                        return;
                    }
                    HashMap<String, ChatRoom> fetchedChats = new HashMap<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            ChatRoom chat = new ChatRoom(response.getJSONObject(i));
                            fetchedChats.put(chat.getRoomId(), chat);
                        } catch (JSONException e) {
                            Log.e(TAG, "getChats: " + e);
                            e.printStackTrace();
                        }
                    }
                    chats.setValue(fetchedChats);
                },
                error -> {
                    Log.e(TAG, "getChats: " + error);
                });
        queue.add(request);
    }

    public static void SendMessageToRoom(Message message, String roomId) {

    }

}
