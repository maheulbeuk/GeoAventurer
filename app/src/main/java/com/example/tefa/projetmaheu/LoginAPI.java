package com.example.tefa.projetmaheu;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Tefa on 06/10/2016.
 */

public interface LoginAPI {

    @FormUrlEncoded
    @POST("/Nicolas/Android/Android_Select.php")
    public void loginUser(
            @Field("username") String username,
            @Field("password") String password,
            @Field("keys") String keys,
            Callback<Response> callback
    );

}
