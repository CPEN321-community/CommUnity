package com.example.community.classes;

public class Tag {
    private static final String TAG = "TAG_CLASS";
    public String name;
    public boolean clicked = false;
    private final int index;

    public Tag(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public void click() {
        TagManager.click(index);
    }

}
