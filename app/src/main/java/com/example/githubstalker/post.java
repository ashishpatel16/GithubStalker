package com.example.githubstalker;

import com.google.gson.annotations.SerializedName;

public class post {
    private String name;
    private String email;
    private int followers;
    private int following;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
    }
}
