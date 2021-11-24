package com.example.filterpost;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.filterpost.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String url = "https://jsonplaceholder.typicode.com/posts";
    private RequestQueue mQueue;
    ActivityMainBinding binding;
    StoryAdapter dataAdapter;

    ArrayList<Posts> postsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postsArrayList = new ArrayList<>();

        mQueue = Volley.newRequestQueue(this);
        jsonParse(new GetStories() {
            @Override
            public void onCallBack(ArrayList<Posts> galleryArrayList) {
                if (galleryArrayList.size() > 0) {
                    binding.recyclerView.setHasFixedSize(true);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
                    dataAdapter = new StoryAdapter(galleryArrayList, MainActivity.this);
                    binding.recyclerView.setAdapter(dataAdapter);
                    binding.recyclerView.setItemViewCacheSize(50
                    );
                }

            }
        });

        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    if (query.isEmpty()) {
                        dataAdapter.updateList();
                    } else if (dataAdapter != null) {
                        dataAdapter.getFilter().filter(query);
                    } else {
                        Toast.makeText(MainActivity.this, "No Match found", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MainActivity.this, "No Match found", Toast.LENGTH_LONG).show();
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

    private void jsonParse(GetStories getStories) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("response", response);

                        postsArrayList = new Gson().fromJson(response,
                                new TypeToken<ArrayList<Posts>>() {
                                }.getType());

                        Log.e("postsArrayList", postsArrayList.toString());

                        getStories.onCallBack(postsArrayList);
                        try {
                            JSONArray result = new JSONArray(response);
                            for (int i = 0; i < result.length(); i++) {

                                JSONObject c = result.getJSONObject(i);

                                String UserRole = c.getString("UserRole");
                                String UserName = c.getString("UserName");
                                int Id = c.getInt("Id");
                                String Email = c.getString("Email");

                            }

                        } catch (JSONException e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        mQueue.add(stringRequest);

//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        ArrayList<Posts> productList = new Gson().fromJson(response.toString(), new TypeToken<ArrayList<Posts>>() {
//                        }.getType());
//                        Log.e("response", productList.toString());
//                        try {
//                            JSONArray json = new JSONArray(response);
//
//                            for (int i = 0; i < json.length(); i++) {
//
//                                JSONObject product = json.getJSONObject(i);
//                                Log.e("json 1", product.getString("product_title"));
//
////                                productList.add(new Product(
////                                        product.getString("product_id"),
////                                        product.getString("product_title"),
////                                        product.getString("product_thumbnail"),
////                                        product.getString("product_description"),
////                                        product.getString("product_place"),
////                                        product.getString("product_user"),
////                                        product.getString("product_store"),
////                                        product.getString("product_date"),
////                                        product.getString("product_off"),
////                                        product.getString("product_price")));
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        mQueue.add(request);
    }

}