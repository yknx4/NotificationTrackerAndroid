package com.yknx4.notificationtracker.events;

import android.service.notification.StatusBarNotification;

/**
 * Created by yknx4 on 7/14/16.
 */
public class NotificationEvent {
    public final String message;
    public final StatusBarNotification notification;

    public NotificationEvent(String message, StatusBarNotification notification) {
        this.message = message;
        this.notification = notification;
    }
}
