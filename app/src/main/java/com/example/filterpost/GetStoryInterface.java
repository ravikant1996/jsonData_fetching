package com.example.filterpost;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetStoryInterface {
    @GET("/posts")
    Call<String> STRING_CALL(
            @Query("page") int page,
            @Query("limit") int limit
    );

    @GET("posts")
    Call<List<Posts>> getAllPost();
}
