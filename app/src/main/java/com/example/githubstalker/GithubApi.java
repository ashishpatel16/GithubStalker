package com.example.githubstalker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubApi {

    @GET("users/{user}")
    Call<post> getPosts(@Path("user") String user);
}
