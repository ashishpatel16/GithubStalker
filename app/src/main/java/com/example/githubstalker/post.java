package com.example.githubstalker;

import com.google.gson.annotations.SerializedName;

public class post {
    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("followers")
    private Integer followers;

    @SerializedName("following")
    private Integer following;

    @SerializedName("avatar_url")
    private String avatar_url;

    public String getAvatar_url(){
        return avatar_url;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getFollowers() {
        return followers;
    }

    public int  getFollowing() {
        return following;
    }
}
