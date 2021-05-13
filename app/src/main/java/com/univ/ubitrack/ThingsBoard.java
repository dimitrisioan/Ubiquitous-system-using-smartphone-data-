package com.univ.ubitrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.PrecomputedText;
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
    private static final String DEVICE_ID = "DEVICE_ID";
    private static final String CREDENTIALS_ID = "CREDENTIALS_ID";
    private String authToken;
    private String deviceId;
    private String credentialsId;
    private SharedPreferences sharedPreferences;
    private Context context;
    private String username = "omada7@ceid.upatras.gr";
    private String password = "diaxitos1998!";
    private int statusCode;


    public ThingsBoard(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        authToken = sharedPreferences.getString(AUTH_TOKEN_KEY, "NO_KEY");
        deviceId = sharedPreferences.getString(DEVICE_ID, "NO_ID");
        credentialsId = sharedPreferences.getString(CREDENTIALS_ID, "NO_ID");
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
                            sharedPreferences.edit().putString(AUTH_TOKEN_KEY, authToken).apply();
                            if (MainActivity.debugging == 1)
                                Log.i(TAG, authToken);
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

    public void addNewDevice(){
        String addDeviceURL = baseURL + "api/device";

        String device_name = "Participant_7_1";
        String device_type = "Participant";

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

    public void checkIfDeviceExists() {
        String checkDeviceURL = baseURL + "api/tenant/devices?deviceName=Participant_7_1";
        String device_name = "Participant_7_1";

        JSONObject jsonBody = new JSONObject();

        String auth = sharedPreferences.getString("AUTH_TOKEN_KEY", "No key");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, checkDeviceURL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (MainActivity.debugging == 1)
                            Log.i("DEVICE", response.toString());
                        statusCode = 400;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        if (error.networkResponse.statusCode == 404)
                            statusCode = 404;
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

    public void getDeviceId() {
        String checkDeviceURL = baseURL + "api/tenant/devices?deviceName=Participant_7_1";
        String device_name = "Participant_7_0";

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

    public void getDeviceCredentials() {
        String checkDeviceURL = baseURL + "api/device/";
        String deviceID = "9685c4e0-b429-11eb-b9f4-cd7797153d94";

        JSONObject jsonBody = new JSONObject();

        String auth = sharedPreferences.getString("AUTH_TOKEN_KEY", "No key");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, checkDeviceURL + deviceID + "/credentials", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String credentialsId = response.getString("credentialsId");
                            sharedPreferences.edit().putString(CREDENTIALS_ID, credentialsId).apply();
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
}
