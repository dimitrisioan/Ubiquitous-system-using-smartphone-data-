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
    private String authToken;
    private SharedPreferences sharedPreferences;
    private Context context;
    private String username = "omada7@ceid.upatras.gr";
    private String password = "diaxitos1998!";

    public ThingsBoard(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        authToken = sharedPreferences.getString(AUTH_TOKEN_KEY, "NO_KEY");
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
//                        Toast.makeText(context.getApplicationContext(), R.string.auth_fail, Toast.LENGTH_SHORT).show();
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
}
