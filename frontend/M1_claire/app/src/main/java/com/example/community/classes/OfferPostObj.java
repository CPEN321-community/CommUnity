package com.example.community.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class OfferPostObj {

    public String offerId;
    public String itemName;
    public String description;
    public String pickupAddr;
    public String image;
    //Change to date object?
    public Object postDate;
    public Object bestBefore;

    public OfferPostObj(JSONObject json) {
        try {
            //TBD
            this.offerId = json.getString("postId");
            this.itemName = json.getString("title");
            this.description = json.getString("description");
            //TODO: convert to a distance
            this.pickupAddr = json.getString("pickUpLocation");
            this.image = json.getString("image");
            this.postDate = json.getString("creationDate");
            this.bestBefore = json.get("bestBeforeDate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}