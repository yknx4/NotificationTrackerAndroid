package com.yknx4.notificationtracker.events;

import com.google.gson.JsonElement;

/**
 * Created by yknx4 on 7/15/16.
 */
public class LoginEvent {
    public final String accessToken;
    public final String client;
    public final String tokenType;
    public final String expiry;
    public final String uid;
    public JsonElement response;

    public LoginEvent(String accessToken, String client, String tokenType, String expiry, String uid) {
        this.accessToken = accessToken;
        this.client = client;
        this.tokenType = tokenType;
        this.expiry = expiry;
        this.uid = uid;

    }
}
