package com.example.community.request_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.community.R;
import com.example.community.classes.OfferPostObj;
import com.example.community.classes.ReqPostObj;
import com.example.community.classes.Utils;

import java.util.Objects;

public class ExpandedReqPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_req_post);

        TextView itemName = (TextView) this.findViewById(R.id.item_name_exp);
        TextView description = (TextView) this.findViewById(R.id.item_description_exp);
        TextView postDate = (TextView) this.findViewById(R.id.item_post_date_exp);

        Intent expReqIntent = getIntent();
        ReqPostObj post = (ReqPostObj) expReqIntent.getSerializableExtra("currReq");

        itemName.setText(post.itemName);
        description.setText(post.description);
        String postDateParsed = post.createdAt.toString().split(" ")[1] + " " + post.createdAt.toString().split(" ")[2];
        postDate.setText("Created: " + postDateParsed);
    }
}