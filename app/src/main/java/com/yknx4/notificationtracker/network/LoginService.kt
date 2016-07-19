package com.yknx4.notificationtracker.network

import com.google.gson.JsonElement
import com.yknx4.notificationtracker.API
import com.yknx4.notificationtracker.events.FailedLoginEvent
import com.yknx4.notificationtracker.events.LoginEvent
import com.yknx4.notificationtracker.getLoginEvent
import com.yknx4.notificationtracker.network.endpoints.AuthService
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by yknx4 on 7/19/16.
 */


//TODO: This class should handle all the logic to add the login data to the secure preferences

class LoginService : Callback<JsonElement> {
    override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
        val event = FailedLoginEvent(call, t)
        EventBus.getDefault().post(event)
    }

    override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
        val event = response?.headers()?.getLoginEvent()
        event?.response = response?.body()
        if(event!=null) EventBus.getDefault().post(event)
    }

    private var retrofit: Retrofit

    private var service: AuthService

    constructor(){
        retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(API.BASE_URL).build()
        service = retrofit.create(AuthService::class.java)
    }

    fun loginAsync(email:String, password:String){
        service.login(email, password).enqueue(this)
    }

    fun login(email:String, password: String): Response<JsonElement>? {
        return service.login(email, password).execute()
    }
}