package com.example.tefa.projetmaheu;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Tefa on 04/10/2016.
 */

public interface RegisterAPI {
    @FormUrlEncoded
    @POST("/Nicolas/Android/Android_Insert.php")
    public void insertUser(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            @Field("keys") String keys,
            Callback<Response> callback);
}
