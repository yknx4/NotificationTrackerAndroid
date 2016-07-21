package com.yknx4.notificationtracker

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.service.notification.StatusBarNotification
import android.text.TextUtils
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.jaredrummler.android.device.DeviceName
import com.securepreferences.SecurePreferences
import com.yknx4.notificationtracker.events.LoginEvent
import okhttp3.Headers
import retrofit2.Response
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

fun Headers.getLoginEvent(): LoginEvent {
    val event = LoginEvent(
            get(PreferencesFields.ACCESS_TOKEN),
            get(PreferencesFields.CLIENT),
            get(PreferencesFields.TOKEN_TYPE),
            get(PreferencesFields.EXPIRY),
            get(PreferencesFields.UID)
    )
    return event
}

fun Context.loggedOut(): Boolean {
    return SecurePreferences(this).getString(PreferencesFields.ACCESS_TOKEN, "").equals("")
}

fun Any?.getDeviceName(): String {
    val try_devicename = DeviceName.getDeviceName()
    if(try_devicename.isNotBlank()){
        return try_devicename
    }
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    if (model.startsWith(manufacturer)) {
        return model.capitalize()
    }
    return manufacturer.capitalize() + " " + model
}

fun String.capitalize(): String {
    if (TextUtils.isEmpty(this)) {
        return this
    }
    val arr = this.toCharArray()
    var capitalizeNext = true
    var phrase = ""
    for (c in arr) {
        if (capitalizeNext && Character.isLetter(c)) {
            phrase += Character.toUpperCase(c)
            capitalizeNext = false
            continue
        } else if (Character.isWhitespace(c)) {
            capitalizeNext = true
        }
        phrase += c
    }
    return phrase
}