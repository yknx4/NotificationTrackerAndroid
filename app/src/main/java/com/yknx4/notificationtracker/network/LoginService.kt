package com.yknx4.notificationtracker.network

import android.content.Context
import com.google.gson.JsonElement
import com.securepreferences.SecurePreferences
import com.yknx4.lib.yknxtools.models.ContextAware
import com.yknx4.notificationtracker.API
import com.yknx4.notificationtracker.PreferencesFields
import com.yknx4.notificationtracker.events.FailedLoginEvent
import com.yknx4.notificationtracker.events.LoginEvent
import com.yknx4.notificationtracker.getLoginEvent
import com.yknx4.notificationtracker.models.AuthInformation
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

class LoginService : ContextAware, Callback<JsonElement> {

    override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
        val event = FailedLoginEvent(call, null, t)
        EventBus.getDefault().post(event)
    }

    override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
        if(response?.isSuccessful?: false) {
            val event = response?.headers()?.getLoginEvent()
            event?.response = response?.body()
            handleLoginResponse(response)
            if (event != null) EventBus.getDefault().post(event)
        }
        else{
            val event = FailedLoginEvent(call, response, null)
            EventBus.getDefault().post(event)
        }
    }

    fun handleLoginResponse(jsonResponse: Response<JsonElement>?) {
        val response = jsonResponse?.raw()
        if (response!= null && response.isSuccessful) {
            authInformation.edit().setAccessToken(response.header(PreferencesFields.ACCESS_TOKEN, authInformation.accessToken)).setClient(response.header(PreferencesFields.CLIENT, authInformation.client)).setTokenType(response.header(PreferencesFields.TOKEN_TYPE, authInformation.tokenType)).setExpiry(response.header(PreferencesFields.EXPIRY, authInformation.expiry)).setUID(response.header(PreferencesFields.UID, authInformation.uid)).apply()
        }
    }

    private var retrofit: Retrofit

    private var service: AuthService

    private val preferences: SecurePreferences
    private val authInformation: AuthInformation

    constructor(context: Context) : super(context) {
        retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(API.BASE_URL).build()
        service = retrofit.create(AuthService::class.java)
        preferences = SecurePreferences(context)
        authInformation = AuthInformation(preferences)
    }

    fun loginAsync(email:String, password:String){
        service.login(email, password).enqueue(this)
    }

    fun login(email:String, password: String): Response<JsonElement>? {
        val response = service.login(email, password).execute()
        handleLoginResponse(response)
        return response
    }
}