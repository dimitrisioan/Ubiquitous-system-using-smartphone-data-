package com.univ.ubitrack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<String> getLastSevenDays() {
        ArrayList<String> last7Dates = new ArrayList<>();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        Calendar cal = Calendar.getInstance();

        // get starting date
        cal.add(Calendar.DAY_OF_YEAR, -7);

        // loop adding one day in each iteration
        for(int i = 0; i< 7; i++){
            cal.add(Calendar.DAY_OF_YEAR, 1);
            String date_start = sdf.format(cal.getTime()) + "00:00:00";
            String date_end = sdf.format(cal.getTime()) + "23:59:59";
            last7Dates.add(date_start);
            last7Dates.add(date_end);
        }
        return last7Dates;
    }

}
