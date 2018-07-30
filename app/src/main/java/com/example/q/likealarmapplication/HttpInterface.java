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
    Call<JsonObject> createUser(@Field("facebook_id") String facebook_id, @Field("nickname") String nickname, @Field("is_love") String islove, @Field("is_boring") String isboring, @Field("is_need") String isneeds);

    @GET("/users")
    Call<JsonObject> getUser(@Header("facebook_id") String facebook_id);

    @FormUrlEncoded
    @POST("/users/is_love")
    Call<JsonObject> isLove(@Field("facebook_id") String facebook_id, @Field("is_love") String islove);

    @FormUrlEncoded
    @POST("/users/is_boring")
    Call<JsonObject> isBoring(@Field("facebook_id") String facebook_id, @Field("is_boring") String isboring);

    @FormUrlEncoded
    @POST("/users/is_need")
    Call<JsonObject> isNeeds(@Field("facebook_id") String facebook_id, @Field("is_need") String isneeds);

    @GET("/users/is_love")
    Call<JsonObject> getisLove(@Header("facebook_id") String facebook_id);

    @GET("/users/is_boring")
    Call<JsonObject> getisBoring(@Header("facebook_id") String facebook_id);

    @GET("/users/is_needs")
    Call<JsonObject> getisNeeds(@Header("facebook_id") String facebook_id);


}
