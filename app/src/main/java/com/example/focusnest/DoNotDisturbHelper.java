package com.example.focusnest;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

public class DoNotDisturbHelper {

    public static boolean isDoNotDisturbOn(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return notificationManager.getCurrentInterruptionFilter() !=
                    NotificationManager.INTERRUPTION_FILTER_ALL;
        }
        return false;
    }

    public static void setDoNotDisturb(Context context, boolean enable) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if we have permission
            if (!notificationManager.isNotificationPolicyAccessGranted()) {
                // Open settings to grant permission
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                context.startActivity(intent);
                return;
            }

            if (enable) {
                notificationManager.setInterruptionFilter(
                        NotificationManager.INTERRUPTION_FILTER_NONE);
            } else {
                notificationManager.setInterruptionFilter(
                        NotificationManager.INTERRUPTION_FILTER_ALL);
            }
        }
        NotificationBlock.setBlockingEnabled(enable);
    }
}
