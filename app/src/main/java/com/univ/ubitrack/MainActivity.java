package com.univ.ubitrack;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    ThingsBoard thingsBoard;
    public static int isPhoneRegistered = -1;
    public static int debugging = 1;
    Intent serviceIntent = null;
    DeviceModel device;
    MeowBottomNavigation bottomNavigation;
    private ActivityRecognitionClient mActivityRecognitionClient;
//    static SharedPreferences switch_home;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper dbHelper = new DBHelper(MainActivity.this);
        device = (DeviceModel) dbHelper.getDevice();
        startNetworkService();

        if (NetworkService.isNetworkAvailable())
            thingsBoard = new ThingsBoard(MainActivity.this);

        if (device != null) {
            isPhoneRegistered = device.getIsDeviseRegistered();
        }

        if (isPhoneRegistered == 1) {
            applicationFragments();

            startAEScreenOnOffService();
            mActivityRecognitionClient = com.google.android.gms.location.ActivityRecognition.getClient(MainActivity.this);
            requestUpdatesHandler();
            if (NetworkService.isNetworkAvailable())
                thingsBoard.getDeviceId(device.getDevice_id());
            Constants.RECORD_COUNT = dbHelper.getUserCount();
        } else {
            goToGetStarted();
            boolean isNotificationServiceRunning = isNotificationServiceRunning();
            if (!isNotificationServiceRunning) {
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
        }
    }

    public void openAppSettings() {
        try {
            //Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            String packageName = "com.univ.ubitrack";
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        } catch ( ActivityNotFoundException e ) {
            //e.printStackTrace();

            //Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivity(intent);

        }
    }

    public PendingIntent getActivityDetectionPendingIntent(Context context) {
        Intent intent = new Intent(context, ActivityRecognition.class);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void requestUpdatesHandler() {
//        ActivityTransitionRequest request = buildTransitionRequest();
        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(0, getActivityDetectionPendingIntent(MainActivity.this));
        task.addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
//                        Toast.makeText(getApplicationContext(), "Recognition Client Initialized Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
        task.addOnFailureListener(
                new OnFailureListener() {
                    @RequiresApi(api = Build.VERSION_CODES.Q)
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

    private boolean isNotificationServiceRunning() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners =
                Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }

    public void startAEScreenOnOffService() {
        Context context = getApplicationContext();
        if (serviceIntent == null) {
            serviceIntent = new Intent(MainActivity.this, AEScreenOnOffService.class);
            startService(serviceIntent);
        }
    }

    private void goToGetStarted() {
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

    private void loadFragment(Fragment fragment) {
        //LOAD YOUR FRAGMENTS
        //Replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
