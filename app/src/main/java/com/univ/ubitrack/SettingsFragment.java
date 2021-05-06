package com.univ.ubitrack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    TextView tx_recruitedTeam, tx_ageRange, tx_gender, tx_deviceId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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