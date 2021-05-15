package com.univ.ubitrack;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class NetworkService {
    static Context context;
    static NetworkCapabilities networkCapabilities;

    public NetworkService(Context context) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isNetworkAvailable())
                networkCapabilities = getNetworkCapabilities();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static NetworkCapabilities getNetworkCapabilities() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        android.net.Network network = connectivity.getActiveNetwork();
        NetworkCapabilities capabilities = connectivity.getNetworkCapabilities(network);
        Log.i("getNetworkType()", String.valueOf(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)));
//        if (MainActivity.debugging == 1)
//            Log.i("getNetworkType()", String.valueOf(capabilities));

        return capabilities;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getNetworkType() {
        String netType = "";
        if (isNetworkAvailable()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    netType = "TRANSPORT_WIFI";
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    netType = "TRANSPORT_CELLULAR";
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                    netType = "TRANSPORT_BLUETOOTH";
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    netType = "TRANSPORT_ETHERNET";
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    netType = "TRANSPORT_VPN";
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                    netType = "TRANSPORT_WIFI_AWARE";
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
                    netType = "TRANSPORT_LOWPAN";
                }
            } else {
                netType = "UNKNOWN";
            }
        } else {
            netType = "NONE";
        }
        Log.i("getNetworkType", netType);
        return netType;
    }
}
