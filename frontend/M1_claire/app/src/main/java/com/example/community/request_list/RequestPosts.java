package com.example.community.request_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.community.ChatActivity;
import com.example.community.R;
import com.example.community.SearchActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RequestPosts extends AppCompatActivity {
    private ImageButton searchReqPostButton;
    private ImageButton chatReqPostButton;
    private FloatingActionButton addReqPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_posts);

        //Search button on Request Posts page
        searchReqPostButton = findViewById(R.id.searchReqPostButton);
        searchReqPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(RequestPosts.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        //Chat button on Requests Posts page
        chatReqPostButton = findViewById(R.id.chatReqPostButton);
        chatReqPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(RequestPosts.this, ChatActivity.class);
                startActivity(chatIntent);
            }
        });

        //Add button for new request posts
        addReqPostButton = findViewById(R.id.addReqPostButton);
        addReqPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(RequestPosts.this, NewRequestForm.class);
                startActivity(chatIntent);
            }
        });
    }
}