package com.example.community.request_list;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.R;
import com.example.community.classes.ReqPostObj;

import java.util.ArrayList;

public class ReqPostAdapter extends RecyclerView.Adapter<ReqPostAdapter.ViewHolder> {
    private static final String TAG = "REQ_POST_ADAPTER";
    private final Context context;
    private final ArrayList<ReqPostObj> reqPosts;

    public ReqPostAdapter(Context applicationContext, ArrayList<ReqPostObj> reqPostList) {
        this.context = applicationContext;
        this.reqPosts = reqPostList;
    }

    public void setItems(ArrayList<ReqPostObj> items) {
        this.reqPosts.clear();
        this.reqPosts.addAll(items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_req_post_individual, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReqPostObj post = this.reqPosts.get(position);
        holder.itemView.setOnClickListener(view_name -> {
            Intent expReqsIntent = new Intent(this.context, ExpandedReqPost.class);
            expReqsIntent.putExtra("currReq", post);
            this.context.startActivity(expReqsIntent);
        });

        holder.itemName.setText(post.itemName);
        // TODO format using JAVA API instead of manually
        Log.d(TAG, "getView: " + post.description);
        String postDateParsed = post.createdAt.toString().split(" ")[1] + " " + post.createdAt.toString().split(" ")[2];
        holder.postDate.setText("Posted on: " + postDateParsed);
        holder.description.setText(post.description);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return this.reqPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName;
        private final TextView postDate;
        private final TextView description;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.itemName = view.findViewById(R.id.req_item_name);
            this.postDate = view.findViewById(R.id.req_post_date);
            this.description = view.findViewById(R.id.req_description);
        }
    }
}