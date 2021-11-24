package com.example.filterpost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.filterpost.databinding.ActivityPostBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PostActivity extends AppCompatActivity {
    String url = "https://jsonplaceholder.typicode.com/";
    ActivityPostBinding binding;
    ArrayList<Posts> postsArrayList;
    StoryAdapter dataAdapter;

    int page = 1, limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        postsArrayList = new ArrayList<>();

        dataAdapter = new StoryAdapter(postsArrayList, PostActivity.this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(dataAdapter);
        binding.recyclerView.setItemViewCacheSize(50);

        getData(page, limit);
    }

    private void getData(int page, int limit) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        GetStoryInterface getStoryInterface = retrofit.create(GetStoryInterface.class);

        Call<String> call = getStoryInterface.STRING_CALL(page, limit);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    binding.progressBar.setVisibility(View.GONE);
                    Log.e("response.body().toString()", response.body());
                    Log.e("response", response.body().toString());

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}