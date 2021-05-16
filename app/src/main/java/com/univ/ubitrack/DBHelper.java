package com.univ.ubitrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DEVICE_TABLE = "DEVICE";
    public static final String COLUMN_RECRUITED_TEAM = "recruitedTeam";
    public static final String COLUMN_AGE_RANGE = "ageRange";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_DEVICE_ID = "device_id";
    public static final String COLUMN_IS_DEVISE_REGISTERED = "isDeviseRegistered";
    public static final String COLUMN_UID = "uid";

    public static  final String USERS_DATA_TABLE = "USERS_DATA";
    private static final String COLUMN_DISPLAY_STATE = "display_state";
    private static final String COLUMN_SYSTEM_TIME = "system_time";
    private static final String COLUMN_ACTIVITY = "activity";
    private static final String COLUMN_ACTIVITY_CONF = "activity_conf";
    private static final String COLUMN_LOCATION_ID = "location_id";
    private static final String COLUMN_LOCATION_TYPE = "location_type";
    private static final String COLUMN_LOCATION_CONF = "location_conf";
    private static final String COLUMN_BATTERY_LEVEL = "battery_level";
    private static final String COLUMN_BATTERY_STATUS = "battery_status";
    private static final String COLUMN_NOTIFS_ACTIVE = "notifs_active";
    private static final String COLUMN_DEVICE_INTERACTIVE = "device_interactive";
    private static final String COLUMN_NETWORK_TYPE = "network_type";
    private static final String COLUMN_ADDED_THINKSBOARD = "thinksboard_added";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public DBHelper(@Nullable Context context) {
        super(context, "ubitrack", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDeviceTableStatement = "create table " + DEVICE_TABLE + " (" +
                COLUMN_UID + " INTEGER " +
                "constraint  device_pk " +
                "primary key autoincrement, " +
                COLUMN_RECRUITED_TEAM + " INTEGER not null, " +
                COLUMN_AGE_RANGE + " TEXT not null, " +
                COLUMN_GENDER + " TEXT not null, " +
                COLUMN_DEVICE_ID + " TEXT not null, " +
                COLUMN_IS_DEVISE_REGISTERED + " BOOLEAN not null )";

        String createUsersDataTableStatement = "create table " + USERS_DATA_TABLE + " (" +
                COLUMN_UID + " INTEGER " +
                "constraint  device_pk " +
                "primary key autoincrement, " +
                COLUMN_DEVICE_INTERACTIVE + " TEXT DEFAULT 'unknown' not null, " +
                COLUMN_DISPLAY_STATE + " INTEGER DEFAULT -1 not null, " +
                COLUMN_SYSTEM_TIME + " TEXT NOT NULL, " +
                COLUMN_ACTIVITY + " TEXT DEFAULT 'unknown', " +
                COLUMN_ACTIVITY_CONF + " FLOAT DEFAULT 0.0, " +
                COLUMN_LOCATION_TYPE + " TEXT DEFAULT 'unknown', " +
                COLUMN_LOCATION_ID + " TEXT DEFAULT 'unknown', " +
                COLUMN_LOCATION_CONF + " TEXT DEFAULT 0, " +
                COLUMN_BATTERY_LEVEL + " INTEGER DEFAULT -1, " +
                COLUMN_BATTERY_STATUS + " TEXT DEFAULT 'unknown', " +
                COLUMN_NETWORK_TYPE + " TEXT DEFAULT 'unknown', " +
                COLUMN_NOTIFS_ACTIVE + " INTEGER DEFAULT -1, " +
                COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT (datetime('now','localtime')), " +
                COLUMN_ADDED_THINKSBOARD + " INTEGER DEFAULT 0)";

        db.execSQL(createDeviceTableStatement);
        db.execSQL(createUsersDataTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addDevice(DeviceModel deviceModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_RECRUITED_TEAM, deviceModel.getRecruitedTeam());
        contentValues.put(COLUMN_AGE_RANGE, deviceModel.getAgeRange());
        contentValues.put(COLUMN_DEVICE_ID, deviceModel.getDevice_id());
        contentValues.put(COLUMN_GENDER, deviceModel.getGender());
        contentValues.put(COLUMN_IS_DEVISE_REGISTERED, deviceModel.getIsDeviseRegistered());

        long insert = db.insert(DEVICE_TABLE, null, contentValues);
        return insert != -1;
    }

    public boolean addUsersData(UsersDataModel usersDataModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_DISPLAY_STATE, usersDataModel.getDisplay_state());
        contentValues.put(COLUMN_DEVICE_INTERACTIVE, usersDataModel.getDevice_interactive());
        contentValues.put(COLUMN_SYSTEM_TIME, usersDataModel.getSystem_time());
        contentValues.put(COLUMN_ACTIVITY, usersDataModel.getActivity());
        contentValues.put(COLUMN_ACTIVITY_CONF, usersDataModel.getActivity_conf());
        contentValues.put(COLUMN_LOCATION_TYPE, usersDataModel.getLocation_type());
        contentValues.put(COLUMN_LOCATION_ID, usersDataModel.getLocation_id());
        contentValues.put(COLUMN_LOCATION_CONF, usersDataModel.getLocation_conf());
        contentValues.put(COLUMN_BATTERY_LEVEL, usersDataModel.getBattery_level());
        contentValues.put(COLUMN_BATTERY_STATUS, usersDataModel.getBattery_status());
        contentValues.put(COLUMN_NETWORK_TYPE, usersDataModel.getNetwork_type());
        contentValues.put(COLUMN_NOTIFS_ACTIVE, usersDataModel.getNotifs_active());
        contentValues.put(COLUMN_ADDED_THINKSBOARD, usersDataModel.getAdded_thinksboard());

        long insert = db.insert(USERS_DATA_TABLE, null, contentValues);
        return insert != -1;
    }

    public void deleteAllDevices() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(DEVICE_TABLE, null, null);
        } catch (Exception e) {
            Log.e("Database", "Error while deleting all devises.");
        }
    }

    public void deleteAllUsersData() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(USERS_DATA_TABLE, null, null);
        } catch (Exception e) {
            Log.e("Database", "Error while deleting all devises.");
        }
    }

    public Object getDevice() {
        String selectDevise = "SELECT * FROM " + DEVICE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        DeviceModel deviceModel = null;
        try (Cursor cursor = db.rawQuery(selectDevise, null)) {

            if (cursor.moveToFirst()) {
                int uid = cursor.getInt(0);
                int recruitedTeam = cursor.getInt(1);
                String ageRange = cursor.getString(2);
                String gender = cursor.getString(3);
                String device_id = cursor.getString(4);
                int isDeviceRegistered = cursor.getInt(5);

                deviceModel = new DeviceModel(uid, recruitedTeam, ageRange, gender, device_id, isDeviceRegistered);
            } else {
                return null;
            }
            cursor.close();
        }
        db.close();
        return deviceModel;
    }

    public ArrayList<UsersDataModel> getLastTwoUsersData() {
        String selectUsersData = "SELECT * FROM " + USERS_DATA_TABLE + " ORDER BY + " + COLUMN_UID + " DESC LIMIT 2";
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<UsersDataModel> usersData = new ArrayList<UsersDataModel>();

        try (Cursor cursor = db.rawQuery(selectUsersData, null)) {
            while (cursor.moveToNext()) {
                UsersDataModel usersDataModel = null;
                int uid = cursor.getInt(0);
                String device_interactive = cursor.getString(1);
                int display_state = cursor.getInt(2);
                String system_time = cursor.getString(3);
                String activity = cursor.getString(4);
                float activity_conf = cursor.getFloat(5);
                String location_type = cursor.getString(6);
                String location_id = cursor.getString(7);
                float location_conf = cursor.getFloat(8);
                int battery_level = cursor.getInt(9);
                String battery_status = cursor.getString(10);
                String network_type = cursor.getString(11);
                int notifs_active = cursor.getInt(12);
                int added_thinksboard = cursor.getInt(13);
                usersDataModel = new UsersDataModel(uid, device_interactive, display_state,
                        system_time,activity, activity_conf, location_type, location_id,
                        location_conf, battery_level, battery_status, network_type, notifs_active, added_thinksboard);
                usersData.add(usersDataModel);
                if (MainActivity.debugging == 1) {
                    Log.i("DB Rows", usersDataModel.toString());
                }
            }
        }
        db.close();
        return usersData;
    }

    public ArrayList<UsersDataModel> getUsersDataForThingsBoard() {
        String selectUsersData = "SELECT * FROM " + USERS_DATA_TABLE + " WHERE thinksboard_added = 0 ORDER BY + " + COLUMN_UID + " DESC LIMIT 5";
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<UsersDataModel> usersData = new ArrayList<UsersDataModel>();

        try (Cursor cursor = db.rawQuery(selectUsersData, null)) {
            while (cursor.moveToNext()) {
                UsersDataModel usersDataModel = null;
                int uid = cursor.getInt(0);
                String device_interactive = cursor.getString(1);
                int display_state = cursor.getInt(2);
                String system_time = cursor.getString(3);
                String activity = cursor.getString(4);
                float activity_conf = cursor.getFloat(5);
                String location_type = cursor.getString(6);
                String location_id = cursor.getString(7);
                float location_conf = cursor.getFloat(8);
                int battery_level = cursor.getInt(9);
                String battery_status = cursor.getString(10);
                String network_type = cursor.getString(11);
                int notifs_active = cursor.getInt(12);
                int added_thinksboard = cursor.getInt(13);
                usersDataModel = new UsersDataModel(uid, device_interactive, display_state,
                        system_time,activity, activity_conf, location_type, location_id,
                        location_conf, battery_level, battery_status, network_type, notifs_active, added_thinksboard);
                usersData.add(usersDataModel);
                if (MainActivity.debugging == 1) {
                    Log.i("DB Rows", usersDataModel.toString());
                }
            }
        }
        db.close();
        return usersData;
    }

    public boolean updateThinsBoardStatus(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ADDED_THINKSBOARD, 1);

        long update = db.update(USERS_DATA_TABLE, contentValues, "uid = ?", new String[]{String.valueOf(id)});
        return update != -1;
    }

    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + USERS_DATA_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public ArrayList<ActivityCounter> groupByActivity() {
        String selectUsersData = "SELECT  activity, COUNT(uid) FROM " + USERS_DATA_TABLE + " GROUP BY " + COLUMN_ACTIVITY;
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<ActivityCounter> activityCounters = new ArrayList<ActivityCounter>();

        try (Cursor cursor = db.rawQuery(selectUsersData, null)) {
            while (cursor.moveToNext()) {
                ActivityCounter activityCounter = null;
                String  activity = cursor.getString(0);
                int count = cursor.getInt(1);
                activityCounter = new ActivityCounter(activity, count);
                activityCounters.add(activityCounter);
                Log.i("Counter", activity + count);
            }
        }
        db.close();
        return activityCounters;
    }

    public ArrayList<EventsPerDay> getEventsForADay(ArrayList<String> dates) {
        ArrayList<EventsPerDay> eventsPerDays = new ArrayList<>();

        for ( int i = 0; i <= dates.size() - 1; i += 2 ) {
            Log.i("Date", dates.get(i)+dates.size());
            String selectUsersData = "SELECT COUNT(uid) FROM " + USERS_DATA_TABLE + " WHERE " + COLUMN_TIMESTAMP + " >= '" + dates.get(i) + "' AND "  + COLUMN_TIMESTAMP + " <= '" + dates.get(i+1) + "'";
            SQLiteDatabase db = this.getReadableDatabase();

            try (Cursor cursor = db.rawQuery(selectUsersData, null)) {
                while (cursor.moveToNext()) {

                    int count = cursor.getInt(0);
                    Log.i("Loop", String.valueOf(count));
                    EventsPerDay eventsPerDay = new EventsPerDay(dates.get(i), count);
                    eventsPerDays.add(eventsPerDay);
                }
            }catch (Exception e) {
                Log.e("SQLITE", e.toString());
            }
            db.close();
        }

        return eventsPerDays;
    }
}
