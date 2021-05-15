package com.univ.ubitrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ThingsBoard {
    public static final String TAG = "ThingsBoard";
    private static final String baseURL = "http://150.140.142.67:8082/";
    private static final String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";
    private static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY";
    private static final String DEVICE_ID = "DEVICE_ID";
    private static final String CREDENTIALS_ID = "CREDENTIALS_ID";
    private static final String LAST_ADDED_DEVICE_NAME = "LAST_ADDED_DEVICE_NAME";
    private String authToken;
    private String refreshToken;
    private String deviceId;
    private String credentialsId;
    private String lastAddedDeviceName;
    private static SharedPreferences sharedPreferences;
    private static Context context;
    private String username = "omada7@ceid.upatras.gr";
    private String password = "diaxitos1998!";
    private int statusCode;


    public ThingsBoard(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        authToken = sharedPreferences.getString(AUTH_TOKEN_KEY, "NO_KEY");
        refreshToken = sharedPreferences.getString(REFRESH_TOKEN_KEY, "NO_KEY");
        deviceId = sharedPreferences.getString(DEVICE_ID, "NO_ID");
        credentialsId = sharedPreferences.getString(CREDENTIALS_ID, "NO_ID");
        lastAddedDeviceName = sharedPreferences.getString(LAST_ADDED_DEVICE_NAME, "NO_NAME");
        obtainThingsBoardToken();
    }


    private void obtainThingsBoardToken() {
        String loginURL = baseURL + "api/auth/login";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, loginURL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.d(TAG, response.toString());

                        try {
                            authToken = response.getString("token");
                            refreshToken = response.getString("refreshToken");
                            sharedPreferences.edit().putString(AUTH_TOKEN_KEY, authToken).apply();
                            sharedPreferences.edit().putString(REFRESH_TOKEN_KEY, refreshToken).apply();
                            if (MainActivity.debugging == 1)
                                Log.i(TAG, authToken);
                                Log.i(TAG, refreshToken);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(getApplicationContext(), R.string.auth_ok, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                            Toast.makeText(context, "Connection to ThingsBoard Failed", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    public void addNewDevice(String device_name, String device_type, String age, String gender, int team){
        String addDeviceURL = baseURL + "api/device";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", device_name);
            jsonBody.put("type", device_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String auth = sharedPreferences.getString("AUTH_TOKEN_KEY", "No key");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, addDeviceURL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String device_id = response.getJSONObject("id").getString("id");
                            sharedPreferences.edit().putString(DEVICE_ID, device_id).apply();
                            getDeviceCredentials(device_id, age, gender, team);
                            if (MainActivity.debugging == 1)
                                Log.i("DEVICE", "Device Added with ID -> " + device_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(context, "Connection to ThingsBoard Failed", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("X-Authorization", "Bearer " + auth);
                return headers;
            }
        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    public void getLastAddedDevice(int teamNumber, String age, String gender){
        String getLastAddedDeviceURL = baseURL + "api/tenant/devices?textSearch=Participant_" + teamNumber + "&sortProperty=name&sortOrder=desc&pageSize=10&page=0";
        String lastAddedDevice = "";

        JSONObject jsonBody = new JSONObject();

        String auth = sharedPreferences.getString("AUTH_TOKEN_KEY", "No key");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, getLastAddedDeviceURL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String device = response.getJSONArray("data").getJSONObject(0).getString("name");
                            sharedPreferences.edit().putString(LAST_ADDED_DEVICE_NAME, device).apply();
                            int deviceNumber = Integer.parseInt(device.substring(device.length() - 1)) + 1;
                            String nextDevice = "Participant_" + teamNumber + "_" + deviceNumber;
                            addNewDevice(nextDevice, "Participant", age, gender, teamNumber);
                            Utilities.addDeviseToDB(context, teamNumber, age, gender, nextDevice);
                            if (MainActivity.debugging == 1)
                                Log.i("DEVICE", "Last added device name -> " + device);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(context, "Connection to ThingsBoard Failed", Toast.LENGTH_SHORT).show();
                        if (error.networkResponse.statusCode == 401){
                            refreshToken();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("X-Authorization", "Bearer " + auth);
                return headers;
            }
        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    public void getDeviceId(String device_name) {
        String checkDeviceURL = baseURL + "api/tenant/devices?deviceName=" + device_name;

        JSONObject jsonBody = new JSONObject();

        String auth = sharedPreferences.getString("AUTH_TOKEN_KEY", "No key");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, checkDeviceURL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String device_id = response.getJSONObject("id").getString("id");
                            sharedPreferences.edit().putString(DEVICE_ID, device_id).apply();
                            if (MainActivity.debugging == 1)
                                Log.i("DEVICE", "Device ID -> " + device_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(context, "Connection to ThingsBoard Failed", Toast.LENGTH_SHORT).show();
                        if (error.networkResponse.statusCode == 401){
                            refreshToken();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("X-Authorization", "Bearer " + auth);
                return headers;
            }
        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    public void deleteDevice(){
        String removeDeviceURL = baseURL + "api/device/";

        JSONObject jsonBody = new JSONObject();

        String auth = sharedPreferences.getString("AUTH_TOKEN_KEY", "No key");
        String device_id = sharedPreferences.getString("DEVICE_ID", "No ID");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, removeDeviceURL + device_id, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (MainActivity.debugging == 1)
                            Log.i("DEVICE Removed", response.toString());
                        Toast.makeText(context, "Device Removed", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(context, "Connection to ThingsBoard Failed", Toast.LENGTH_SHORT).show();
                        if (error.networkResponse.statusCode == 401){
                            refreshToken();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("X-Authorization", "Bearer " + auth);
                return headers;
            }
        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    public void getDeviceIdFromDeviceName(String deviceName) {
        String checkDeviceURL = baseURL + "api/tenant/devices?deviceName=";

        JSONObject jsonBody = new JSONObject();

        String auth = sharedPreferences.getString("AUTH_TOKEN_KEY", "No key");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, checkDeviceURL + deviceName, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String device_id = response.getJSONObject("id").getString("id");
                            sharedPreferences.edit().putString(DEVICE_ID, device_id).apply();
                            if (MainActivity.debugging == 1)
                                Log.i("DEVICE", "Device ID -> " + device_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(context, "Connection to ThingsBoard Failed", Toast.LENGTH_SHORT).show();
                        if (error.networkResponse.statusCode == 401){
                            refreshToken();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("X-Authorization", "Bearer " + auth);
                return headers;
            }
        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    public void getDeviceCredentials(String deviceID, String age, String gender, int team) {
        String checkDeviceURL = baseURL + "api/device/";

        JSONObject jsonBody = new JSONObject();

        String auth = sharedPreferences.getString("AUTH_TOKEN_KEY", "No key");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, checkDeviceURL + deviceID + "/credentials", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String credentialsId = response.getString("credentialsId");
                            sharedPreferences.edit().putString(CREDENTIALS_ID, credentialsId).apply();
                            addDeviceAttributes(age, gender, team);
                            if (MainActivity.debugging == 1)
                                Log.i("DEVICE", "Credentials ID -> " + credentialsId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(context, "Connection to ThingsBoard Failed", Toast.LENGTH_SHORT).show();
                        if (error.networkResponse.statusCode == 401){
                            refreshToken();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("X-Authorization", "Bearer " + auth);
                return headers;
            }
        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    public void addDeviceAttributes(String userAge, String gender, int team) {
        String credentials = sharedPreferences.getString(CREDENTIALS_ID, "No Id");
        String addDeviceAttributesURL = baseURL + "api/v1/" + credentials + "/attributes";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_age", userAge);
            jsonBody.put("user_sex", gender);
            jsonBody.put("recruiting_team", team);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String auth = sharedPreferences.getString("AUTH_TOKEN_KEY", "No key");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, addDeviceAttributesURL , jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (MainActivity.debugging == 1)
                            Log.i("DEVICE", "Attributes Added");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(context, "Connection to ThingsBoard Failed", Toast.LENGTH_SHORT).show();
                        try {
                            if (error.networkResponse.statusCode == 401){
                                refreshToken();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("X-Authorization", "Bearer " + auth);
                return headers;
            }
        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    public void refreshToken() {
        String refreshTokenURL = baseURL + "api/auth/logout";
        String refreshToken = sharedPreferences.getString("REFRESH_TOKEN_KEY", "No key");

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("refreshToken", refreshToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String auth = sharedPreferences.getString("AUTH_TOKEN_KEY", "No key");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, refreshTokenURL , jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (MainActivity.debugging == 1)
                            Log.i("DEVICE", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(context, "Connection to ThingsBoard Failed", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "*/*");
                headers.put("X-Authorization", "Bearer " + auth);
                return headers;
            }
        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    public static void addDeviceTelemetry(String deviceInteractive, int displayState, String systemTime,
                                   String activity, float activityConf, String locationType,
                                   String locationId, float locationConf,  int batteryLevel,
                                   String batteryStatus, String networkType, int notifsActive) {
        String deviceId = sharedPreferences.getString("DEVICE_ID", "No ID");
        String addDeviceTelemetryURL = baseURL + "api/plugins/telemetry/DEVICE/" + deviceId + "/timeseries/CLIENT_SCOPE";

        JSONObject jsonValues = new JSONObject();
        try {
            jsonValues.put("device_interactive", deviceInteractive);
            jsonValues.put("display_state", displayState);
            jsonValues.put("system_time", systemTime);
            jsonValues.put("activity", activity);
            jsonValues.put("activity_conf", activityConf);
            jsonValues.put("location_type", locationType);
            jsonValues.put("location_id", locationId);
            jsonValues.put("location_conf", locationConf);
            jsonValues.put("battery_level", batteryLevel);
            jsonValues.put("battery_status", batteryStatus);
            jsonValues.put("network_type", networkType);
            jsonValues.put("notifs_active", notifsActive);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("values", jsonValues);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String auth = sharedPreferences.getString("AUTH_TOKEN_KEY", "No key");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, addDeviceTelemetryURL , jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("X-Authorization", "Bearer " + auth);
                return headers;
            }
        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    public void addNewDevice(int team, String age, String gender) {
        getLastAddedDevice(team, age, gender);
    }
}
