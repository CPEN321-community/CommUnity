package com.example.community;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.classes.Chat;
import com.example.community.classes.ChatMessageHandler;
import com.example.community.classes.Message;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private EditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.searchField = findViewById(R.id.search_text_input);
        ImageButton searchButton = findViewById(R.id.submit_search_button);
        searchButton.setOnClickListener(v -> {
            this.PerformSearch();
        });
        this.searchField.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                //do what you want on the press of 'done'
                searchField.performClick();
            }
            return false;
        });
    }


    private void PerformSearch() {
//        String url = "http://10.0.2.2:8081/c/" + uid;
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
//                url,
//                null,
//                (JSONArray response) -> {
//                    Log.d(TAG, "getChats: " + response);
//                    if (response.length() == 0) {
////                        TODO tell user they have no chats
//                        return;
//                    }
//                    ArrayList<Chat> currChats = new ArrayList<>();
//                    for (int i = 0; i < response.length(); i++) {
//                        try {
//                            Chat currChat = new Chat(response.getJSONObject(i));
//                            ChatMessageHandler.AddRoom(currChat.postId);
//                            for (Message message : currChat.messages) {
//                                ChatMessageHandler.AddMessage(currChat.postId, message);
//                            }
//                            currChats.add(currChat);
//                        } catch (JSONException e) {
//                            Log.e(TAG, "getChats: " + e);
//                            e.printStackTrace();
//                        }
//                    }
//
//                    mChatList.setValue(currChats);
//                },
//                error -> {
//                    Log.e(TAG, "getChats: " + error);
//                });
//        queue.add(request);
    }

}