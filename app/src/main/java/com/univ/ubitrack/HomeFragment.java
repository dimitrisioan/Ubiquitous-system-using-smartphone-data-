package com.univ.ubitrack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment {

    Intent serviceIntent = null;
    Switch onOffSwitch;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SwitchCompat onOffSwitch = (SwitchCompat) view.findViewById(R.id.switch_home);
        if (Constants.SCREEN_ON_OFF_SERVICE)
            onOffSwitch.setChecked(true);
        else
            onOffSwitch.setChecked(false);

        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                if (isChecked) {
                    Constants.SCREEN_ON_OFF_SERVICE = true;
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    Constants.SCREEN_ON_OFF_SERVICE = false;
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            }

        });
        return view;
    }}