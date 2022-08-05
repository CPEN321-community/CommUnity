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
import io.socket.emitter.Emitter;

public class ChatHelper {
    private static final String TAG = "CHAT_MANAGER";
    private static final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private static final MutableLiveData<HashMap<String, ChatRoom>> chats = new MutableLiveData<>(new HashMap<>());
    private static final Emitter.Listener sendMessageListener = args -> {
        JSONObject data = (JSONObject) args[0];
        Message message = new Message(data);
        Log.d(TAG, "Message!: " + message);
        String postId = message.postId;
        ChatRoom chat = chats.getValue().get(postId);
        if (chat != null) {
            chat.AddMessage(message);
//            chats.postValue(chats.getValue());
        }
    };

    private static final Emitter.Listener createRoomListener = args -> {
        JSONObject data = (JSONObject) args[0];
        Log.d(TAG, "Connect: " + data);
        ChatRoom chat = new ChatRoom(data);
        Log.d(TAG, "iConnect: " + chat.getMe().firstName);
        HashMap<String, ChatRoom> newChats = new HashMap<>();
        newChats.put(chat.getRoomId(), chat);
        newChats.putAll(chats.getValue());
        chats.postValue(newChats);
    };

    private static Socket socket;

    public static void Connect() {
        try {
            IO.Options opts = new IO.Options();
            opts.query = "auth_token=" + GlobalUtil.getId();
            socket = IO.socket(GlobalUtil.CHAT_URL, opts);
            socket.on("sendMessage", sendMessageListener);
            socket.on("createRoom", createRoomListener);

            socket.connect();
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
        setLoading(true);
        CustomJSONArrayRequest request = new CustomJSONArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "FetchChats: " + response);
                    if (response.length() == 0) {
                        setLoading(false);
                        return;
                    }
                    HashMap<String, ChatRoom> fetchedChats = new HashMap<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            ChatRoom chat = new ChatRoom(response.getJSONObject(i));
                            fetchedChats.put(chat.getRoomId(), chat);
                        } catch (JSONException e) {
                            Log.e(TAG, "FetchChats: " + e);
                            e.printStackTrace();
                        }
                    }
                    chats.setValue(fetchedChats);
                },
                error -> {
                    setLoading(false);
                    Log.e(TAG, "getChats: " + error);
                });
        queue.add(request);
    }

    public static void SendMessageToRoom(String message, String roomId) {
        Socket socket = GetSocket();
        JSONObject body = new JSONObject();
        try {
            body.put("userId", GlobalUtil.getId());
            body.put("postId", roomId);
            body.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.emit("sendMessage", body);
    }

    public static void CreateRoom(String roomId, boolean isOffer, CreateRoomInterface i) {
        Socket socket = GetSocket();
        JSONObject body = new JSONObject();
        socket.once("createRoom", args -> {
            if (args[0] == null) {
                i.onFailure();
            }
            ChatRoom room = new ChatRoom((JSONObject) args[0]);
            i.onSuccess(room);
        });
        try {
            JSONObject senderData = new JSONObject();
            senderData.put("senderId", GlobalUtil.getId());
            senderData.put("senderFirstName", GlobalUtil.getGivenName());
            senderData.put("senderLastName", GlobalUtil.getLastName());
            senderData.put("senderProfilePicture", GlobalUtil.getAccount().getPhotoUrl());
            body.put("userId", GlobalUtil.getId());
            body.put("postId", roomId);
            body.put("isOffer", isOffer);
            body.put("senderData", senderData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.emit("createRoom", body);
    }

    public static MutableLiveData<HashMap<String, ChatRoom>> getChats() {
        return chats;
    }

    public static boolean isLoading() {
        return Boolean.TRUE.equals(loading.getValue());
    }

    public static void setLoading(boolean l) {
        loading.setValue(l);
    }

    public static MutableLiveData<Boolean> getLoadingData() {
        return loading;
    }

    public static ChatRoom getRoomById(String roomId) {
        return chats.getValue().get(roomId);
    }

    public static void SetChats(HashMap<String, ChatRoom> mChats) {
        chats.postValue(mChats);
    }
}

