package com.yknx4.notificationtracker.events;

import android.service.notification.StatusBarNotification;

/**
 * Created by yknx4 on 7/14/16.
 */
public class StatusBarNotificationEvent {
    public final String tag;
    public final String action;
    public final String message;
    public final StatusBarNotification notification;

    public StatusBarNotificationEvent(String tag, String action, String message, StatusBarNotification notification) {
        this.tag = tag;
        this.action = action;
        this.message = message;
        this.notification = notification;
    }
}
