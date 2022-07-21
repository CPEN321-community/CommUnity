package com.example.community.classes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OfferPostObj implements Serializable {

    public String offerId;
    public String itemName;
    public int quantityKg;
    public String description;
    public String pickupAddr;
    public String image;
    public Date bestBefore;
    public String userId;

    public OfferPostObj(JSONObject json) {
        try {
            this.offerId = json.getString("offerId");
            this.userId = json.getString("userId");
            this.itemName = json.getString("title");
            this.description = json.getString("description");
            this.quantityKg = json.getInt("quantity");
            //TODO: convert to a distance
            this.pickupAddr = json.getString("pickUpLocation");
            this.image = json.getString("image");
            String bestBeforeString = json.getString("bestBeforeDate");
            this.bestBefore = new SimpleDateFormat(Util.dateFormatString).parse(bestBeforeString);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}