package com.example.githubstalker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class repositoryList extends AppCompatActivity {

    final String baseUrl = "https://api.github.com/";
    private String username;
    Intent i;
    Bundle extras;
    ListView myRepos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_list);
        myRepos = findViewById(R.id.repositoryLstView);

        extras = getIntent().getExtras();
        username = extras.getString("USERNAME_FROM_MAIN_ACTIVITY");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RepositoryAPI myApi = retrofit.create(RepositoryAPI.class);
        Call<List<Repos>> call = myApi.getRepos(username);
        call.enqueue(new Callback<List<Repos>>() {
            @Override
            public void onResponse(Call<List<Repos>> call, Response<List<Repos>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(repositoryList.this, "Error!", Toast.LENGTH_SHORT).show();
                    Log.i("Error","Repos not fetched! + Code "+response.code());
                }else{
                    ArrayList<String> repos = new ArrayList<>();
                    if(response.body().size()==0) {
                        repos.add("This user doesn't have any public repositories.");
                    }else {
                        for(Repos obj : response.body()) {
                            String str = "";
                            str += obj.getName() + "\n" + obj.getDescription() + "\n";
                            repos.add(str);
                            Log.i("DATA FETCHED",str);
                        }
                    }

                    ArrayAdapter<String> reposAdapter = new ArrayAdapter<String>(repositoryList.this,R.layout.list_white_text,repos);
                    myRepos.setAdapter(reposAdapter);
                    myRepos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String link = response.body().get(position).getHtml_url();
                            Uri url = Uri.parse(link);
                            Intent openUrl = new Intent(Intent.ACTION_VIEW,url);
                            startActivity(openUrl);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Repos>> call, Throwable t) {
                t.printStackTrace();
                Log.i("Failure","Something went wrong.");
                Toast.makeText(repositoryList.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}