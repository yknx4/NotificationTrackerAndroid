package com.yknx4.notificationtracker.events;

import com.google.gson.JsonElement;

import retrofit2.Call;

/**
 * Created by yknx4 on 7/19/16.
 */
public class FailedLoginEvent {
    public final Call<JsonElement> call;
    public final Throwable error;

    public FailedLoginEvent(Call<JsonElement> call, Throwable error) {
        this.call = call;
        this.error = error;
    }
}
