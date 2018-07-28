package com.example.q.likealarmapplication;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface HttpInterface {
    static final String BaseURL = "http://52.231.70.150:3000/";

    @FormUrlEncoded
    @POST("/users/create")
    Call<JsonObject> createUser(@Field("facebook_id") String facebook_id, @Field("nickname") String nickname);


    @GET("/users")
    Call<JsonObject> getUser(@Header("facebook_id") String facebook_id);

}
