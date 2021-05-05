package com.univ.ubitrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SecondRegisterPage extends AppCompatActivity {

    private Button button2;
    private String recruitingTeam, ageRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_register_page);

        Bundle bundle = getIntent().getExtras();

        recruitingTeam = bundle.getString("recruitingTeam");
        ageRange = bundle.getString("ageRange");
        Log.i("Data", recruitingTeam + ", " + ageRange);

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivity4();
            }
        });
    }
    public void openActivity4(){
        MainActivity.isPhoneRegistered = !MainActivity.isPhoneRegistered;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}