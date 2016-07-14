package com.yknx4.notificationtracker.network.endpoints;

import com.yknx4.notificationtracker.API;
import com.yknx4.notificationtracker.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by yknx4 on 7/14/16.
 */

public interface EchoService {
    @GET(API.ECHO_ENDPOINT)
    Call<String> index();

    @POST(API.ECHO_ENDPOINT)
    Call<String> create(@Body String body);

}
