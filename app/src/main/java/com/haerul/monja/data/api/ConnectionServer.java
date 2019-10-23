package com.haerul.monja.data.api;

import com.google.gson.JsonObject;
import com.haerul.monja.utils.Constants;

import javax.inject.Inject;

import retrofit2.Call;

public class ConnectionServer {

    @Inject ApiInterface apiInterface;
    
    public ConnectionServer(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }
    
    public Call<JsonObject> loginCall(JsonObject body) {
        return apiInterface.login(body);
    }

    public Call<JsonObject> getLinkDb(JsonObject head, JsonObject body) {
        return apiInterface.initDB(
                head.get(Constants.SECURITY_KEY).getAsString(),
                body
        );
    }

    public Call<JsonObject> postInspeksi(String head, JsonObject body) {
        return apiInterface.postInspeksi(head, body);
    }
    
    public Call<JsonObject> postBase64Data(String head,JsonObject body) {
        return apiInterface.postBase64Data(head, body);
    }
}
