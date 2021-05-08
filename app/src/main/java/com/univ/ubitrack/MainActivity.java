package com.univ.ubitrack;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    //Initialize variables
    private NetworkCapabilities network_temp;
    public static int isPhoneRegistered = -1;
    Intent serviceIntent = null;
    public static int debugging = 1;
    DeviceModel device;
    MeowBottomNavigation bottomNavigation;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper dbHelper = new DBHelper(MainActivity.this);

        device = (DeviceModel) dbHelper.getDevise();

        if (device != null) {
            isPhoneRegistered = device.getIsDeviseRegistered();
        }

        if (isPhoneRegistered == 1) {
            applicationFragments();
            startAEScreenOnOffService();
            startLocationService();
            startNetworkService();
        }else{
            goToGetStarted();
            boolean isNotificationServiceRunning = isNotificationServiceRunning();
            if(!isNotificationServiceRunning){
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
        }
    }

    public void startNetworkService() {
        Context context = getApplicationContext();
        NetworkService network = new NetworkService(context);
    }

    private void startLocationService() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LocationService.MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION);
        }
        Context context = getApplicationContext();
        //LocationService locationSevice = new LocationService(context);
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
