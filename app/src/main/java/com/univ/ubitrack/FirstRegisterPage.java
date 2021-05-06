package com.univ.ubitrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class FirstRegisterPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner sp_recruited_team;
    Spinner sp_age;
    private String RECRUITED_TEAM_SPINNER_DATA = "";
    private String AGE_RANGE_SPINNER_DATA = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_first_register_page);

        sp_recruited_team = findViewById(R.id.sp_recruited_team);
        String[] recruitingItems = getResources().getStringArray(R.array.recruiting_items);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, recruitingItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_recruited_team.setAdapter(adapter);
        sp_recruited_team.setSelection(7);

        sp_recruited_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RECRUITED_TEAM_SPINNER_DATA = adapterView.getItemAtPosition(i).toString();
                Log.i("Team", RECRUITED_TEAM_SPINNER_DATA);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_age = findViewById(R.id.sp_age);
        String[] age_items = getResources().getStringArray(R.array.age_items);
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, age_items);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_age.setAdapter(adapter2);

        sp_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AGE_RANGE_SPINNER_DATA = adapterView.getItemAtPosition(i).toString();
                Log.i("Age", AGE_RANGE_SPINNER_DATA);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button first_next_btn = (Button) findViewById(R.id.first_next_btn );
        first_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSecondRegisterPage(RECRUITED_TEAM_SPINNER_DATA, AGE_RANGE_SPINNER_DATA);
            }
        });
    }

    public void goToSecondRegisterPage(String recruitingTeam, String ageRange) {
        Intent intent = new Intent(this, SecondRegisterPage.class);
        Bundle bundle = new Bundle();
        bundle.putString("recruitingTeam", recruitingTeam);
        bundle.putString("ageRange", ageRange);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}