package com.univ.ubitrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome_page);

        Button get_started_btn = (Button) findViewById(R.id.get_started_btn);
        get_started_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkService.isNetworkAvailable()) {
                    goToFirstRegisterPage();
                }else {
                    Toast.makeText(WelcomePage.this, "You must be connected to network in order to continue", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToFirstRegisterPage() {
        Intent intent = new Intent(this, FirstRegisterPage.class);
        startActivity(intent);
    }
}

