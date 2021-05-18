package com.univ.ubitrack;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

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
import java.util.Random;
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
    private int added_thingsboard;

    private Context context;
    private PowerManager powerManager;
    Battery battery;
    Notifications notifications;
    private PlacesClient placesClient;
    private ActivityRecognitionClient mActivityRecognitionClient;

    String[] keys = new String[]{"AIzaSyCuludz6FCrxBJMCRdFQ66DodFYEOq5ymk", "AIzaSyD14w7Mw15hohCdbOyc26kdJzozAPPeIaA"};
    Random random = new Random();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public ScreenEventInfo(Context context, PowerManager powerManager, int display_state) {
        this.display_state = display_state;
        this.context = context;
        this.powerManager = powerManager;
        battery = new Battery(context);
        notifications = new Notifications(context);
        this.system_time = getCurrentTime();
        this.activity = ActivityRecognition.getLastMostProbableActivityString();
        this.activity_conf = ActivityRecognition.getLastMostProbableActivityConf();
        if (NetworkService.isNetworkAvailable()){
            if (checkIfNewLocation()) {
                Places.initialize(context, keys[random.nextInt(keys.length)]);
                placesClient = Places.createClient(context);
                getLocation();
            } else {
                this.location_conf = Constants.LAST_LOCATION_CONF;
                this.location_type = Constants.LAST_LOCATION_TYPE;
                this.location_id = Constants.LAST_LOCATION_ID;
                afterComplete(true);
            }
        }else{
            this.location_conf = (float) 0.0;
            this.location_id = null;
            this.location_type = null;
            afterComplete(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void afterComplete(boolean result){
        this.battery_level = battery.getBatteryPercentage();
        this.battery_status = battery.getBatteryStatus();
        this.device_interactive = getDeviceInteractive();
        this.notifs_active = notifications.getNotificationCount();
        this.network_type = NetworkService.getNetworkType();
        if (NetworkService.isNetworkAvailable()) {
            ThingsBoard.addDeviceTelemetry(device_interactive, display_state, system_time, activity,
                    activity_conf, location_type, location_id, location_conf, battery_level,
                    battery_status, network_type, notifs_active);
            this.added_thingsboard = 1;
        }else{
            this.added_thingsboard = 0;
        }
        Constants.LAST_TIMESTAMP = this.system_time;
        Constants.LAST_ACTIVITY = this.activity;
        Constants.LAST_LOCATION_TYPE = this.location_type;
        Constants.LAST_LOCATION_ID = this.location_id;
        Constants.LAST_LOCATION_CONF = this.location_conf;
        addUsersDataToDB();
        if (NetworkService.isNetworkAvailable()) {
            addRemainingData();
        }
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
            UsersDataModel usersDataModel = new UsersDataModel(-1, this.device_interactive,
                    this.display_state, this.system_time, this.activity, this.activity_conf,
                    this.location_type, this.location_id, this.location_conf, this.battery_level,
                    this.battery_status, this.network_type, this.notifs_active, this.added_thingsboard);
            boolean success = dbHelper.addUsersData(usersDataModel);
            if (MainActivity.debugging == 1) {
                Log.i("DB", String.valueOf(success));
            }
            return true;
        }catch (Exception e){
//            Toast.makeText(context, toString(), Toast.LENGTH_LONG).show();
//            Toast.makeText(context, "An Error Occurred", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void addRemainingData() {
        DBHelper dbHelper = new DBHelper(context);
        ArrayList<UsersDataModel> usersDataModels;
        try {
            usersDataModels = dbHelper.getUsersDataForThingsBoard();
            Log.i("Data", String.valueOf(usersDataModels.size()));
            for (int i = 0; i <= usersDataModels.size() - 1; i++){
                UsersDataModel data = usersDataModels.get(i);
                ThingsBoard.addDeviceTelemetry(data.getDevice_interactive(), data.getDisplay_state(),
                        data.getSystem_time(), data.getActivity(), data.getActivity_conf(),
                        data.getLocation_type(), data.getLocation_id(), data.getLocation_conf(),
                        data.getBattery_level(), data.getBattery_status(), data.getNetwork_type(),
                        data.getNotifs_active());
                boolean success = dbHelper.updateThinsBoardStatus(data.getId());
            }
        } catch (Exception e) {
            if (MainActivity.debugging == 1)
                e.printStackTrace();
        }
    }

    private boolean checkIfNewLocation(){
        String lastTimestamp = Constants.LAST_TIMESTAMP;
        String lastActivity = Constants.LAST_ACTIVITY;
        String lastLocationType = Constants.LAST_LOCATION_TYPE;
        String lastLocationId = Constants.LAST_LOCATION_ID;
        float lastLocationConf = Constants.LAST_LOCATION_CONF;

        if (lastLocationConf != 0.0 && lastLocationId != null && lastLocationType != null){
            if (lastTimestamp != null && lastActivity != null) {
                long currentTimestamp = Long.parseLong(this.system_time);
                long lastTimestampInt = Long.parseLong(lastTimestamp);
                Log.i("timestamps", String.valueOf(currentTimestamp));
                Log.i("timestamps", String.valueOf(lastTimestampInt));
                long diff = currentTimestamp - lastTimestampInt;
                Log.i("Diff", String.valueOf(diff));
                if (diff <= 10 && lastActivity.equals("Still")){
                    return false;
                }else if (diff <= 40 && lastActivity.equals("Still") && this.activity.equals("Still")){
                    return false;
                }else {
                    return true;
                }
            }
        }
        return true;
    }
}
