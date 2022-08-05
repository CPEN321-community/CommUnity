package com.example.community.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.community.R;
import com.example.community.classes.ChatManager;
import com.example.community.classes.ChatRoom;
import com.example.community.databinding.ActivityChatBinding;

import java.util.ArrayList;


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
        ChatManager.getChats().observe(this, (chats) -> {
            Log.d(TAG, "onCreate: chats!!!" + chats.size());
            adapter.setItems(new ArrayList<>(chats.values()));
            adapter.notifyDataSetChanged();
            for (ChatRoom c : chats.values()) {
                c.getMessageData().observe(this, v -> {
                    adapter.notifyDataSetChanged();
                });
            }
        });

    }

    public ChatAdapter getAdapter() {
        return adapter;
    }
}