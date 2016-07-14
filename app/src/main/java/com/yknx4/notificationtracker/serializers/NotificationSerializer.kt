package com.yknx4.notificationtracker.serializers

import android.app.Notification
import android.content.Context
import android.os.Bundle
import android.service.notification.StatusBarNotification
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.yknx4.notificationtracker.toBase64
import java.lang.reflect.Type

/**
 * Created by yknx4 on 7/13/16.
 */
class NotificationSerializer : LocationAwareSerializer(), JsonSerializer<Notification> {
    override fun serialize(src: Notification, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val serialized_object = JsonObject()
        serialized_object.addProperty(TICKER_TEXT, src.tickerText?.toString())
        serialized_object.addProperty(WHEN, src.`when`)
//        serialized_object.addProperty(CATEGORY, src.category)
//        serialized_object.addProperty(COLOR, src.color)
        serialized_object.addProperty(DEFAUlTS, src.defaults)
        serialized_object.addProperty(FLAGS, src.flags)
        serialized_object.addProperty(ICON_LEVEL, src.iconLevel)
        serialized_object.addProperty(PRIORITY, src.priority)
//        serialized_object.addProperty(VISIBILITY, src.visibility)
//        serialized_object.addProperty(ICON_BASE64, src.largeIcon?.toBase64())
        serialized_object.add(EXTRAS, context.serialize(src.extras, Bundle::class.java))
        return serialized_object
    }
    companion object {
        val TICKER_TEXT = "ticker_text"
        val WHEN = "when"
        val CATEGORY = "category"
        val COLOR = "color"
        val DEFAUlTS = "defaults"
        val FLAGS = "flags"
        val ICON_LEVEL = "icon_level"
        val PRIORITY = "priority"
        val VISIBILITY = "visibility"
        val ICON_BASE64 = "large_icon"
        val EXTRAS = "extras"
    }
}

