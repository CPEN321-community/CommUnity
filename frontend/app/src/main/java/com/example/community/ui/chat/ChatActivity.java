package com.example.community.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.R;
import com.example.community.classes.Chat;
import com.example.community.classes.ChatMessageHandlerUtil;
import com.example.community.classes.ChatRoom;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.Message;
import com.example.community.databinding.ActivityChatBinding;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "CHAT_ACTIVITY";
    ChatAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityChatBinding binding;

        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarChat.backButton.setOnClickListener(v -> {
            finish();
        });
        ChatAdapter adapter = new ChatAdapter(this);
        ListView chatListView = findViewById(R.id.chat_list);
        chatListView.setAdapter(adapter);

    }

    public ChatAdapter getAdapter() {
        return adapter;
    }
}