package com.example.githubstalker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class userDetails extends AppCompatActivity {

    private final String baseURL = "https://api.github.com/";
    Button getRepositoriesButton;
    public TextView name;
    TextView email;
    TextView followers;
    TextView following;
    ImageView dp;
    Bundle extras;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // Instantiating everything we need.
        getRepositoriesButton = findViewById(R.id.getRepositoriesButton);
        name = findViewById(R.id.nameTextView);
        email = findViewById(R.id.emailTextView);
        followers = findViewById(R.id.followersTextView);
        following = findViewById(R.id.followingTextView);
        dp = findViewById(R.id.displayPicture);

        extras = getIntent().getExtras();
        username = extras.getString("USERNAME_FROM_MAIN_ACTIVITY");
        name.setText(username);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        GithubApi myApi = retrofit.create(GithubApi.class);
        Call<List<post>> call = myApi.getPosts();

        call.enqueue(new Callback<List<post>>() {
            @Override
            public void onResponse(Call<List<post>> call, Response<List<post>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(userDetails.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    Log.i("error!",""+response.code());
                }else {
                    List<post> details = response.body();
                    for(post post : details){
                        name.setText(post.getName());
                        email.setText(post.getEmail());
                        followers.setText(post.getFollowers());
                        following.setText(post.getFollowing());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<post>> call, Throwable t) {
                Log.i("Error!",t.getMessage());
            }
        });
    }

    public void onButtonClick(View view) {

        Intent intent = new Intent(this,repositoryList.class);
        startActivity(intent);
    }

}