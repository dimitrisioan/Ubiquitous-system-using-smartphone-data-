package com.univ.ubitrack;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static androidx.core.content.ContextCompat.startActivity;

public class Notifications {
    private final Context context;
    private static final String TAG = "Notifications";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    public Notifications(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public int getNotificationCount() {
        int count = showNotifications();
        if (MainActivity.debugging == 1)
            Log.i(TAG, String.valueOf(count));
        return count;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public int showNotifications() {
        int count = 0;
        if (isNotificationServiceEnabled()) {
            Log.i(TAG, "Notification enabled -- trying to fetch it");
            count = getNotifications();
        } else {
            Log.i(TAG, "Notification disabled -- Opening settings");
            context.startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
        }
        return count;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public int getNotifications() {
//        Log.i(TAG, "Waiting for MyNotificationService");
        MyNotificationService myNotificationService = MyNotificationService.get();
//        Log.i(TAG, "Active Notifications: [");
        int count = 0;
        for (StatusBarNotification notification : myNotificationService.getActiveNotifications()) {
//            Log.i(TAG, "    " + notification.getPackageName() + " / " + notification.getTag());
            count += 1;
        }
        return count;
    }

    private boolean isNotificationServiceEnabled(){
        String pkgName = context.getPackageName();
        final String allNames = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        if (allNames != null && !allNames.isEmpty()) {
            for (String name : allNames.split(":")) {
                if (context.getPackageName().equals(ComponentName.unflattenFromString(name).getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

}
