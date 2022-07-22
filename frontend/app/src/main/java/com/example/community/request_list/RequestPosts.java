package com.example.community.request_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.community.R;
import com.example.community.SearchActivity;
import com.example.community.offer_list.OfferHomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RequestPosts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        // Search button on Request Posts page
        ImageButton searchReqPostButton = findViewById(R.id.search_button);
        searchReqPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(RequestPosts.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        //Button to move to list of offers
        Button viewOffersButton = findViewById(R.id.viewOffersButton);
        viewOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewOffersIntent = new Intent(RequestPosts.this, OfferHomeFragment.class);
                startActivity(viewOffersIntent);
            }
        });

        // Add button for new request posts
        FloatingActionButton addReqPostButton = findViewById(R.id.addReqPostButton);
        addReqPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addReqIntent = new Intent(RequestPosts.this, NewRequestForm.class);
                startActivity(addReqIntent);
            }
        });

//        reqName = findViewById(R.id.req_item_name);
//        reqName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("RequestPost", "Trying to expand post");
//                Intent reqExpPostIntent = new Intent(RequestPosts.this, ExpandedReqPost.class);
//                startActivity(reqExpPostIntent);
//            }
//        });
    }
}