package com.univ.ubitrack;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


public class SettingsFragment extends Fragment {
    //    private Context context;
    TextView tx_recruitedTeam, tx_ageRange, tx_gender, tx_deviceId;
    Button deleteButton;
//    private Context context;

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
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Title")
                .setMessage("Example Message")
                .setPositiveButton("Ok",null)
                .setNegativeButton("Cancel",null)
                .show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "NotClosing", Toast.LENGTH_SHORT).show();
            }
        });
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