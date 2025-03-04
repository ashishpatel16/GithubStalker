package com.example.githubstalker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RepositoryAPI {

    @GET("users/{user}/repos")
    Call<List<Repos>> getRepos(@Path("user") String user);
}
