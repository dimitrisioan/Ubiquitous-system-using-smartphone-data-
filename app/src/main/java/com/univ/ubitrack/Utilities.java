package com.univ.ubitrack;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
    public static void addDeviseToDB(Context context, int recruitingTeam, String ageRange, String gender, String device_id) {
        try {
            DBHelper dbHelper = new DBHelper(context);
            DeviceModel deviceModel = new DeviceModel(-1, recruitingTeam, ageRange, gender, device_id, 1);
            dbHelper.deleteAllDevices();
            boolean success = dbHelper.addDevice(deviceModel);
            if (MainActivity.debugging == 1) {
                Log.i("DB", String.valueOf(success));
            }
        }catch (Exception e){
            Log.i("Add device", e.toString());
//            Toast.makeText(SecondRegisterPage.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
        }
    }
}
