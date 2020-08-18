package com.example.githubstalker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // This corresponds to first screen,i.e. Username and login screen.
    private final String baseURL = "https://api.github.com/";
    Button loginBtn;
    EditText usernameField;
    Intent i;
    String username;
    String name,email,avatar_url, followers,following;
    LottieAnimationView animationView;

    public void login(View view){
        Log.i("Login success",usernameField.getText().toString());
        username = usernameField.getText().toString();
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();
        loadData();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.loginButton);
        usernameField = findViewById(R.id.usernameTxtField);
        animationView = findViewById(R.id.animationView);
    }

    public void jumpToNextActivity(){
        i = new Intent(this,userDetails.class);
        i.putExtra("NAME_FROM_MAIN_ACTIVITY",name);
        i.putExtra("EMAIL_FROM_MAIN_ACTIVITY",email);
        i.putExtra("URL_FROM_MAIN_ACTIVITY",avatar_url);
        i.putExtra("FOLLOWERS_FROM_MAIN_ACTIVITY",followers);
        i.putExtra("FOLLOWING_FROM_MAIN_ACTIVITY",following);
        i.putExtra("USERNAME_FROM_MAIN_ACTIVITY",username);
        animationView.setVisibility(View.INVISIBLE);
        animationView.pauseAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(i);
        }

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
                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    Log.i("error!",""+response.code());
                }else {

                    if(response.body().getName()!=null){
                        Log.i("status","name not null");
                        name = "Name : " + response.body().getName();
                    }else {
                        name = "Name not found";
                    }
                    if(response.body().getEmail()!=null) {
                        email = "Email : "+response.body().getEmail();
                        Log.i("status","email not null");
                    }else {
                        email = "Email not found";
                    }
                    followers = "Followers : "+ String.valueOf(response.body().getFollowers());
                    following = "Following : "+ String.valueOf(response.body().getFollowing());
                    avatar_url = response.body().getAvatar_url();
                    Log.i("avatar url" ,""+response.body().getAvatar_url());
                    jumpToNextActivity();
                }
            }

            @Override
            public void onFailure(Call<post> call, Throwable t) {
                Log.i("Failure!",t.getMessage());
                animationView.pauseAnimation();
                animationView.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}