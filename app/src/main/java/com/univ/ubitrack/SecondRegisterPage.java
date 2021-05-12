package com.univ.ubitrack;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
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
                    boolean success = addDeviseToDB();
                    if (success) {
                        openApp();
                        openAppSettings();
                    }
                }catch (Exception e) {
                    Toast.makeText(SecondRegisterPage.this, "Choose your Gender", Toast.LENGTH_SHORT).show();
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

    private boolean addDeviseToDB() {
        try {
            DBHelper dbHelper = new DBHelper(SecondRegisterPage.this);
            DeviceModel deviceModel = new DeviceModel(-1, Integer.parseInt(recruitingTeam), ageRange, gender, "device_" + recruitingTeam + "_1", 1);
            dbHelper.deleteAllDevices();
            boolean success = dbHelper.addDevice(deviceModel);
            if (MainActivity.debugging == 1) {
                Log.i("DB", String.valueOf(success));
            }
            return true;
        }catch (Exception e){
            Toast.makeText(SecondRegisterPage.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}