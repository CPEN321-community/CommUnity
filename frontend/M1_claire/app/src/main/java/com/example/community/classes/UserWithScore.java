package com.example.community.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class UserWithScore {
    public String firstName;
    public String lastName;
    public String profilePicture;
    public int offerPosts;
    public int requestPosts;
    public int score;

    public UserWithScore(JSONObject json) {
        try {
            this.firstName = json.getString("firstName");
            this.lastName = json.getString("lastName");
            this.profilePicture = json.getString("profilePicture");
            this.offerPosts = json.getInt("offerPosts");
            this.requestPosts = json.getInt("requestPosts");
            this.score = json.getInt("score");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//
    }

}
