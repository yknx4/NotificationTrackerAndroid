package com.yknx4.notificationtracker

import com.google.android.gms.maps.model.LatLng

/**
 * Created by yknx4 on 7/14/16.
 */
abstract class Constants{
    companion object{
    }
}

abstract class API{
    companion object{
        const val BASE_URL = "https://android-notification-tracker.herokuapp.com/"
//        const val BASE_URL = "http://ea692ad4.ngrok.io/"
        const val API_URL = BASE_URL + "api/v1/"
        const val ECHO_ENDPOINT = "echo"
        const val STATUS_BAR_NOTIFICATION_ENDPOINT = "status_bar_notifications"
        const val AUTH_ENDPOINT = "auth"
        const val SIGNIN_ENDPOINT = AUTH_ENDPOINT + "/sign_in"
    }
}

abstract class HttpStatus{
    companion object{
        const val UNAUTHORIZED = 401
    }
}

abstract class PreferencesFields{
    companion object{
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val ACCESS_TOKEN = "access-token"
        const val CLIENT = "client"
        const val TOKEN_TYPE = "token-type"
        const val EXPIRY = "expiry"
        const val UID = "uid"
        const val LATITUDE = "last-latitue"
        const val LONGITUDE = "last-longitude"
    }
}

abstract class CustomHeaders {
    companion object{
        const val DEVICE_UID = "device-id"
        const val DEVICE_NAME = "device-name"
    }
}

abstract class Map{
    companion object{

    }
}