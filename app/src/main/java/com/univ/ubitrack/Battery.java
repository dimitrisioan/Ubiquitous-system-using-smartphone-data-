package com.univ.ubitrack;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

public class Battery{
    private Context context;

    public Battery(Context c) {
        this.context = c;
    }

    public boolean getBatteryStatus() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        if(isCharging){
            if (MainActivity.debugging == 1)
                Log.i("Battery Status ", "Charging");
            return true;
        }
        if (MainActivity.debugging == 1)
            Log.i("Battery Status", "Discharging");
        return false;
    }

    public int getBatteryPercentage() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level * 100 / (float)scale;
        if (MainActivity.debugging == 1)
            Log.i("Battery Level ", String.valueOf(batteryPct) + "%");
        return Math.round(batteryPct);
    }
}