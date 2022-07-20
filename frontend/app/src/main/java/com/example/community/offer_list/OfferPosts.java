package com.example.community.offer_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.community.R;
import com.example.community.SearchActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OfferPosts extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_posts);

        // Search button on Request Posts page
        ImageButton searchOfferPostButton = findViewById(R.id.searchOfferPostButton);
        searchOfferPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(OfferPosts.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        // Add button for new request posts
        FloatingActionButton addOfferPostButton = findViewById(R.id.addOfferPostButton);
        addOfferPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(OfferPosts.this, NewOfferForm.class);
                startActivity(chatIntent);
            }
        });
    }
}