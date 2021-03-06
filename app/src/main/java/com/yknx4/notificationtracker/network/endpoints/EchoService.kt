package com.yknx4.notificationtracker.network.endpoints

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.yknx4.notificationtracker.API
import com.yknx4.notificationtracker.Constants

import retrofit2.Call
import retrofit2.http.*

/**
 * Created by yknx4 on 7/14/16.
 */

interface EchoService {
    @GET(API.ECHO_ENDPOINT)
    fun index(): Call<String>

    @Headers("Content-type: application/json")
    @POST(API.ECHO_ENDPOINT)
    fun create(@Body body: JsonElement): Call<JsonElement>

}
