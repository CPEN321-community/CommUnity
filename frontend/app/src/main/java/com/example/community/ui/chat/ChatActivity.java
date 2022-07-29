package com.example.community.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.R;
import com.example.community.classes.Chat;
import com.example.community.classes.ChatMessageHandlerUtil;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.Message;
import com.example.community.databinding.ActivityChatBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;


public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "CHAT_ACTIVITY";
    private MutableLiveData<ArrayList<Chat>> mChatList;
    private Socket mSocket;

    @Override
    public void onDestroy() {
        JSONObject userId = new JSONObject();
        try {
            userId.put("userId", GlobalUtil.getId());
            this.mSocket.emit("leave-all", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.mSocket.disconnect();
        ChatMessageHandlerUtil.cleanup();
        Log.d(TAG, "onStop: Disconnected");
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            mSocket = IO.socket(GlobalUtil.CHAT_URL);
            Log.d(TAG, "instance initializer: Socket Initted");
        } catch (URISyntaxException e) {
            Log.e(TAG, "instance initializer: " + e);
            e.printStackTrace();
        }

        ActivityChatBinding binding;

        super.onCreate(savedInstanceState);
        mSocket.connect();
        GlobalUtil.setSocket(mSocket);

        this.mChatList = new MutableLiveData<>();
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitle("Chats");
        ListView chatListView = findViewById(R.id.chat_list);
        ChatAdapter adapter = new ChatAdapter(this);
        ChatMessageHandlerUtil.setChatAdapter(adapter);
        chatListView.setAdapter(adapter);
        Intent intent = getIntent();
        String createRoomId = intent.getStringExtra("createRoomId");
        boolean isOffer = intent.getBooleanExtra("isOffer", true);
        Log.d(TAG, "onCreate: " + createRoomId);
        if (createRoomId == null) {
            Log.d(TAG, "Chat room was not created");
        }
        Chat.createRoom(createRoomId, isOffer);

        mChatList.observe(this, chatsList -> {
            adapter.setChats(chatsList);
        });

        Chat.joinRooms();
        Chat.listenForMessages();
        getChats(GlobalUtil.getId());
        // if (createRoomId != null) {
//            TODO: join room automatically
//            Intent messageIntent = new Intent(this, MessageActivity.class);
//            messageIntent.putExtra("chat", Chat.getChat(createRoomId));
//            startActivity(messageIntent);
        // }

    }

    private void getChats(String uid) {
        String url = GlobalUtil.CHAT_URL + "/chat/" + uid;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "getChats: " + response);
                    if (response.length() == 0) {
//                        TODO tell user they have no chats
                        return;
                    }
                    ArrayList<Chat> currChats = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Chat currChat = new Chat(response.getJSONObject(i));
                            ChatMessageHandlerUtil.AddRoom(currChat.postId);
                            for (Message message : currChat.messages) {
                                ChatMessageHandlerUtil.AddMessage(currChat.postId, message);
                            }
                            currChats.add(currChat);
                        } catch (JSONException e) {
                            Log.e(TAG, "getChats: " + e);
                            e.printStackTrace();
                        }
                    }

                    mChatList.setValue(currChats);
                },
                error -> {
                    Log.e(TAG, "getChats: " + error);
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("token", GlobalUtil.getHeaderToken());
                return headers;
            }
        };
        queue.add(request);
    }
}