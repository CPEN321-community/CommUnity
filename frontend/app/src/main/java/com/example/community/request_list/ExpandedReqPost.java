package com.example.community.request_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.community.R;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.ReqPostObj;
import com.example.community.ui.chat.ChatActivity;

public class ExpandedReqPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_req_post);

        TextView itemName = (TextView) this.findViewById(R.id.item_name_exp);
        TextView description = (TextView) this.findViewById(R.id.item_description_exp);
        TextView postDate = (TextView) this.findViewById(R.id.item_post_date_exp);
        Button acceptButton = this.findViewById(R.id.accept_req_button);
        Intent expReqIntent = getIntent();
        ReqPostObj post = (ReqPostObj) expReqIntent.getSerializableExtra("currReq");

        if (post.userId.equals(GlobalUtil.getAccount().getId())) {
            acceptButton.setVisibility(View.GONE);
        } else {
//            acceptButton.setVisibility(View.GONE);
            acceptButton.setOnClickListener(v -> {
                Intent chatIntent = new Intent(this, ChatActivity.class);
                chatIntent.putExtra("createRoomId", post.reqId);
                chatIntent.putExtra("isOffer", false);
                startActivity(chatIntent);
                finish();
            });
        }

        itemName.setText(post.itemName);
        description.setText(post.description);
        String postDateParsed = post.createdAt.toString().split(" ")[1] + " " + post.createdAt.toString().split(" ")[2];
        postDate.setText("Created: " + postDateParsed);
    }
}