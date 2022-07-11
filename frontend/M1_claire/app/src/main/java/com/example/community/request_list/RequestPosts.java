package com.example.community.request_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.community.R;
import com.example.community.SearchActivity;
import com.example.community.offer_list.OfferHomeFragment;
import com.example.community.offer_list.OfferPosts;
import com.example.community.ui.chat.ChatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RequestPosts extends AppCompatActivity {
    private ImageButton searchReqPostButton;
    private ImageButton chatReqPostButton;
    private FloatingActionButton addReqPostButton;
    private Button viewOffersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_request_posts);
        setContentView(R.layout.fragment_home);

        // Search button on Request Posts page
        searchReqPostButton = findViewById(R.id.search_button);
        searchReqPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(RequestPosts.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        //Button to move to list of offers
        viewOffersButton = findViewById(R.id.viewOffersButton);
        viewOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);

                // Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.fragment_req_post, OfferHomeFragment.class, null);

                // Commit the transaction
                transaction.commit();
                //Intent viewOffersIntent = new Intent(RequestPosts.this, OfferHomeFragment.class);
                //startActivity(viewOffersIntent);
            }
        });

        // Add button for new request posts
        addReqPostButton = findViewById(R.id.addReqPostButton);
        addReqPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addReqIntent = new Intent(RequestPosts.this, NewRequestForm.class);
                startActivity(addReqIntent);
            }
        });

        // Chat button on Requests Posts page
//        chatReqPostButton = findViewById(R.id.chatReqPostButton);
//        chatReqPostButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent chatIntent = new Intent(RequestPosts.this, ChatActivity.class);
//                startActivity(chatIntent);
//            }
//        });
    }
}