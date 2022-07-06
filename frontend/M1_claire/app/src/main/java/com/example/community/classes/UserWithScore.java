package com.example.community.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class UserWithScore {
    public String id;
    public int offerPosts;
    public int requestPosts;
    public int score;

    public UserWithScore(JSONObject json) {
        try {
            this.id = json.getString("id");
            this.offerPosts = json.getInt("offerPosts");
            this.requestPosts = json.getInt("requestPosts");
            this.score = json.getInt("score");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//
    }

}
