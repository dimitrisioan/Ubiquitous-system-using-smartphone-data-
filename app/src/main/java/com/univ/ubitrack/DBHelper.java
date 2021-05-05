package com.univ.ubitrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DEVICE_TABLE = "DEVICE";
    public static final String COLUMN_RECRUITED_TEAM = "recruitedTeam";
    public static final String COLUMN_AGE_RANGE = "ageRange";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_DEVICE_ID = "device_id";
    public static final String COLUMN_IS_DEVISE_REGISTERED = "isDeviseRegistered";
    public static final String COLUMN_UID = "uid";

    public DBHelper(@Nullable Context context) {
        super(context, "ubitrack", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDeviseTableStatement = "create table " + DEVICE_TABLE + " (" +
                COLUMN_UID + " INTEGER " +
                "constraint  device_pk " +
                "primary key autoincrement, " +
                COLUMN_RECRUITED_TEAM + " INTEGER not null, " +
                COLUMN_AGE_RANGE + " TEXT not null, " +
                COLUMN_GENDER + " TEXT not null, " +
                COLUMN_DEVICE_ID + " TEXT not null, " +
                COLUMN_IS_DEVISE_REGISTERED + " BOOLEAN not null )";

        db.execSQL(createDeviseTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addDevise(DeviceModel deviceModel){
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

    public void deleteAllDevises() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(DEVICE_TABLE, null, null);
        } catch (Exception e) {
            Log.e("Database", "Error while deleting all devises.");
        }
    }

    public Object getDevise() {
        String selectDevise = "SELECT * FROM " + DEVICE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        DeviceModel deviceModel = null;
        try (Cursor cursor = db.rawQuery(selectDevise, null)) {

            if (cursor.moveToFirst()) {
                int uid = cursor.getInt(0);
                int recruitedTeam = cursor.getInt(1);
                String ageRange = cursor.getString(2);
                String gender = cursor.getString(3);
                String devise_id = cursor.getString(4);
                int isDeviseRegisted = cursor.getInt(5);

                deviceModel = new DeviceModel(uid, recruitedTeam, ageRange, gender, devise_id, isDeviseRegisted);
            } else {
                return null;
            }
            cursor.close();
        }
        db.close();
        return deviceModel;
    }
}
