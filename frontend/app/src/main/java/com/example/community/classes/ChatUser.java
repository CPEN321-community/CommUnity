package com.example.community.classes;

import java.io.Serializable;

public class ChatUser implements Serializable {
    public String firstName;
    public String lastName;
    public String profilePicture;

    public ChatUser(String firstName, String lastName, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
    }
}
