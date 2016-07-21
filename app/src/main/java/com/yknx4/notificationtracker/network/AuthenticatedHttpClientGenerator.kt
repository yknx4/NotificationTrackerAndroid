package com.yknx4.notificationtracker.network

import android.content.Context
import com.securepreferences.SecurePreferences
import com.yknx4.lib.yknxtools.device.getDeviceUUID
import com.yknx4.lib.yknxtools.models.ContextAware
import com.yknx4.notificationtracker.CustomHeaders
import com.yknx4.notificationtracker.PreferencesFields
import com.yknx4.notificationtracker.getLoginEvent
import okhttp3.*
import com.yknx4.notificationtracker.models.AuthInformation

import java.io.IOException

/**
 * Created by yknx4 on 7/19/16.
 */
class AuthenticatedHttpClientGenerator internal constructor(context: Context) : ContextAware(context) {

    private val mPreferences: SecurePreferences
    private val authInformation: AuthInformation

    init {
        mPreferences = SecurePreferences(context)
        authInformation = AuthInformation(mPreferences)
    }

    val authenticatedClient: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(AuthenticationInterceptor())
            builder.authenticator(Authenticator())
            return builder.build()
        }

    fun handleNewTokenResponse(response: Response) {
        if (response.isSuccessful) {
            authInformation.edit().setAccessToken(response.header(PreferencesFields.ACCESS_TOKEN, authInformation.accessToken)).setClient(response.header(PreferencesFields.CLIENT, authInformation.client)).setTokenType(response.header(PreferencesFields.TOKEN_TYPE, authInformation.tokenType)).setExpiry(response.header(PreferencesFields.EXPIRY, authInformation.expiry)).setUID(response.header(PreferencesFields.UID, authInformation.uid)).apply()
        }
    }

    private fun getProperty(property: String): String {
        return mPreferences.getString(property, "")
    }

    internal inner class Authenticator : okhttp3.Authenticator {
        @Throws(IOException::class)
        override fun authenticate(route: Route, response: Response): Request? {
            val loginResponse = LoginService(context).login(authInformation.email, authInformation.password)
            if(loginResponse!=null && loginResponse.isSuccessful) {
                val event = loginResponse.headers().getLoginEvent()
                return response
                        .request()
                        .newBuilder()
                        .addHeader(PreferencesFields.UID, event.uid)
                        .addHeader(PreferencesFields.EXPIRY, event.expiry)
                        .addHeader(PreferencesFields.ACCESS_TOKEN, event.accessToken)
                        .addHeader(PreferencesFields.CLIENT, event.client)
                        .build()
            }
            else{
                return response.request()
            }
        }
    }

    // TODO: ADD DEVICE_NAME TO HEADERS
    internal inner class AuthenticationInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            // Add authorization header with updated authorization value to intercepted request
            val authorisedRequest = originalRequest
                    .newBuilder()
                    .header(CustomHeaders.DEVICE_UID, context.getDeviceUUID().toString())
                    .header(PreferencesFields.ACCESS_TOKEN, authInformation.accessToken)
                    .header(PreferencesFields.CLIENT, authInformation.client)
                    .header(PreferencesFields.TOKEN_TYPE, authInformation.tokenType)
                    .header(PreferencesFields.EXPIRY, authInformation.expiry)
                    .header(PreferencesFields.UID, authInformation.uid)
                    .build()
            val response = chain.proceed(authorisedRequest)

            handleNewTokenResponse(response)

            return response
        }
    }



}
