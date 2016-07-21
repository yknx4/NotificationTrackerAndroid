package com.yknx4.notificationtracker.events;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by yknx4 on 7/19/16.
 */
public class FailedLoginEvent {
    public final Call<JsonElement> call;
    public final  Response<JsonElement> response;
    public final Throwable error;

    public FailedLoginEvent(Call<JsonElement> call, Response<JsonElement> response, Throwable error) {
        this.call = call;
        this.response = response;
        this.error = error;
    }
}
