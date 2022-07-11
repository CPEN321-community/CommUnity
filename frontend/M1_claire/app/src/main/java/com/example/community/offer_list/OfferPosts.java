package com.example.community.offer_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.community.R;
import com.example.community.SearchActivity;
import com.example.community.request_list.NewRequestForm;
import com.example.community.ui.chat.ChatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OfferPosts extends AppCompatActivity {
    private ImageButton searchOfferPostButton;
    private FloatingActionButton addOfferPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_posts);

        // Search button on Request Posts page
        searchOfferPostButton = findViewById(R.id.searchOfferPostButton);
        searchOfferPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(OfferPosts.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        // Add button for new request posts
        addOfferPostButton = findViewById(R.id.addOfferPostButton);
        addOfferPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(OfferPosts.this, NewOfferForm.class);
                startActivity(chatIntent);
            }
        });
    }
}