package com.example.community.classes;

import org.json.JSONArray;

import java.util.ArrayList;

public class TagManager {
    private static final String[] allTags = {"Fruit", "Veggie", "Meat", "Fish", "Keto", "Nut-Free", "Spice", "Carb", "Sauce", "Other"};

    private static final ArrayList<Tag> tags = new ArrayList();

    public static ArrayList<Tag> getTags() {
        if (tags.size() == 0) {
            for (String s : allTags) {
                tags.add(new Tag(s));
            }
        }
        return tags;
    }

    public static ArrayList<Tag> reset() {
        ArrayList<Tag> tags = getTags();
        for (Tag t : tags) {
            t.getClickData().postValue(false);
        }
        return tags;
    }

    public static ArrayList<Tag> getClickedTags() {
        ArrayList<Tag> tags = getTags();
        ArrayList<Tag> ret = new ArrayList<>();
        for (Tag t : tags) {
            if (t.clicked.getValue()) {
                ret.add(t);
            }
        }

        return ret;
    }

    public static JSONArray getJSONArr() {
        JSONArray ret = new JSONArray();
        for (Tag t: tags) {
            if (Boolean.TRUE.equals(t.clicked.getValue())) {
                ret.put(t.name);
            }
        }
        return ret;
    }
}
