package com.example.community.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.R;
import com.example.community.classes.Tag;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    private static final String TAG = "TAG_ADAPTER";
    private final Context context;
    private final ArrayList<Tag> tags;

    public TagAdapter(Context applicationContext, ArrayList<Tag> tags) {
        this.context = applicationContext;
        this.tags = tags;
        Log.d(TAG, "TagAdapter: Created");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.tag_indiv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tag t = this.tags.get(position);
        holder.name.setText(t.name);
        holder.itemView.setOnClickListener(v -> t.click());
        if (t.clicked.getValue()) {
            holder.name.setBackgroundResource(R.drawable.full_rounded_card_clicked);
        } else {
            holder.name.setBackgroundResource(R.drawable.full_rounded_card);
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + this.tags.size());
        return this.tags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.name = view.findViewById(R.id.tag_name);
        }
    }
}
