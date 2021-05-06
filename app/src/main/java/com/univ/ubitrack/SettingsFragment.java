package com.univ.ubitrack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;


public class SettingsFragment extends Fragment {
    TextView tx_recruitedTeam, tx_ageRange, tx_gender, tx_deviceId;


    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        assert getArguments() != null;
        String recruitedTeam = getArguments().getString("recruitedTeam");
        String ageRange = getArguments().getString("ageRange");
        String gender = getArguments().getString("gender");
        String deviceId = getArguments().getString("deviceId");

        tx_recruitedTeam = view.findViewById(R.id.tx_recruitedTeam);
        tx_recruitedTeam.setText(String.valueOf(recruitedTeam));

        tx_ageRange = view.findViewById(R.id.tx_ageRange);
        tx_ageRange.setText(String.valueOf(ageRange));

        tx_gender = view.findViewById(R.id.tx_gender);
        tx_gender.setText(String.valueOf(gender));

        tx_deviceId = view.findViewById(R.id.tx_deviceId);
        tx_deviceId.setText(String.valueOf(deviceId));

        return view;
    }
}