package com.example.githubstalker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GithubApi {
    @GET("users")
    Call<List<post>> getPosts();
}
