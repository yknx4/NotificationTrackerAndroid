package com.yknx4.notificationtracker.serializers

import android.content.Context
import android.os.Build
import android.service.notification.StatusBarNotification

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import java.lang.reflect.Type

/**
 * Created by yknx4 on 7/13/16.
 */
class StatusBarNotificationSerializer : LocationAwareSerializer(), JsonSerializer<StatusBarNotification> {
    override fun serialize(src: StatusBarNotification, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val serialized_object = JsonObject()
        serialized_object.addProperty(NOTIFICATION_ID, src.id)
        serialized_object.addProperty(PACKAGE_NAME, src.packageName)
        serialized_object.addProperty(POST_TIME, src.postTime)
        serialized_object.addProperty(TAG, src.tag)
        serialized_object.addProperty(IS_CLEARABLE, src.isClearable)
        serialized_object.addProperty(IS_ONGOING, src.isOngoing)
        serialized_object.add(NOTIFICATION, context.serialize(src.notification))
        if(location!=null){
            serialized_object.add(LOCATION, context.serialize(location))
        }

        /* Methods not available in every api */

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            serialized_object.add(USER, context.serialize(src.user))  # need to see what to serialize
            serialized_object.addProperty(GROUP_KEY, src.groupKey)
            serialized_object.addProperty(KEY, src.key)
        }
        else{
            serialized_object.addProperty(USER_ID, src.userId)
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            serialized_object.addProperty(IS_GROUP, src.isGroup)
            serialized_object.addProperty(OVERRIDE_GROUP_KEY, src.overrideGroupKey)
        }


        return serialized_object
    }
    companion object {
        val GROUP_KEY = "group_key"
        val NOTIFICATION_ID = "android_notification_id"
        val KEY = "key"
        val OVERRIDE_GROUP_KEY = "override_group_key"
        val PACKAGE_NAME = "package_name"
        val POST_TIME = "post_time_ms"
        val TAG = "tag"
        val USER_ID = "android_user_id"
        val IS_CLEARABLE = "is_clearable"
        val IS_ONGOING = "is_ongoing"
        val IS_GROUP  = "is_group"

        val NOTIFICATION = "notification_attributes"
        val USER = "user"
        val LOCATION = "location_attributes"
    }
}




