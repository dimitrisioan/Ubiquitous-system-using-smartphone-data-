package com.univ.ubitrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class FirstRegisterPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner sp_recruited_team;
    Spinner sp_age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_register_page);

        sp_recruited_team = findViewById(R.id.sp_recruited_team);
        String[] recruitingItems = getResources().getStringArray(R.array.recruiting_items);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, recruitingItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_recruited_team.setAdapter(adapter);

        sp_age = findViewById(R.id.sp_age);
        String[] age_items = getResources().getStringArray(R.array.age_items);
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, age_items);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_age.setAdapter(adapter2);

        Button first_next_btn = (Button) findViewById(R.id.first_next_btn );
        first_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSecondRegisterPage();
            }
        });
    }

    public void goToSecondRegisterPage() {
        Intent intent = new Intent(this, SecondRegisterPage.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner) {
            String valueFromSpinner = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}