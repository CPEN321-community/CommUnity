package com.example.community.classes;

public class Tag {
    public String name;
    public boolean clicked = false;
    private final int index;

    public Tag(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public void click() {
        TagHelper.click(index);
    }

}
