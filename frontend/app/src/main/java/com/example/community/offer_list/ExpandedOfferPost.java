package com.example.community.offer_list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.community.R;
import com.example.community.classes.ChatHelper;
import com.example.community.classes.ChatRoom;
import com.example.community.classes.CreateRoomInterface;
import com.example.community.classes.DateImgUtil;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.OfferPostObj;
import com.example.community.ui.chat.ChatActivity;

import java.util.Objects;

public class ExpandedOfferPost extends AppCompatActivity {

    private static final String TAG = "EXPANDED_OFFER_POST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_offer_post);

        TextView itemName = this.findViewById(R.id.offer_item_name_exp);
        TextView itemQuantity = this.findViewById(R.id.offer_item_quantity_exp);
        TextView pickupLocation = this.findViewById(R.id.offer_item_addr_exp);
        TextView description = this.findViewById(R.id.offer_item_description_exp);
        TextView bestBefore = this.findViewById(R.id.offer_item_bb_date_exp);
        Button acceptButton = this.findViewById(R.id.accept_offer_button);
        Intent expOfferIntent = getIntent();
        OfferPostObj post = (OfferPostObj) expOfferIntent.getSerializableExtra("currOffer");
        if (post.userId.equals(GlobalUtil.getId()) || ChatHelper.getChats().getValue().containsKey(post.offerId)) {
            acceptButton.setVisibility(View.GONE);
        } else {
            acceptButton.setOnClickListener(v -> {
                CreateRoomInterface i = new CreateRoomInterface() {
                    @Override
                    public void onSuccess(ChatRoom room) {
                        Intent chatIntent = new Intent(ExpandedOfferPost.this, ChatActivity.class);
                        startActivity(chatIntent);
                        finish();
                    }

                    @Override
                    public void onFailure() {
//                        Toast or something
                        Log.d(TAG, "onFailure: ERROR");
                    }
                };
                ChatHelper.CreateRoom(post.offerId, true, i);
            });
        }

        itemName.setText(post.itemName);
        //TODO: change pickup location to distance in the future
        itemQuantity.setText("Quantity: " + post.quantityKg + "kg");
        pickupLocation.setText(post.pickupAddr);
        description.setText(post.description);
        String bbDateParsed = post.bestBefore.toString().split(" ")[1] + " " + post.bestBefore.toString().split(" ")[2];
        bestBefore.setText("Best Before: " + bbDateParsed);

        ImageView itemImage = this.findViewById(R.id.offer_item_image_exp);
        if (!Objects.equals(post.image, "")) {
            DateImgUtil.setImageWhenLoaded(this, post.image, itemImage);
        } else {
            itemImage.setImageDrawable(DateImgUtil.GetDefaultAvatar(this));
        }


    }
}