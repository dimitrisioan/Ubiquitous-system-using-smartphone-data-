package com.univ.ubitrack;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

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
    LocationService locationService;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public ScreenEventInfo(Context context, PowerManager powerManager) {
        this.activity = TransitionReceiver.getLastMostProbableActivityString();
        this.activity_conf = TransitionReceiver.getLastMostProbableActivityConf();
        this.context = context;
        this.powerManager = powerManager;
        locationService = new LocationService(context);
        Battery battery = new Battery(context);
        this.battery_level = battery.getBatteryPercentage();
        this.battery_status = battery.getBatteryStatus();
        this.system_time = getCurrentTime();
        this.device_interactive = getDeviceInteractive();
        Notifications notifications = new Notifications(context);
        this.notifs_active = notifications.getNotificationCount();
        this.network_type = NetworkService.getNetworkType();
        getLocation();
    }

    private void afterComplete(boolean result){
        Log.i("Data", toString());
    }

    public void getLocation() {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.TYPES, Place.Field.ID);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = MainActivity.placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(this::onComplete);
        }
    }

    private void onComplete(Task<FindCurrentPlaceResponse> task) {
        if (task.isSuccessful()) {
            FindCurrentPlaceResponse response = task.getResult();
            PlaceLikelihood place = response.getPlaceLikelihoods().get(0);
            this.location_conf = (float) place.getLikelihood();
            this.location_id = place.getPlace().getId();
            this.location_type = Objects.requireNonNull(place.getPlace().getTypes()).get(0).toString();
            if (MainActivity.debugging == 1)
                Log.i("Location", location_id + ", " + location_type + ", " + location_conf);
//            Maybe it can be done better
            afterComplete(task.isComplete());
        } else {
            Exception exception = task.getException();
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                if (MainActivity.debugging == 1)
                    Log.e("TAG", "Place not found: " + apiException.getStatusCode());
                this.location_conf = 0;
                this.location_id = null;
                this.location_type = null;
                afterComplete(task.isComplete());
            }
        }
    }

    @Override
    public String toString() {
        return "ScreenEventInfo{" +
                "device_interactive='" + device_interactive + '\'' +
                ", display_state=" + display_state +
                ", system_time='" + system_time + '\'' +
                ", activity='" + activity + '\'' +
                ", activity_conf=" + activity_conf +
                ", location_type='" + location_type + '\'' +
                ", location_id='" + location_id + '\'' +
                ", location_conf=" + location_conf +
                ", battery_level=" + battery_level +
                ", battery_status=" + battery_status +
                ", network_type='" + network_type + '\'' +
                ", notifs_active=" + notifs_active +
                ", context=" + context +
                ", powerManager=" + powerManager +
                '}';
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
