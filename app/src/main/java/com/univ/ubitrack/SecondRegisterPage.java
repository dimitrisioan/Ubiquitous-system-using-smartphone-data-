package com.univ.ubitrack;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SecondRegisterPage extends AppCompatActivity {

    private Button button2;
    private String recruitingTeam, ageRange, gender;
    RadioGroup genderRadioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_second_register_page);

        Bundle bundle = getIntent().getExtras();

        recruitingTeam = bundle.getString("recruitingTeam");
        ageRange = bundle.getString("ageRange");

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                try {
                    gender = getSelectedRadio();
                }catch (Exception e) {
                    Toast.makeText(SecondRegisterPage.this, "Choose your Gender", Toast.LENGTH_SHORT).show();
                }
                if (NetworkService.isNetworkAvailable()) {
                    ThingsBoard thingsBoard = new ThingsBoard(getApplicationContext());
                    thingsBoard.addNewDevice(Integer.parseInt(recruitingTeam), ageRange, gender);
                    openApp();
                    openAppSettings();
                }else {
                    Toast.makeText(SecondRegisterPage.this, "You must be connected to network in order to continue", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void openApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private String getSelectedRadio() {
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();

        radioButton = (RadioButton) findViewById(selectedId);
        return String.valueOf(radioButton.getText()).toLowerCase();
    }
}