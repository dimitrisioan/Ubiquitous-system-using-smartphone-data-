package com.univ.ubitrack;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.concurrent.TimeUnit;

public class ScreenEventInfo {
    private String device_interactive;
    private int display_state;
    private String system_time;
    private String activity;
    private float activity_conf;
    private String location_type;
    private String location_id;
    private float location_conf;
    private int battery_level;
    private boolean battery_status;
    private String network_type;
    private int notifs_active;

    private Context context;
    private PowerManager powerManager;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public ScreenEventInfo(Context context, PowerManager powerManager) {
        this.context = context;
        this.powerManager = powerManager;
        Battery battery = new Battery(context);
        this.battery_level = battery.getBatteryPercentage();
        this.battery_status = battery.getBatteryStatus();
        this.system_time = getCurrentTime();
        this.device_interactive = getDeviceInteractive();
        Notifications notifications = new Notifications(context);
        this.notifs_active = notifications.getNotificationCount();
    }

    private String getCurrentTime() {
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        if (MainActivity.debugging == 1){
            Log.i("Timestamp ", String.valueOf(timeStamp));
        }
        return String.valueOf(timeStamp);
    }

    private String getDeviceInteractive() {
        boolean isScreenOn = false;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = powerManager.isInteractive();
            if(isScreenOn){
                if (MainActivity.debugging == 1)
                    Log.i("Device Interactive ", "TRUE");
                return "TRUE";
            }
            if (MainActivity.debugging == 1)
                Log.i("Device Interactive ", "FALSE");
            return "FALSE";
        }
        return "unknown";
    }
}
