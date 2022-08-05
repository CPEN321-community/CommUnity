package com.example.community.ui.chat.message;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.classes.ChatManager;
import com.example.community.classes.ChatRoom;
import com.example.community.databinding.ActivityMessageListBinding;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MESSAGE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String roomId = intent.getStringExtra("roomId");
        ChatRoom thisRoom = ChatManager.getRoomById(roomId);
        ActivityMessageListBinding binding = ActivityMessageListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.msgToolbar.backButton.setOnClickListener(f -> {
            finish();
        });

        binding.chatName.setText(thisRoom.getYou().firstName);
        final RecyclerView messageList = binding.msgs.messageRecyclerView;
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        messageList.setLayoutManager(manager);
        MessageAdapter adapter = new MessageAdapter(this, thisRoom.getMessages());
        messageList.setAdapter(adapter);

        thisRoom.getMessageData().observe(this, messages -> {
            Log.d(TAG, "onCreate: observer" + messages);
            adapter.setMessages(new ArrayList<>(messages));
            adapter.notifyDataSetChanged();
        });

        binding.sendMessageButton.setOnClickListener(v -> {
            String message = binding.messageEditText.getText().toString().trim();
            if (message.length() > 0) {
                ChatManager.SendMessageToRoom(message, thisRoom.getRoomId());
                binding.messageEditText.setText("");
            }
        });

    }
}