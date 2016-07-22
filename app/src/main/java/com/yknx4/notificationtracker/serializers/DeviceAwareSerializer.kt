package com.yknx4.notificationtracker.serializers

import android.location.Location
import java.util.*

/**
 * Created by yknx4 on 7/18/16.
 */

abstract class DeviceAwareSerializer{
    companion object{
        private var _deviceUuid: UUID? = null
        var deviceUUid: UUID?
            get() {
                synchronized(this){
                    return _deviceUuid
                }
            }
            set(new_location: UUID?) {
                synchronized(this){
                    _deviceUuid = new_location
                }
            }
    }
}