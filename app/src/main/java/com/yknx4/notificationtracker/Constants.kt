package com.yknx4.notificationtracker

/**
 * Created by yknx4 on 7/14/16.
 */
abstract class Constants{
    companion object{
        val API_URL = "http://10017f58.ngrok.io"

    }
}

abstract class API{
    companion object{
        const val BASE_URL = "https://android-notification-tracker.herokuapp.com/"
        const val API_URL = BASE_URL + "api/v1/"
        const val ECHO_ENDPOINT = "echo"
        const val STATUS_BAR_NOTIFICATION_ENDPOINT = "status_bar_notifications"
        const val AUTH_ENDPOINT = "auth"
        const val SIGNIN_ENDPOINT = AUTH_ENDPOINT + "/sign_in"
    }
}