package com.example.community.ui.chat;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.R;
import com.example.community.classes.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;
import android.widget.ListView;

import com.example.community.classes.Global;
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
        mChatList.observe(this, chatsList -> {
            ChatAdapter adapter = new ChatAdapter(this, chatsList);
            chatListView.setAdapter(adapter);
        });
        getChats(Global.account.getId());

    }

    private void getChats(String uid) {
        String url = "http://10.0.2.2:3000/chat/" + uid;
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