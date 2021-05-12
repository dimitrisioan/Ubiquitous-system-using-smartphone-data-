package com.univ.ubitrack;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
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
    private String battery_status;
    private String network_type;
    private int notifs_active;

    private Context context;
    private PowerManager powerManager;
    Battery battery;
    LocationService locationService;
    Notifications notifications;
    private PlacesClient placesClient;
    private ActivityRecognitionClient mActivityRecognitionClient;

    private static final int LONG_DELAY = 10500; // 3.5 seconds
    private static final int SHORT_DELAY = 2000; // 2 seconds


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public ScreenEventInfo(Context context, PowerManager powerManager, int display_state) {
        this.display_state = display_state;
        this.context = context;
        this.powerManager = powerManager;
        locationService = new LocationService(context);
        battery = new Battery(context);
        notifications = new Notifications(context);
        Places.initialize(context, "AIzaSyCuludz6FCrxBJMCRdFQ66DodFYEOq5ymk");
        placesClient = Places.createClient(context);
        getLocation();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void afterComplete(boolean result){
        this.activity = TransitionReceiver.getLastMostProbableActivityString();
        this.activity_conf = TransitionReceiver.getLastMostProbableActivityConf();
        this.battery_level = battery.getBatteryPercentage();
        this.battery_status = battery.getBatteryStatus();
        this.system_time = getCurrentTime();
        this.device_interactive = getDeviceInteractive();
        this.notifs_active = notifications.getNotificationCount();
        this.network_type = NetworkService.getNetworkType();
        addUsersDataToDB();
//        Log.i("Data", toString());

    }

    public void getLocation() {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.TYPES, Place.Field.ID);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = this.placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(this::onComplete);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
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
                Log.e("ApiException", String.valueOf(apiException));
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

    private boolean addUsersDataToDB() {
        try {
            DBHelper dbHelper = new DBHelper(context);
            UsersDataModel usersDataModel = new UsersDataModel(-1, this.device_interactive, this.display_state, this.system_time,
                    this.activity, this.activity_conf, this.location_type, this.location_id, this.location_conf,
                    this.battery_level, this.battery_status, this.network_type, this.notifs_active);
            boolean success = dbHelper.addUsersData(usersDataModel);
            if (MainActivity.debugging == 1) {
                Log.i("DB", String.valueOf(success));
            }
            Toast.makeText(context, toString(), Toast.LENGTH_LONG).show();
            return true;
        }catch (Exception e){
//            Toast.makeText(context, toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(context, "An Error Occurred", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
