package com.yknx4.notificationtracker.serializers

import android.app.Notification
import android.content.Context
import android.location.Location
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
class LocationSerializer: JsonSerializer<Location> {
    override fun serialize(src: Location, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val serialized_object = JsonObject()
        serialized_object.addProperty(ACCURACY, src.accuracy)
        serialized_object.addProperty(ALTITULE, src.altitude)
        serialized_object.addProperty(BEARING, src.bearing)
        serialized_object.addProperty(ELAPSED_TIME_NANOS, src.elapsedRealtimeNanos)
        serialized_object.addProperty(LATITUDE, src.latitude)
        serialized_object.addProperty(LONGITUDE, src.longitude)
        serialized_object.addProperty(PROVIDER, src.provider)
        serialized_object.addProperty(SPEED, src.speed)
        serialized_object.addProperty(TIME, src.time)
        serialized_object.add(EXTRAS, context.serialize(src.extras))
        return serialized_object
    }
    companion object {
        val ACCURACY = "accuracy"
        val ALTITULE = "altitude"
        val BEARING = "bearing"
        val ELAPSED_TIME_NANOS = "elapsed_real_time_nanos"
        val LATITUDE = "latitude"
        val LONGITUDE = "longitude"
        val PROVIDER = "provider"
        val SPEED = "speed"
        val TIME = "time"
        val EXTRAS = "extras"
    }
}

