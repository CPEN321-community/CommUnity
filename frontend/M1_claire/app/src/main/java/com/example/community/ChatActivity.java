package com.example.community;

import android.os.Bundle;

import com.example.community.classes.Chat;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ListView;

import com.example.community.databinding.ActivityChatBinding;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitle("Chats");
        ListView chatListView = findViewById(R.id.chat_list);
        ArrayList<Chat> chats = new ArrayList<>();
        chats.add(new Chat("tester"));
        chats.add(new Chat("tester2"));
        chats.add(new Chat("tester3"));
        chats.add(new Chat("tester4"));
        chats.add(new Chat("tester5"));
        chats.add(new Chat("tester6"));
        chats.add(new Chat("tester7"));
        chats.add(new Chat("tester8"));
        chats.add(new Chat("tester9"));
        chats.add(new Chat("tester10"));
        chats.add(new Chat("tester11"));
        chats.add(new Chat("tester12"));
        chats.add(new Chat("tester13"));
        chats.add(new Chat("tester14"));
        chats.add(new Chat("tester15"));
        chats.add(new Chat("tester16"));
        ChatAdapter adapter = new ChatAdapter(this, chats);
        chatListView.setAdapter(adapter);

    }
}