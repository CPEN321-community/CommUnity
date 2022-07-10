package com.example.community.request_list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.community.R;
import com.example.community.classes.OfferPostObj;
import com.example.community.classes.ReqPostObj;

import java.util.ArrayList;

public class ReqPostAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ReqPostObj> reqPosts;
    private LayoutInflater inflater;

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
        view = inflater.inflate(R.layout.fragment_req_post_individual, null);
        TextView itemName = (TextView) view.findViewById(R.id.req_item_name);
        TextView postDate = (TextView) view.findViewById(R.id.req_post_date);
        TextView description = (TextView) view.findViewById(R.id.req_description);

        ReqPostObj post = this.reqPosts.get(i);

        itemName.setText(post.itemName);
        String postDateParsed = post.createdAt.toString().split(" ")[1] + " " + post.createdAt.toString().split(" ")[2];
        postDate.setText("Posted on: " + postDateParsed);
        description.setText(post.description);
        return view;
    }
}