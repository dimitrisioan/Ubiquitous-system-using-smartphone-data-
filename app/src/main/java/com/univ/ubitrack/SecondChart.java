package com.univ.ubitrack;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class SecondChart extends Fragment {
    BarChart mChart;

    public SecondChart() {
        // Required empty public constructor
    }


    public static SecondChart newInstance(String param1, String param2) {
        SecondChart fragment = new SecondChart();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_chart, container, false);
        mChart = view.findViewById(R.id.chart2);
        mChart.getDescription().setEnabled(false);
        setData();
        mChart.setFitBars(true);



        // Inflate the layout for this fragment
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setData(){
        ArrayList<BarEntry> yVals = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(getContext());
        ArrayList<String> dates = Utilities.getLastSevenDays();
        ArrayList<EventsPerDay> eventsPerDays = dbHelper.getEventsForADay(dates);

        for(int i=1; i < 8; i++){
            EventsPerDay eventsPerDay = eventsPerDays.get(i-1);
            yVals.add(new BarEntry(i,(int) eventsPerDay.getCount()));
        }
        BarDataSet set = new BarDataSet(yVals,"");
        set.setColors(ColorTemplate.rgb("2FCC76"));
        set.setDrawValues(true);

        BarData data = new BarData(set);
        mChart.setData(data);
        mChart.invalidate();
        mChart.animateY(500);
    }
}

