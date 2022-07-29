package com.example.community.classes;

import com.example.community.exceptions.PreferenceException;

import org.json.JSONObject;

public class DietaryRestriction {
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

    public DietaryRestriction(Preference pref) throws PreferenceException {
        if (!pref.isDietary())
            throw new PreferenceException("Attempted to Parse non-DIETARY preference as DIETARY");
        this.id = pref.getId();
        this.name = pref.getValue();
    }

    public String getId() {
        return id;
    }
}
