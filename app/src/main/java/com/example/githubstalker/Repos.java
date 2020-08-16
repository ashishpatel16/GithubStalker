package com.example.githubstalker;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Repos {

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("html_url")
    private String html_url;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHtml_url() {
        return html_url;
    }
}
