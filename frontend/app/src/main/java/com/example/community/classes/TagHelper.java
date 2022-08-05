package com.example.community.classes;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;

import java.util.ArrayList;

public class TagHelper {
    private static final String[] allTags = {"Fruit", "Veggie", "Meat", "Fish", "Keto", "Nut-Free", "Spice", "Carb", "Sauce", "Other"};

    private static final MutableLiveData<ArrayList<Tag>> tags = new MutableLiveData<>(new ArrayList());

    public static ArrayList<Tag> getTags() {
        if (tags.getValue().size() == 0) {
            ArrayList<Tag> localTags = new ArrayList<>();
            for (int i = 0; i < allTags.length; i++) {
                String s = allTags[i];
                localTags.add(new Tag(s, i));
            }
            tags.postValue(localTags);
        }
        return tags.getValue();
    }

    public static ArrayList<Tag> reset() {
        ArrayList<Tag> tags = getTags();
        for (int i = 0; i < tags.size(); i++) {
            disable(i);
        }
        return tags;
    }

    public static ArrayList<Tag> getClickedTags() {
        ArrayList<Tag> tags = getTags();
        ArrayList<Tag> ret = new ArrayList<>();
        for (Tag t : tags) {
            if (t.clicked) {
                ret.add(t);
            }
        }

        return ret;
    }

    public static JSONArray getJSONArr() {
        JSONArray ret = new JSONArray();
        ArrayList<Tag> ts = getTags();
        for (Tag t : ts) {
            if (t.clicked) {
                ret.put(t.name);
            }
        }
        return ret;
    }

    private static Tag getTagByIndex(int index) {
        ArrayList<Tag> tags = getTags();
        return tags.get(index);
    }

    public static void click(int index) {
        Tag t = getTagByIndex(index);
        t.clicked = !t.clicked;
        ArrayList<Tag> ts = tags.getValue();
        ts.set(index, t);
        tags.postValue(ts);
    }

    private static void disable(int index) {
        Tag t = getTagByIndex(index);
        t.clicked = false;
        ArrayList<Tag> ts = tags.getValue();
        ts.set(index, t);
        tags.postValue(ts);
    }

    public static MutableLiveData<ArrayList<Tag>> getTagData() {
        return tags;
    }
}
