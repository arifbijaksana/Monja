package com.haerul.monja.data.api;

import com.google.gson.JsonObject;
import com.haerul.monja.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    
    @POST("api/login")
    Call<JsonObject> login(@Body JsonObject jsonObject);

    @POST("api/init_db")
    Call<JsonObject> initDB(
            @Header(Constants.SECURITY_KEY) String auth_token,
            @Body JsonObject jsonObject);
    
    @POST("api/inspeksi")
    Call<JsonObject> postInspeksi(
            @Header(Constants.SECURITY_KEY) String auth_token,
            @Body JsonObject body);

    @POST("api/base64_data")
    Call<JsonObject> postBase64Data(
            @Header(Constants.SECURITY_KEY) String auth_token,
            @Body JsonObject body);
    
}
