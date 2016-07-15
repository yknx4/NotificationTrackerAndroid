package com.yknx4.notificationtracker

/**
 * Created by yknx4 on 7/14/16.
 */
abstract class Constants{
    companion object{
        val API_URL = "http://192.168.0.90"

    }
}

abstract class API{
    companion object{
        const val BASE_URL = "https://android-notification-tracker.herokuapp.com/"
        const val API_URL = BASE_URL + "api/v1/"
        const val ECHO_ENDPOINT = "echo"
        const val STATUS_BAR_NOTIFICATION_ENDPOINT = "status_bar_notifications"
    }
}