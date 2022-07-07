package com.example.community;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.community.classes.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;
import android.widget.ListView;

import com.example.community.classes.UserWithScore;
import com.example.community.databinding.ActivityChatBinding;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "CHAT_ACTIVITY";
    private ActivityChatBinding binding;
    private MutableLiveData<ArrayList<Chat>> mChatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mChatList = new MutableLiveData<>();
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitle("Chats");
        ListView chatListView = findViewById(R.id.chat_list);
        ArrayList<Chat> chats = new ArrayList<>();

        ChatAdapter adapter = new ChatAdapter(this, chats);
        chatListView.setAdapter(adapter);

    }

    private void getChats(String uid) {

        String url = "10.0.2.2:3000/chat/"+uid;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "getChats: " + response);
                },
                error -> {
                    Log.e(TAG, "fetchLeaderboard: " + error);
//                    callback.onError(error);
                });
    }
}