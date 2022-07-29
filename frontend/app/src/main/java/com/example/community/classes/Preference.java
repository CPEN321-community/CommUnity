package com.example.community.classes;

import org.json.JSONObject;

import java.util.Objects;

public class Preference {
    private String id;
    private String type;
    private String value;

    public Preference(JSONObject prefJSON) {
        try {
            this.id = prefJSON.getString("id");
            this.type = prefJSON.getString("type");
            this.value = prefJSON.getString("value");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDietary() {
        return Objects.equals(this.type, "DIETARY");
    }

    public String getValue() {
        return value;
    }

    public String getId() {
        return id;
    }
}
