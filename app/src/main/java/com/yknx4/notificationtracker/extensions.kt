package com.yknx4.notificationtracker

import android.graphics.Bitmap
import android.service.notification.StatusBarNotification
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.io.ByteArrayOutputStream

/**
 * Created by yknx4 on 7/13/16.
 */

fun Any.getTag(): String { return javaClass.simpleName }

fun Bitmap.toBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream();
    compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
    val byteArray = byteArrayOutputStream.toByteArray();
    return Base64.encodeToString(byteArray, Base64.DEFAULT);
}

fun StatusBarNotification.toJson(gson:Gson = Gson()): String {
    return gson.toJson(this, StatusBarNotification::class.java)
}

fun StatusBarNotification.toJsonObject(gson:Gson = Gson(), root:Boolean = true): JsonElement {
    var result = gson.toJsonTree(this)
    if(root){
        val root = JsonObject()
        root.add("status_bar_notification", result)
        result = root
    }
    return result
}