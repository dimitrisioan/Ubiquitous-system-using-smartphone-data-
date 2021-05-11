package com.univ.ubitrack;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    public static PlacesClient placesClient;
    //Initialize variables
    private NetworkCapabilities network_temp;
    public static int isPhoneRegistered = -1;
    Intent serviceIntent = null;
    public static int debugging = 1;
    DeviceModel device;
    MeowBottomNavigation bottomNavigation;
    private ActivityRecognitionClient mActivityRecognitionClient;
    public static final String DETECTED_ACTIVITY = ".DETECTED_ACTIVITY";

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper dbHelper = new DBHelper(MainActivity.this);
        device = (DeviceModel) dbHelper.getDevice();
        Battery.CurrentPlaceActivity currentPlaceActivity = new Battery.CurrentPlaceActivity();

        Places.initialize(getApplicationContext(), "AIzaSyCuludz6FCrxBJMCRdFQ66DodFYEOq5ymk");
        placesClient = Places.createClient(this);

        if (device != null) {
            isPhoneRegistered = device.getIsDeviseRegistered();
        }

        if (isPhoneRegistered == 1) {
            applicationFragments();
            startAEScreenOnOffService();
            checkForLocationPermision();
            startNetworkService();
            mActivityRecognitionClient = ActivityRecognition.getClient(MainActivity.this);

            requestUpdatesHandler();
        }else{
            goToGetStarted();
            boolean isNotificationServiceRunning = isNotificationServiceRunning();
            if(!isNotificationServiceRunning){
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
        }
    }

    public PendingIntent getActivityDetectionPendingIntent(Context context) {
        Intent intent = new Intent(context, TransitionReceiver.class);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void requestUpdatesHandler() {
//        ActivityTransitionRequest request = buildTransitionRequest();
        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(0, getActivityDetectionPendingIntent(MainActivity.this));
        task.addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(getApplicationContext(), "Recognition Client Initialized Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
        task.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to initialize Recognition Client", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void startNetworkService() {
        Context context = getApplicationContext();
        NetworkService network = new NetworkService(context);
    }

    private void checkForLocationPermision() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LocationService.MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION);
        }
//        Context context = getApplicationContext();
//        LocationService locationSevice = new LocationService(context);
    }

    private boolean isNotificationServiceRunning() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners =
                Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }

    private void startAEScreenOnOffService(){
        Context context = getApplicationContext();
        if (serviceIntent == null) {
            serviceIntent = new Intent(MainActivity.this, AEScreenOnOffService.class);
            startService(serviceIntent);
        }
    }

    private void goToGetStarted(){
        Intent intent = new Intent(this, WelcomePage.class);
        startActivity(intent);
    }

    private void applicationFragments() {
        setContentView(R.layout.activity_get_started_4);
        //Assign variable
        bottomNavigation = findViewById(R.id.bottom_navigation);

        //Add menu items
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_statistics));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_settings));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
            }
        });

        bottomNavigation.show(2, true);
        loadFragment(new HomeFragment());
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                //Check conditions
                if (item.getId() == 1) {
                    //Statistics selected
                    fragment = new StatisticsFragment();
                }
                if (item.getId() == 2) {
                    //Home selected
                    fragment = new HomeFragment();
                }
                if (item.getId() == 3) {
                    //Settings Selected
                    fragment = new SettingsFragment();
                }
                Bundle settingsBundle = new Bundle();
                if (device.getIsDeviseRegistered() == 1) {
                    settingsBundle.putString("recruitedTeam", String.valueOf(device.getRecruitedTeam()));
                    settingsBundle.putString("ageRange", String.valueOf(device.getAgeRange()));
                    settingsBundle.putString("gender", capitalize(device.getGender()));
                    settingsBundle.putString("deviceId", String.valueOf(device.getDevice_id()));
                }
                fragment.setArguments(settingsBundle);
                //Load fragments
                loadFragment(fragment);
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });
    }

    private void loadFragment(Fragment fragment){
        //LOAD YOUR FRAGMENTS
        //Replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
