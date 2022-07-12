package com.example.community;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.community.classes.Tag;
import com.example.community.databinding.TagIndividualBinding;

import java.util.ArrayList;
import java.util.List;

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.ViewHolder> {

    private final List<Tag> mValues;

    public TagListAdapter() {
        mValues = new ArrayList<>();
    }
    public TagListAdapter(List<Tag> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(TagIndividualBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Tag thisTag = holder.mItem;
        holder.mContentView.setText(thisTag.name);
        holder.mContentView.setOnClickListener(v -> {
            thisTag.click();
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public Tag mItem;

        public ViewHolder(TagIndividualBinding binding) {
            super(binding.getRoot());
            mContentView = binding.tagName;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public void setItems(ArrayList<Tag> tags) {
        this.mValues.clear();
        this.mValues.addAll(tags);
    }

    public ArrayList<Tag> getClickedTags() {
        ArrayList<Tag> ret = new ArrayList<>();
        for (Tag t : this.mValues) {
            if (t.clicked) {
                ret.add(t);
            }
        }

        return ret;
    }
}