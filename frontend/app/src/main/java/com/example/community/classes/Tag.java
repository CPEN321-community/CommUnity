package com.example.community.classes;

public class Tag {
    public String name;
    public boolean clicked;

    public Tag(String name) {
        this.clicked = false;
        this.name = name;
    }

    public void click() {
        this.clicked = !this.clicked;
    }

}
