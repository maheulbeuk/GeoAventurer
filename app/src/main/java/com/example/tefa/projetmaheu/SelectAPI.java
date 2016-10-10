package com.example.tefa.projetmaheu;

import android.view.ViewDebug;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by maheulbeuk on 10/10/2016.
 */

public interface SelectAPI {

    @FormUrlEncoded
    @POST("/Nicolas/Android/Android_Select.php")
   public void PagePrincipal(
            @Field("id") int IdUser,
            @Field("keys") String keys,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/Nicolas/Android/Android_Select.php")
    public void loginUser(
            @Field("username") String username,
            @Field("password") String password,
            @Field("keys") String keys,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/Nicolas/Android/Android_Select.php")
    public void ListLevel(
            @Field("keys") String keys,
            Callback<Response> callback
    );
}


