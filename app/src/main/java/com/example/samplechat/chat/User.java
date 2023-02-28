package com.example.samplechat.chat;

public class User {

    private String uid, name, email, profileImage;
//Empty Constructor compulsory to deal with firebase
    public User() {

    }
//Filled Constructor to initiate object of user class


    public User(String uid, String name, String email, String profileImage) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
