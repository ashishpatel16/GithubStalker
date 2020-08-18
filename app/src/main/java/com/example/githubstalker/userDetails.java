package com.example.githubstalker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
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

    Button getRepositoriesButton;
    TextView name;
    TextView email;
    TextView followers;
    TextView following;
    ImageView dp;
    Bundle extras;

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
        name.setText(extras.getString("NAME_FROM_MAIN_ACTIVITY"));
        email.setText(extras.getString("EMAIL_FROM_MAIN_ACTIVITY"));
        followers.setText(extras.getString("FOLLOWERS_FROM_MAIN_ACTIVITY"));
        following.setText(extras.getString("FOLLOWING_FROM_MAIN_ACTIVITY"));
        downloadImageTask task = new downloadImageTask();
        try{
            Bitmap img = task.execute(extras.getString("URL_FROM_MAIN_ACTIVITY")).get();
            if(img!=null) dp.setImageBitmap(img);
        }catch(Exception e) {
            e.printStackTrace();
        }

    }
    public void onButtonClick(View view) {
        Intent intent = new Intent(this,repositoryList.class);
        intent.putExtra("USERNAME_FROM_MAIN_ACTIVITY",extras.getString("USERNAME_FROM_MAIN_ACTIVITY"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
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