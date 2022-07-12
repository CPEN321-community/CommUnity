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
import com.example.community.classes.ChatMessageHandler;
import com.example.community.classes.Global;
import com.example.community.classes.Message;
import com.example.community.databinding.ActivityChatBinding;
import com.example.community.ui.chat.message.MessageActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

import io.socket.client.IO;
import io.socket.client.Socket;


public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "CHAT_ACTIVITY";
    private ActivityChatBinding binding;
    private MutableLiveData<ArrayList<Chat>> mChatList;
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(Global.CHAT_URL);
            Log.d(TAG, "instance initializer: Socket Initted");
        } catch (URISyntaxException e) {
            Log.e(TAG, "instance initializer: " + e);
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocket.connect();
        Global.setSocket(mSocket);

        this.mChatList = new MutableLiveData<>();
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitle("Chats");
        ListView chatListView = findViewById(R.id.chat_list);
        ChatAdapter adapter = new ChatAdapter(this);
        ChatMessageHandler.setChatAdapter(adapter);
        chatListView.setAdapter(adapter);
        Intent intent = getIntent();
        String createRoomId = intent.getStringExtra("createRoomId");
        Log.d(TAG, "onCreate: " + createRoomId);
        if (createRoomId != null) {
            Chat.createRoom(createRoomId, true);
        }

        mChatList.observe(this, chatsList -> {
            adapter.setChats(chatsList);
        });

        Chat.joinRooms();
        Chat.listenForMessages();
        getChats(Global.getAccount().getId());
        if (createRoomId != null) {
            Intent messageIntent = new Intent(this, MessageActivity.class);
            messageIntent.putExtra("chat", Chat.getChat(createRoomId));
            startActivity(messageIntent);
        }

    }

    private void getChats(String uid) {
        String url = Global.CHAT_URL + "/chat/" + uid;
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
                            ChatMessageHandler.AddRoom(currChat.postId);
                            for (Message message : currChat.messages) {
                                ChatMessageHandler.AddMessage(currChat.postId, message);
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
                });
        queue.add(request);
    }
}