package com.univ.ubitrack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ThirdChart extends Fragment {


    public ThirdChart() {
        // Required empty public constructor
    }


    public static ThirdChart newInstance(String param1, String param2) {
        ThirdChart fragment = new ThirdChart();
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

        View view = inflater.inflate(R.layout.fragment_third_chart, container, false);

        // Inflate the layout for this fragment
        return view;
    }
}