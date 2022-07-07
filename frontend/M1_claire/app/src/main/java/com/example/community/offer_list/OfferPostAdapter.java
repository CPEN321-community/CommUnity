package com.example.community.offer_list;

import android.content.Context;
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

public class OfferPostAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OfferPostObj> offerPosts;
    private LayoutInflater inflater;

    public OfferPostAdapter(Context applicationContext, ArrayList<OfferPostObj> offerPostList) {
        this.context = applicationContext;
        this.offerPosts = offerPostList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return this.offerPosts.size();
    }

    @Override
    public Object getItem(int i) {
        return this.offerPosts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.fragment_offer_post, null);
        TextView itemName = (TextView) view.findViewById(R.id.item_name);
        TextView bestBefore = (TextView) view.findViewById(R.id.best_before);
        TextView distance = (TextView) view.findViewById(R.id.distance);
        //item description will not be listed in preview, only when user expands the post

        OfferPostObj post = this.offerPosts.get(i);

        itemName.setText(post.itemName);
        //TODO: fix once we have calculated distance
        //distance.setText(post.distanceKm);
        bestBefore.setText(String.valueOf(post.bestBefore));

        //TODO: set the image
        ImageView itemImage = (ImageView) view.findViewById(R.id.item_image);
        //itemImage.setImage
        return view;
    }
}
