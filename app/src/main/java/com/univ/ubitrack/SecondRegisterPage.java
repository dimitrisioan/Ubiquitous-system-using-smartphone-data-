package com.univ.ubitrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
            @Override
            public void onClick(View v) {
                try {
                    gender = getSelectedRadio();
                    boolean success = addDeviseToDB();
                    if (success) {
                        openActivity4();
                    }
                }catch (Exception e) {
                    Toast.makeText(SecondRegisterPage.this, "Choose your Gender", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void openActivity4(){
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