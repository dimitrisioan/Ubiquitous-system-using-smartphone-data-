package com.univ.ubitrack;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class MainActivity extends AppCompatActivity {

    public static boolean isPhoneRegisted = true;

    //Initilize variable
    MeowBottomNavigation bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isPhoneRegisted) {
            applicationFragments();
        }else{
            goToGetStarted();
        }
    }

    public void goToGetStarted(){
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
                //Initialize fragment
                Fragment fragment = null;
                //Check conditions
                switch (item.getId()) {
                    case 1:
                        //Home selected
                        fragment = new HomeFragment();
                        break;
                    case 2:
                        //Statistics selected
                        fragment = new StatisticsFragment();
                        break;
                    case 3:
                        fragment = new SettingsFragment();
                        break;
                }
                //Load fragments
                loadFragment(fragment);

            }
        });
        bottomNavigation.show(1,true);
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
}
