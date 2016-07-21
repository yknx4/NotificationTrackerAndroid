package com.yknx4.notificationtracker.events;

import android.location.Location;

/**
 * Created by yknx4 on 7/21/16.
 */
public class LocationChangedEvent {
    public final Location newLocation;

    public LocationChangedEvent(Location newLocation) {
        this.newLocation = newLocation;
    }
}
