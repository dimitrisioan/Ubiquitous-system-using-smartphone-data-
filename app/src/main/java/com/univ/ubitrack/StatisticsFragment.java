package com.univ.ubitrack;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;


public class StatisticsFragment extends Fragment {
    private Spinner sp_chart;
    private String CHART_SPINNER_DATA = "";
    public StatisticsFragment() {
        // Required empty public constructor
    }


    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sp_chart = view.findViewById(R.id.sp_chart);
        String[] chartItems = getResources().getStringArray(R.array.chart_items);
        ArrayAdapter adapter3 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, chartItems);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_chart.setAdapter(adapter3);

        sp_chart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CHART_SPINNER_DATA = adapterView.getItemAtPosition(i).toString();
                ((TextView) view).setTextColor(Color.BLACK);
                Log.i("Chart", CHART_SPINNER_DATA);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }



}