package com.example.community.classes;

import org.json.JSONObject;

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
        if (!pref.isDietary())
            throw new RuntimeException("Attempt to construct DietaryRestriction from non-DIETARY preference");
        this.id = pref.getId();
        this.name = pref.getValue();
    }

    public String getId() {
        return id;
    }
}
