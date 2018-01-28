package com.pantomim.Interface;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by aryahm on 1/28/18.
 */

public interface Request {
    @POST("signup")
    Call<JsonObject> register(@Body JsonObject register);
    @GET("get_empty_games")
    Call<JsonObject> getGames();
    @POST("add_game")
    Call<JsonObject> addGame(@Body JsonObject addGame);
    @POST("add_client")
    Call<JsonObject> addClient(@Body JsonObject addClient);
    @GET("get_game/{id}")
    Call<JsonObject> getGame(@Path("id") String id);
    @DELETE("leave_game/{id}")
    Call<JsonObject> deleteGame(@Path("id") String id);

}
