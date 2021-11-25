package com.example.filterpost;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.filterpost.databinding.ActivityPostBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity {
    String url = "https://jsonplaceholder.typicode.com";
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

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.text.getText().toString().trim().isEmpty()) {
                    binding.text.setError("Enter text");
                } else {
                    String editValue = binding.text.getText().toString().toString();
                    singleTonExample singletonexample = singleTonExample.getInstance();
                    singletonexample.setText(editValue);
                    Toast.makeText(PostActivity.this, singletonexample.getText(), Toast.LENGTH_LONG).show();
                }
            }
        });

        getData(page, limit);
//        getData2(page, limit);

        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    if (query.isEmpty()) {
                        dataAdapter.updateList();
                    } else if (dataAdapter != null) {
                        dataAdapter.getFilter().filter(query);
                    } else {
                        Toast.makeText(PostActivity.this, "No Match found", Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    if (newText.isEmpty()) {
                        dataAdapter.updateList();
                    } else if (dataAdapter != null) {
                        dataAdapter.getFilter().filter(newText);
                    } else {
                        if (newText.length() >= 3)
                            Toast.makeText(PostActivity.this, "No Match found", Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        ImageView closeButton = (ImageView) binding.searchBar.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    binding.searchBar.setQuery("", false);
                    dataAdapter.updateList();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
//                Intent intent = getIntent();
//                finish();
//                overridePendingTransition(0, 0);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
            }
        });
    }

    private void getData(int page, int limit) {

        Call<List<Posts>> call = RetrofitClient.getInstance().getMyApi().getAllPost();
        call.enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        binding.progressBar.setVisibility(View.GONE);
                        List<Posts> arrayList = response.body();
                        postsArrayList.addAll(arrayList);
                        Log.e("response", arrayList.toString());
                        dataAdapter.notifyDataSetChanged();

                    } else {
                        Log.e("Call", call.toString());
                    }
                } catch (NullPointerException w) {
                    Log.e("NullPointerException", "response.body().toString()");
                }
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                Log.e("t.getMessage()", t.getMessage());
            }
        });
    }

    private void getData2(int page, int limit) {
        RetrofitClient.getInstance().getMyApi()
                .getPost()
                .flatMapIterable(list -> list)
                .flatMap(posts -> {
                    postsArrayList.add(posts);
                    dataAdapter.notifyDataSetChanged();
                    return null;
                })
                .subscribe();

//        Call<List<Posts>> call = RetrofitClient.getInstance().getMyApi().getAllPost();
//        call.enqueue(new Callback<List<Posts>>() {
//            @Override
//            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
//                try {
//                    if (response.isSuccessful() && response.body() != null) {
//                        binding.progressBar.setVisibility(View.GONE);
//                        List<Posts> arrayList = response.body();
//                        postsArrayList.addAll(arrayList);
//                        Log.e("response", arrayList.toString());
//                        dataAdapter.notifyDataSetChanged();
//
//                    } else {
//                        Log.e("Call", call.toString());
//                    }
//                } catch (NullPointerException w) {
//                    Log.e("NullPointerException", "response.body().toString()");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Posts>> call, Throwable t) {
//                Log.e("t.getMessage()", t.getMessage());
//            }
//        });
    }
}