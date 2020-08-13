package com.example.githubstalker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    // This corresponds to first screen,i.e. Username and login screen.
    Button loginBtn;
    EditText usernameField;
    Intent i;

    public void login(View view){
        Log.i("Login success",usernameField.getText().toString());
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        i = new Intent(this,userDetails.class);
        i.putExtra("USERNAME_FROM_MAIN_ACTIVITY",usernameField.getText().toString());
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.loginButton);
        usernameField = findViewById(R.id.usernameTxtField);
    }
}