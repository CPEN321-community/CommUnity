package com.example.community.classes;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile {
    private static final String TAG = "USER_PROFILE_CLASS";
    public String firstName;
    public String lastName;
    public String email;
    public String profilePicture;

    public UserProfile(JSONObject userJSON) {
        try {
            this.firstName = userJSON.getString("firstName");
            this.lastName = userJSON.getString("lastName");
            this.email = userJSON.getString("email");
            this.profilePicture = userJSON.getString("profilePicture");
        } catch (JSONException e) {
            Log.e(TAG, "UserProfile: " + e);
            e.printStackTrace();
        }
    }
}
