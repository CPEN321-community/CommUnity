package com.example.community.request_list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.community.R;
import com.example.community.classes.ChatManager;
import com.example.community.classes.ChatRoom;
import com.example.community.classes.CreateRoomInterface;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.ReqPostObj;
import com.example.community.ui.chat.ChatActivity;

public class ExpandedReqPost extends AppCompatActivity {

    private static final String TAG = "EXPANDED_REQ_POST";

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

        if (post.userId.equals(GlobalUtil.getId()) || ChatManager.getChats().getValue().containsKey(post.reqId)) {
            acceptButton.setVisibility(View.GONE);
        } else {
          acceptButton.setOnClickListener(v -> {
                CreateRoomInterface i = new CreateRoomInterface() {
                    @Override
                    public void onSuccess(ChatRoom room) {
                        Intent chatIntent = new Intent(ExpandedReqPost.this, ChatActivity.class);
                        startActivity(chatIntent);
                        finish();
                    }

                    @Override
                    public void onFailure() {
//                        Toast or something
                        Log.d(TAG, "onFailure: ERROR");
                    }
                };
                ChatManager.CreateRoom(post.reqId, false, i);
            });
        }

        itemName.setText(post.itemName);
        description.setText(post.description);
        String postDateParsed = post.createdAt.toString().split(" ")[1] + " " + post.createdAt.toString().split(" ")[2];
        postDate.setText("Created: " + postDateParsed);
    }
}