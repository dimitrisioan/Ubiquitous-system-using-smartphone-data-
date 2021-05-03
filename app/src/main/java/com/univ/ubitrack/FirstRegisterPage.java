package com.univ.ubitrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class FirstRegisterPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button button2;
    private Spinner spinner;
    private Spinner spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_2);

        spinner = findViewById(R.id.spinner);
        String[] recruitingItems = getResources().getStringArray(R.array.recruiting_items);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,recruitingItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner2 = findViewById(R.id.spinner2);
        String[] age_items = getResources().getStringArray(R.array.age_items);
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,age_items);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });
    }
    public void openActivity3(){
        Intent intent = new Intent(this, SecondRegisterPage.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinner){
            String valueFromSpinner = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}