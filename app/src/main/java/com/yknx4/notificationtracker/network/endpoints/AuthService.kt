package com.yknx4.notificationtracker.network.endpoints

import com.google.gson.JsonElement
import com.yknx4.notificationtracker.API
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by yknx4 on 7/18/16.
 */
interface AuthService {

    @FormUrlEncoded
    @POST(API.SIGNIN_ENDPOINT)
    fun login(@Field("email") email:String, @Field("password") password:String): Call<JsonElement>

}