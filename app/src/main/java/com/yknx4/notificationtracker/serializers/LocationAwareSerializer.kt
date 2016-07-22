package com.yknx4.notificationtracker.serializers

import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices

import com.google.gson.JsonSerializer

import java.lang.ref.WeakReference


/**
 * Created by yknx4 on 7/13/16.
 */
abstract class LocationAwareSerializer : DeviceAwareSerializer() {
    companion object{
        private var _location:Location? = null
        var location: Location?
            get() {
                synchronized(this){
                    return _location
                }
            }
            set(new_location:Location?) {
                synchronized(this){
                    _location = new_location
                }
            }
    }
}
