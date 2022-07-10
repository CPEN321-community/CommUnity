package com.example.community.classes;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DietaryRestriction {
    private static final String TAG = "DIETARY_RESTRICTION_CLASS";
    private String id;
    public String name;

    public DietaryRestriction(JSONObject dietaryRestrictionJSON) {
        try {
            this.id = dietaryRestrictionJSON.getString("id");
            this.name = dietaryRestrictionJSON.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Delete(Context context) {
        DeleteRequest(context);
    }

    private void DeleteRequest(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://10.0.2.2:8080/restrictions/" + Global.getAccount().getId();
        JSONObject deleteBody = new JSONObject();
        try {
            deleteBody.put("id", this.id);
        } catch (Exception e) {
            Log.e(TAG, "DeleteRequest: " + e);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                url,
                deleteBody,
                (JSONObject response) -> {
                    try {
                        int deleted = response.getInt("deleted");
                        Log.d(TAG, "DeleteRequest: " + "Deleted " + deleted + " item: " + this.id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "fetchLeaderboard: " + error);
                });
        queue.add(request);
    }

    public String getId() {
        return this.id;
    }
}
