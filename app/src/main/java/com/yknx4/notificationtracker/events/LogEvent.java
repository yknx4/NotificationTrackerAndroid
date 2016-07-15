package com.yknx4.notificationtracker.events;

/**
 * Created by yknx4 on 7/15/16.
 */
public class LogEvent {
    public final String message;
    public final String tag;
    public final Throwable t;

    public LogEvent(String tag, String message, Throwable t) {
        this.message = message;
        this.tag = tag;
        this.t = t;
    }
}
