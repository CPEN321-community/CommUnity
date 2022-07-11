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
            this.name = dietaryRestrictionJSON.getString("type");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DietaryRestriction(Preference pref) {
        if (!pref.isDietary()) throw new RuntimeException("Attempt to construct DietaryRestriction from non-DIETARY preference");
        this.id = pref.getId();
        this.name = pref.getValue();
    }

}
