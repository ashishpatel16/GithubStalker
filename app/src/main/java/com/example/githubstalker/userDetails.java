package com.example.githubstalker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class userDetails extends AppCompatActivity {

    private final String baseURL = "https://api.github.com/";
    Button getRepositoriesButton;
    TextView name;
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
        Log.i("username",""+username);
        name.setText(username);
        loadData();
    }

    public void loadData(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        GithubApi myApi = retrofit.create(GithubApi.class);
        Call<post> call = myApi.getPosts(username);
        call.enqueue(new Callback<post>() {
            @Override
            public void onResponse(Call<post> call, Response<post> response) {
                Log.i("status","started onResponse");
                if(!response.isSuccessful()){
                    Toast.makeText(userDetails.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    Log.i("error!",""+response.code());
                }else {
                    //Log.i("status","name,email,followers,following -> "+response.body().getName()+" "
                       // +response.body().getEmail()+" "+response.body().getFollowers()+" "+response.body().getFollowing());
                    if(response.body().getName()!=null){
                        Log.i("status","name not null");
                        name.setText("Name : " + response.body().getName());
                    }else {
                        name.setText("Name not found");
                    }
                    if(response.body().getEmail()!=null) {
                        email.setText("Email : "+response.body().getEmail());
                        Log.i("status","email not null");
                    }else {
                        email.setText("Email not found");
                    }
                    followers.setText("Followers : "+ String.valueOf(response.body().getFollowers()));
                    following.setText("Following : "+ String.valueOf(response.body().getFollowing()));

                    // Updating dp
                    downloadImageTask task = new downloadImageTask();
                    Log.i("avatar url" ,""+response.body().getAvatar_url());
                    Bitmap img = null;
                    try {
                        img = task.execute(response.body().getAvatar_url()).get();
                        if(img!=null) {
                            dp.setImageBitmap(img);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<post> call, Throwable t) {
                Log.i("Failure!",t.getMessage());
            }
        });
    }

    public void onButtonClick(View view) {

        Intent intent = new Intent(this,repositoryList.class);
        startActivity(intent);
    }

    public static class downloadImageTask extends AsyncTask<String,Void, Bitmap> {
        // Asynchronously download the display picture
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap downloadedImg;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream ip = connection.getInputStream();
                downloadedImg = BitmapFactory.decodeStream(ip);
                return downloadedImg;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}