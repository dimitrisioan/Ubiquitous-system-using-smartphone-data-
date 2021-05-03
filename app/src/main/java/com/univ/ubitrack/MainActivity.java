package com.univ.ubitrack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final boolean isPhoneRegisted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isPhoneRegisted) {
            goToHome();
        }else{
            goToGetStarted();
        }
    }

    public void goToGetStarted(){
        Intent intent = new Intent(this, GetStartedActivityPage1.class);
        startActivity(intent);
    }

    public void goToHome(){
        Intent intent = new Intent(this, GetStartedActivityPage4.class);
        startActivity(intent);
    }
}
