package com.univ.ubitrack;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import static androidx.core.content.ContextCompat.startActivity;

public class MainActivity extends AppCompatActivity {

    public static int isPhoneRegistered = -1;
    Intent serviceIntent = null;
    public static int debugging = 1;
    DeviceModel device;
    //Initilize variable
    MeowBottomNavigation bottomNavigation;

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
        }else{
            goToGetStarted();
            boolean isNotificationServiceRunning = isNotificationServiceRunning();
            if(!isNotificationServiceRunning){
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
        }
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
//        this.getSupportActionBar().hide();

        //Assign variable
        bottomNavigation = findViewById(R.id.bottom_navigation);

        //Add menu items
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_statistics));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_settings));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //Initialize fragment
                Fragment fragment = null;
                //Check conditions
                switch (item.getId()) {
                    case 1:
                        //Statistics selected
                        fragment = new StatisticsFragment();
                        break;
                    case 2:
                        //Home selected
                        fragment = new HomeFragment();
                        break;
                    case 3:
                        //Settings Selected
                        Bundle settingsBundle = new Bundle();
                        if (device.getIsDeviseRegistered() == 1){
                            settingsBundle.putString("recruitedTeam", String.valueOf(device.getRecruitedTeam()));
                            settingsBundle.putString("ageRange", String.valueOf(device.getAgeRange()));
                            settingsBundle.putString("gender", capitalize(device.getGender()));
                            settingsBundle.putString("deviceId", String.valueOf(device.getDevice_id()));
                        }
                        fragment = new SettingsFragment();
                        fragment.setArguments(settingsBundle);
                        break;
                }
                //Load fragments
                loadFragment(fragment);

            }
        });
        bottomNavigation.show(2,true);
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //
            }
        });
    }

    private void loadFragment(Fragment fragment){
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
