package com.univ.ubitrack;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class FirstChart extends Fragment {
    PieChart pieChart;


    public FirstChart() {
        // Required empty public constructor
    }


    public static FirstChart newInstance(String param1, String param2) {
        FirstChart fragment = new FirstChart();
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
        View view = inflater.inflate(R.layout.fragment_first_chart, container, false);
        pieChart = view.findViewById(R.id.chart1);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        //pieChart.getExtraOffset(5, 10, 5, 5);.
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        DBHelper dbHelper = new DBHelper(getContext());
        ArrayList<ActivityCounter> activityCounters = dbHelper.groupByActivity();

        ArrayList<PieEntry> yValues = new ArrayList<>();
        for (int i = 0; i <= activityCounters.size() - 1; i++ ){
            ActivityCounter activityCounter = (ActivityCounter) activityCounters.get(i);
            yValues.add(new PieEntry(activityCounter.getCount(),activityCounter.getActivity_type()));
        }


        PieDataSet dataset = new PieDataSet(yValues, "");
        dataset.setSliceSpace(3f);
        dataset.setSelectionShift(5f);
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataset));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);


        // Inflate the layout for this fragment
        return view;
    }
}