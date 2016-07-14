package com.yknx4.notificationtracker.services

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yknx4.notificationtracker.getTag
import com.yknx4.notificationtracker.serializers.LocationSerializer
import com.yknx4.notificationtracker.serializers.NotificationSerializer
import com.yknx4.notificationtracker.serializers.StatusBarNotificationSerializer

/**
 * Created by yknx4 on 7/13/16.
 */
class NotificationTrackerService : NotificationListenerService() {

    var gson: Gson = Gson()
    var status_notification_serializer:StatusBarNotificationSerializer? = null
    var notification_serializer:NotificationSerializer? = null

    override fun onCreate() {
        status_notification_serializer = StatusBarNotificationSerializer(this)
        notification_serializer = NotificationSerializer(this)
        gson = GsonBuilder()
                .registerTypeAdapter(Location::class.java, LocationSerializer())
                .registerTypeAdapter(StatusBarNotification::class.java, status_notification_serializer)
                .registerTypeAdapter(Notification::class.java, notification_serializer)
                .setPrettyPrinting()
                .create()
        status_notification_serializer!!.tryConnect()
        notification_serializer!!.tryConnect()
        super.onCreate()
    }

    override fun onDestroy() {
        status_notification_serializer?.tryDisconnect()
        notification_serializer?.tryDisconnect()
        super.onDestroy()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.i(getTag(), "**********  onNotificationPosted")
        Log.i(getTag(), "ID :" + sbn.id + "t" + sbn.notification.tickerText + "t" + sbn.packageName)
        val json = gson.toJson(sbn, StatusBarNotification::class.java)
        Log.d(getTag(), json)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.i(getTag(), "********** onNotificationRemoved")
        Log.i(getTag(), "ID :" + sbn.id + "t" + sbn.notification.tickerText + "t" + sbn.packageName)

    }

}
