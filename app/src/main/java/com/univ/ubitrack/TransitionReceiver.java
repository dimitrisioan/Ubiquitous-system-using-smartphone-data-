package com.univ.ubitrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class TransitionReceiver extends BroadcastReceiver {
    final String KEY_LAST_ACTIVITY_TYPE = "lastActivityType";
    private int lastActivityType = -1;
    public static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 100;

    public static String getActivityString(Context context, int detectedActivityType) {
        Resources resources = context.getResources();
        switch (detectedActivityType) {
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.vehicle);
            default:
                return resources.getString(R.string.unknown_activity);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Hi", "test");
        if (ActivityTransitionResult.hasResult(intent)) {
            ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
            for (ActivityTransitionEvent event : result.getTransitionEvents()) {
                Toast.makeText(context, getActivityString(context, event.getActivityType()), Toast.LENGTH_SHORT).show();
            }
        }
        if (ActivityRecognitionResult.hasResult(intent)) {

            //If data is available, then extract the ActivityRecognitionResult from the Intent//
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            SharedPreferences sharedpreferences = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
            lastActivityType = sharedpreferences.getInt(KEY_LAST_ACTIVITY_TYPE, -1);

            //Get an array of DetectedActivity objects//
            ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

            for (DetectedActivity resultReceived : detectedActivities) {
                if (resultReceived.getConfidence() > 75) {

                    if (lastActivityType != resultReceived.getType()) {
                        lastActivityType = resultReceived.getType();
                        Log.i("LastActivity", String.valueOf(lastActivityType));
                        Toast.makeText(context, getActivityString(context, lastActivityType), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}