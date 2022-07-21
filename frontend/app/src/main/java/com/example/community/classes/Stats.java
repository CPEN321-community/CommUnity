package com.example.community.classes;

import org.json.JSONObject;

public class Stats {

    public int offerPosts;
    public int requestPosts;
    public int score;

    public Stats(JSONObject statsJSON) {
        {
            try {
                this.offerPosts = statsJSON.getInt("offerPosts");
                this.requestPosts = statsJSON.getInt("requestPosts");
                this.score = statsJSON.getInt("score");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
