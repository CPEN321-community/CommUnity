package com.example.community.request_list;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.community.R;
import com.example.community.classes.ReqPostObj;

import java.util.ArrayList;

public class ReqPostAdapter extends BaseAdapter {
    private static final String TAG = "REQ_POST_ADAPTER";
    private final Context context;
    private final ArrayList<ReqPostObj> reqPosts;
    private final LayoutInflater inflater;

    public ReqPostAdapter(Context applicationContext, ArrayList<ReqPostObj> reqPostList) {
        this.context = applicationContext;
        this.reqPosts = reqPostList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return this.reqPosts.size();
    }

    @Override
    public Object getItem(int i) {
        return this.reqPosts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView = view;
        newView = inflater.inflate(R.layout.fragment_req_post_individual, null);
        TextView itemName = (TextView) newView.findViewById(R.id.req_item_name);
        TextView postDate = (TextView) newView.findViewById(R.id.req_post_date);
        TextView description = (TextView) newView.findViewById(R.id.req_description);

        ReqPostObj post = this.reqPosts.get(i);
        newView.setOnClickListener(view_name -> {
            Intent expReqsIntent = new Intent(this.context, ExpandedReqPost.class);
            expReqsIntent.putExtra("currReq", post);
            this.context.startActivity(expReqsIntent);
        });

        itemName.setText(post.itemName);
        // TODO format using JAVA API instead of manually
        Log.d(TAG, "getView: " + post.description);
        String postDateParsed = post.createdAt.toString().split(" ")[1] + " " + post.createdAt.toString().split(" ")[2];
        postDate.setText("Posted on: " + postDateParsed);
        description.setText(post.description);
        return newView;
    }
}