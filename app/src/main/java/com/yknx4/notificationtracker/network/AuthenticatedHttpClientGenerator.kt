package com.yknx4.notificationtracker.network

import android.content.Context

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.securepreferences.SecurePreferences
import com.yknx4.lib.yknxtools.models.ContextAware
import com.yknx4.notificationtracker.PreferencesFields
import com.yknx4.notificationtracker.getLoginEvent

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

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

    fun handleLoginResponse(response: Response) {
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
            val loginResponse = LoginService().login(authInformation.email, authInformation.password)
            if(loginResponse!=null && loginResponse.isSuccessful) {
                val event = loginResponse!!.headers().getLoginEvent()
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

    internal inner class AuthenticationInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            // Add authorization header with updated authorization value to intercepted request
            val authorisedRequest = originalRequest.newBuilder().header(PreferencesFields.ACCESS_TOKEN, authInformation.accessToken).header(PreferencesFields.CLIENT, authInformation.client).header(PreferencesFields.TOKEN_TYPE, authInformation.tokenType).header(PreferencesFields.EXPIRY, authInformation.expiry).header(PreferencesFields.UID, authInformation.uid).build()
            val response = chain.proceed(authorisedRequest)

            handleLoginResponse(response)

            return response
        }
    }

    inner class AuthInformation(private val sharedPreferences: SecurePreferences) {

        private fun getProperty(property: String): String {
            return sharedPreferences.getString(property, "")
        }

        val email: String
            get() = getProperty(PreferencesFields.EMAIL)

        val password: String
            get() = getProperty(PreferencesFields.PASSWORD)

        val accessToken: String
            get() = getProperty(PreferencesFields.ACCESS_TOKEN)

        val client: String
            get() = getProperty(PreferencesFields.CLIENT)

        val tokenType: String
            get() = getProperty(PreferencesFields.TOKEN_TYPE)

        val expiry: String
            get() = getProperty(PreferencesFields.EXPIRY)

        val uid: String
            get() = getProperty(PreferencesFields.UID)

        fun edit(): Setter {
            return Setter(mPreferences.edit())
        }

        inner class Setter internal constructor(private val _editor: SecurePreferences.Editor) {

            private fun setProperty(property: String, value: Any): Setter {
                _editor.putString(property, value.toString())
                return this
            }

            fun setEmail(value: String): Setter {
                return setProperty(PreferencesFields.EMAIL, value)
            }

            fun setPassword(value: String): Setter {
                return setProperty(PreferencesFields.PASSWORD, value)
            }

            fun setAccessToken(value: String): Setter {
                return setProperty(PreferencesFields.ACCESS_TOKEN, value)
            }

            fun setClient(value: String): Setter {
                return setProperty(PreferencesFields.CLIENT, value)
            }

            fun setTokenType(value: String): Setter {
                return setProperty(PreferencesFields.TOKEN_TYPE, value)
            }

            fun setExpiry(value: String): Setter {
                return setProperty(PreferencesFields.EXPIRY, value)
            }

            fun setUID(value: String): Setter {
                return setProperty(PreferencesFields.UID, value)
            }

            fun apply() {
                _editor.apply()
            }

            fun commit(): Boolean {
                return _editor.commit()
            }

        }

    }

}
