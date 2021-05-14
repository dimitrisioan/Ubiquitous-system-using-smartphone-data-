package com.univ.ubitrack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_chart, container, false);
        mChart = view.findViewById(R.id.chart2);
        mChart.getDescription().setEnabled(false);
        setData(11);
        mChart.setFitBars(true);



        // Inflate the layout for this fragment
        return view;
    }
    private void setData(int count){
        ArrayList<BarEntry> yVals = new ArrayList<>();
        for(int i=0; i< count; i++){
            float value = (float) (Math.random()*100);
            yVals.add(new BarEntry(i,(int) value));
        }
        BarDataSet set = new BarDataSet(yVals,"Data Set");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);

        BarData data = new BarData(set);
        mChart.setData(data);
        mChart.invalidate();
        mChart.animateY(500);
    }
}

