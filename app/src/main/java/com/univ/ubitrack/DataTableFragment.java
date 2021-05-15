package com.univ.ubitrack;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;


public class DataTableFragment extends Fragment {
    private TableLayout tableLayout;
    private ArrayList<UsersDataModel> usersDataModels;
    UsersDataModel screen_off_data, screen_on_data;
    public DataTableFragment() {
        // Required empty public constructor
    }

    public static DataTableFragment newInstance(String param1, String param2) {
        DataTableFragment fragment = new DataTableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_table, container, false);

        tableLayout = view.findViewById(R.id.lastUsersDataTable);
        if (getTableDataFromDB()) {
            if (usersDataModels.get(0).getDisplay_state() == 0){
                screen_off_data = usersDataModels.get(0);
                screen_on_data = usersDataModels.get(1);
            }else{
                screen_off_data = usersDataModels.get(1);
                screen_on_data = usersDataModels.get(0);
            }
            addRowData(view);
        }
        return view;
    }


    @SuppressLint("SetTextI18n")
    public void addRowData(View view) {
        TableRow row;
        TextView textView1, textView2;

        row = view.findViewById(R.id.tv_device_interactive);
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        textView1.setText(String.valueOf(screen_on_data.getDevice_interactive()));
        textView1.setGravity(Gravity.CENTER);
        textView2.setText(String.valueOf(screen_off_data.getDevice_interactive()));
        textView2.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        row.addView(textView1);
        row.addView(textView2);

        row = view.findViewById(R.id.tv_display_state);
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        textView1.setText(String.valueOf(screen_on_data.getDisplay_state()));
        textView1.setGravity(Gravity.CENTER);
        textView2.setText(String.valueOf(screen_off_data.getDisplay_state()));
        textView2.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        row.addView(textView1);
        row.addView(textView2);

        row = view.findViewById(R.id.tv_system_time);
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        textView1.setText(String.valueOf(screen_on_data.getSystem_time()));
        textView1.setGravity(Gravity.CENTER);
        textView2.setText(String.valueOf(screen_off_data.getSystem_time()));
        textView2.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        row.addView(textView1);
        row.addView(textView2);

        row = view.findViewById(R.id.tv_activity);
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        textView1.setText(String.valueOf(screen_on_data.getActivity()));
        textView1.setGravity(Gravity.CENTER);
        textView2.setText(String.valueOf(screen_off_data.getActivity()));
        textView2.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        row.addView(textView1);
        row.addView(textView2);

        row = view.findViewById(R.id.tv_activity_conf);
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        textView1.setText(String.valueOf(screen_on_data.getActivity_conf()));
        textView1.setGravity(Gravity.CENTER);
        textView2.setText(String.valueOf(screen_off_data.getActivity_conf()));
        textView2.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        row.addView(textView1);
        row.addView(textView2);

        row = view.findViewById(R.id.tv_location_type);
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        textView1.setText(String.valueOf(screen_on_data.getLocation_type()));
        textView1.setGravity(Gravity.CENTER);
        textView1.setTextSize(10);
        textView2.setText(String.valueOf(screen_off_data.getLocation_type()));
        textView2.setGravity(Gravity.CENTER);
        textView2.setTextSize(10);
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        row.addView(textView1);
        row.addView(textView2);

        row = view.findViewById(R.id.tv_location_id);
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        if (screen_on_data.getLocation_id() != null)
            textView1.setText(screen_on_data.getLocation_id().substring(0, 8));
        else
            textView1.setText("null");
        textView1.setGravity(Gravity.CENTER);
        if (screen_on_data.getLocation_id() != null)
            textView2.setText(screen_off_data.getLocation_id().substring(0, 8));
        else
            textView2.setText("null");
        textView2.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        row.addView(textView1);
        row.addView(textView2);

        row = view.findViewById(R.id.tv_location_conf);
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        textView1.setText(String.valueOf(screen_on_data.getLocation_conf()));
        textView1.setGravity(Gravity.CENTER);
        textView2.setText(String.valueOf(screen_off_data.getLocation_conf()));
        textView2.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        row.addView(textView1);
        row.addView(textView2);

        row = view.findViewById(R.id.tv_battery_level);
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        textView1.setText(String.valueOf(screen_on_data.getBattery_level()));
        textView1.setGravity(Gravity.CENTER);
        textView2.setText(String.valueOf(screen_off_data.getBattery_level()));
        textView2.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        row.addView(textView1);
        row.addView(textView2);

        row = view.findViewById(R.id.tv_battery_status);
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        textView1.setText(String.valueOf(screen_on_data.getBattery_status()));
        textView1.setGravity(Gravity.CENTER);
        textView2.setText(String.valueOf(screen_off_data.getBattery_status()));
        textView2.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        row.addView(textView1);
        row.addView(textView2);

        row = view.findViewById(R.id.tv_network_type);
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        textView1.setText(screen_on_data.getNetwork_type());
        textView1.setTextSize(10);
        textView1.setGravity(Gravity.CENTER);
        textView2.setText(screen_off_data.getNetwork_type());
        textView1.setTextSize(10);
        textView2.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        row.addView(textView1);
        row.addView(textView2);

        row = view.findViewById(R.id.tv_notifs_active);
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        textView1.setText(String.valueOf(screen_on_data.getNotifs_active()));
        textView1.setGravity(Gravity.CENTER);
        textView2.setText(String.valueOf(screen_off_data.getNotifs_active()));
        textView2.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        row.addView(textView1);
        row.addView(textView2);
    }

    private boolean getTableDataFromDB() {
        DBHelper dbHelper = new DBHelper(getContext());
        try {
            usersDataModels = dbHelper.getLastTwoUsersData();
        } catch (Exception e) {
            if (MainActivity.debugging == 1)
            e.printStackTrace();
        }
        return usersDataModels.size() == 2;
    }
}


