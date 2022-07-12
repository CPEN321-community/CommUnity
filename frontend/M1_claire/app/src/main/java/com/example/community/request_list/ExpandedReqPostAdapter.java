package com.example.community.request_list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.community.R;
import com.example.community.classes.ReqPostObj;

import java.util.ArrayList;

public class ExpandedReqPostAdapter extends BaseAdapter {
    private static final String TAG = "EXP_REQ_POST_ADAPTER";
    private Context context;
    private ArrayList<ReqPostObj> expReqPosts;
    private LayoutInflater inflater;

    public ExpandedReqPostAdapter(Context applicationContext, ArrayList<ReqPostObj> expReqPostList) {
        this.context = applicationContext;
        this.expReqPosts = expReqPostList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return this.expReqPosts.size();
    }

    @Override
    public Object getItem(int i) {
        return this.expReqPosts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_expanded_req_post, null);
        TextView itemName = (TextView) view.findViewById(R.id.item_name_exp);
        TextView postDate = (TextView) view.findViewById(R.id.item_post_date_exp);
        TextView description = (TextView) view.findViewById(R.id.item_description_exp);

        ReqPostObj post = this.expReqPosts.get(i);

        itemName.setText(post.itemName);
        // TODO format using JAVA API instead of manually
        Log.d(TAG, "getView: " + post.description);
        String postDateParsed = post.createdAt.toString().split(" ")[1] + " " + post.createdAt.toString().split(" ")[2];
        postDate.setText("Posted: " + postDateParsed);
        description.setText(post.description);
        return view;
    }
}

