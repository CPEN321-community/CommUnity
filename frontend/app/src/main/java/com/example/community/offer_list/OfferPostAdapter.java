package com.example.community.offer_list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.R;
import com.example.community.classes.DateImgUtil;
import com.example.community.classes.OfferPostObj;

import java.util.ArrayList;
import java.util.Objects;

public class OfferPostAdapter extends RecyclerView.Adapter<OfferPostAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<OfferPostObj> offerPosts;

    public OfferPostAdapter(Context applicationContext, ArrayList<OfferPostObj> offerPostList) {
        this.context = applicationContext;
        this.offerPosts = offerPostList;
    }

    public void setItems(ArrayList<OfferPostObj> posts) {
        this.offerPosts.clear();
        this.offerPosts.addAll(posts);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_offer_post_individual, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OfferPostObj post = this.offerPosts.get(position);
        holder.itemName.setText(post.itemName);
        holder.itemView.setOnClickListener(view_name -> {
            Intent expOffersIntent = new Intent(this.context, ExpandedOfferPost.class);
            expOffersIntent.putExtra("currOffer", post);
            this.context.startActivity(expOffersIntent);
        });
        //TODO: fix once we have calculated distance
        //distance.setText(post.distanceKm);
        holder.pickupLocation.setText(post.pickupAddr);
        holder.itemQuantity.setText(String.valueOf(post.quantityKg));

        ImageView itemImage = holder.itemImage.findViewById(R.id.offer_image);
        if (!Objects.equals(post.image, "")) {
            DateImgUtil.setImageWhenLoaded(context, post.image, itemImage);
        } else {
            itemImage.setImageDrawable(DateImgUtil.GetDefaultAvatar(context));
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return this.offerPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName;
        private final TextView itemQuantity;
        private final TextView pickupLocation;
        private final ImageView itemImage;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.itemName = view
                    .findViewById(R.id.offer_item_name);
            this.itemQuantity = view
                    .findViewById(R.id.offer_quantity);
            this.pickupLocation = view
                    .findViewById(R.id.offer_pickup_loc);
            this.itemImage = view.findViewById(R.id.offer_image);
        }
    }
}
