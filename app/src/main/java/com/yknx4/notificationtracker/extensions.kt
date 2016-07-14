package com.yknx4.notificationtracker

import android.graphics.Bitmap
import android.util.Base64
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