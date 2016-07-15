package com.yknx4.notificationtracker

import android.util.Log
import com.yknx4.notificationtracker.events.LogEvent
import com.yknx4.notificationtracker.events.StatusBarNotificationEvent
import org.greenrobot.eventbus.EventBus

/**
 * Created by yknx4 on 7/15/16.
 */

class NotificationLogger {

    companion object{
        fun d(tag:String, message:String, t:Throwable?=null){
            Log.d(tag, message, t)
            EventBus.getDefault().post(LogEvent(tag, message, t))
        }
    }
}