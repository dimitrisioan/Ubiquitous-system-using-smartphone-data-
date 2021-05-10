package com.univ.ubitrack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsFragment extends Fragment {
    //    private Context context;
    TextView tx_recruitedTeam, tx_ageRange, tx_gender, tx_deviceId;
    private Button delete_btn;
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
    }
    private void creteAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to remove your device?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBHelper dbHelper = new DBHelper(getContext());
                try {
                    dbHelper.deleteAllDevices();
                    Toast.makeText(getContext(), "Device Removed Successfully", Toast.LENGTH_SHORT).show();
                    goToGetStarted();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Couldn't remove device", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.create();
        builder.show();
    }

    private void goToGetStarted(){
        Intent intent = new Intent(getContext(), WelcomePage.class);
        startActivity(intent);
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

        delete_btn = view.findViewById(R.id.delete_btn);

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creteAlertDialog();
            }
        });

        return view;
    }

}