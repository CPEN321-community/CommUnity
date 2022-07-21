package com.example.community.offer_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.community.R;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.OfferPostObj;
import com.example.community.classes.Util;
import com.example.community.ui.chat.ChatActivity;

import java.util.Objects;

public class ExpandedOfferPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_offer_post);

        TextView itemName = (TextView) this.findViewById(R.id.offer_item_name_exp);
        TextView itemQuantity = (TextView) this.findViewById(R.id.offer_item_quantity_exp);
        TextView pickupLocation = (TextView) this.findViewById(R.id.offer_item_addr_exp);
        TextView description = (TextView) this.findViewById(R.id.offer_item_description_exp);
        TextView bestBefore = (TextView) this.findViewById(R.id.offer_item_bb_date_exp);
        Button acceptButton = this.findViewById(R.id.accept_offer_button);
        Intent expOfferIntent = getIntent();
        OfferPostObj post = (OfferPostObj) expOfferIntent.getSerializableExtra("currOffer");
        if (post.userId.equals(GlobalUtil.getAccount().getId())) {
            acceptButton.setVisibility(View.GONE);
        } else {
            acceptButton.setOnClickListener(v -> {
                Intent chatIntent = new Intent(this, ChatActivity.class);
                chatIntent.putExtra("createRoomId", post.offerId);
                startActivity(chatIntent);
                finish();
            });
        }

        itemName.setText(post.itemName);
        //TODO: change pickup location to distance in the future
        itemQuantity.setText("Quantity: " + post.quantityKg + "kg");
        pickupLocation.setText(post.pickupAddr);
        description.setText(post.description);
        String bbDateParsed = post.bestBefore.toString().split(" ")[1] + " " + post.bestBefore.toString().split(" ")[2];
        bestBefore.setText("Best Before: " + bbDateParsed);

        ImageView itemImage = (ImageView) this.findViewById(R.id.offer_item_image_exp);
        if (!Objects.equals(post.image, "")) {
            Util.setImageWhenLoaded(this, post.image, itemImage);
        } else {
            itemImage.setImageDrawable(Util.GetDefaultAvatar(this));
        }


    }
}