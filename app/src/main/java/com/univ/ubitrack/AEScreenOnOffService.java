package com.univ.ubitrack;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;


public class AEScreenOnOffService extends Service {
    BroadcastReceiver mReceiver=null;
    private boolean previousScreenState = false;

    @Override
    public void onCreate() {
        super.onCreate();

        // Toast.makeText(getBaseContext(), "Service on create", Toast.LENGTH_SHORT).show();

        // Register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new AEScreenOnOffReceiver();
        registerReceiver(mReceiver, filter);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        boolean screenOn = false;

        try{
            // Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java )
            screenOn = intent.getBooleanExtra("screen_state", false);

        }catch(Exception e){
            Toast.makeText(getBaseContext(), "An error occured", Toast.LENGTH_SHORT).show();
        }

        if(screenOn != previousScreenState) {
            previousScreenState = !previousScreenState;
            if (!screenOn) {
                Context context = getApplicationContext();
                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    ScreenEventInfo eventInfo = new ScreenEventInfo(this, powerManager);
                }
                Toast.makeText(getBaseContext(), "Screen is on", Toast.LENGTH_SHORT).show();
                if (MainActivity.debugging == 1)
                    Log.i("Screen State", "Screen is on");

            } else {
                Context context = getApplicationContext();
                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    ScreenEventInfo eventInfo = new ScreenEventInfo(this, powerManager);
                }
                Toast.makeText(getBaseContext(), "Screen is off", Toast.LENGTH_SHORT).show();
                if (MainActivity.debugging == 1)
                    Log.i("Screen State", "Screen is off");
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i("ScreenOnOff", "Service  destroy");
        Toast.makeText(getBaseContext(), "Service Destroyed", Toast.LENGTH_SHORT).show();
        if(mReceiver != null)
            unregisterReceiver(mReceiver);
//        super.onDestroy();
    }
}