package com.example.focusnest;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationBlock extends NotificationListenerService { // notification listener service

    private static boolean isBlockingEnabled = false;

    public static void setBlockingEnabled(boolean enabled) {
        isBlockingEnabled = enabled; // enable dnd
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (isBlockingEnabled) {
            // Cancel the notification
            cancelNotification(sbn.getKey());
        }
    }
}