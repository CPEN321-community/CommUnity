package com.example.community.offer_list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.community.R;
import com.example.community.classes.OfferPostObj;
import com.example.community.classes.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class OfferPostAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<OfferPostObj> offerPosts;
    private final LayoutInflater inflater;

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
        view = inflater.inflate(R.layout.fragment_offer_post_individual, null);
        TextView itemName = (TextView) view.findViewById(R.id.offer_item_name);
        TextView itemQuantity = (TextView) view.findViewById(R.id.offer_quantity);
        TextView pickupLocation = (TextView) view.findViewById(R.id.offer_pickup_loc);

        OfferPostObj post = this.offerPosts.get(i);

        itemName.setText(post.itemName);
        view.setOnClickListener(view_name -> {
            Intent expOffersIntent = new Intent(this.context, ExpandedOfferPost.class);
            expOffersIntent.putExtra("currOffer", post);
            this.context.startActivity(expOffersIntent);
        });
        //TODO: fix once we have calculated distance
        //distance.setText(post.distanceKm);
        pickupLocation.setText(post.pickupAddr);
        itemQuantity.setText(String.valueOf(post.quantityKg));

        ImageView itemImage = (ImageView) view.findViewById(R.id.offer_image);
        if (!Objects.equals(post.image, "")) {
            Utils.setImageWhenLoaded(context, post.image, itemImage);
        } else {
            itemImage.setImageDrawable(Utils.GetDefaultAvatar(context));
        }
        return view;
    }
}
