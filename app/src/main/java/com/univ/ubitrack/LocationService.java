package com.univ.ubitrack;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.util.Log;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.content.PermissionChecker;
//
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.Task;
//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.model.Place;
//import com.google.android.libraries.places.api.model.PlaceLikelihood;
//import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
//import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
//import com.google.android.libraries.places.api.net.PlacesClient;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class LocationSevice {
//
//    Context context;
//
//    public LocationSevice(Context context) {
//        this.context = context;
//        // Initialize the SDK
//        Places.initialize(context, "AIzaSyCuludz6FCrxBJMCRdFQ66DodFYEOq5ymk");
//        // Create a new PlacesClient instance
//
//    }
//
//    // Use fields to define the data types to return.
//    List<Place.Field> placeFields = Arrays.asList(Place.Field.TYPES, Place.Field.ID);
//
//    // Use the builder to create a FindCurrentPlaceRequest.
//    FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
//
//    // Call findCurrentPlace and handle the response (first check that the user has granted permission).
//if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//        Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
//        placeResponse.addOnCompleteListener(task -> {
//            if (task.isSuccessful()){
//                FindCurrentPlaceResponse response = task.getResult();
//                for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//                    Log.i("Location", String.format("Place '%s' has likelihood: %f",
//                            placeLikelihood.getPlace().getName(),
//                            placeLikelihood.getLikelihood()));
//                }
//            } else {
//                Exception exception = task.getException();
//                if (exception instanceof ApiException) {
//                    ApiException apiException = (ApiException) exception;
//                    Log.e("Location", "Place not found: " + apiException.getStatusCode());
//                }
//            }
//        });
//    }
//
//
//
//    {
//
//            PlacesClient placesClient = Places.createClient(context);
//
//
//            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
//        placeResponse.addOnCompleteListener(task -> {
//            if (task.isSuccessful()){
//                FindCurrentPlaceResponse response = task.getResult();
//                Log.i("Location", String.valueOf(response));
//                for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//                    Log.i("location", placeLikelihood.getPlace().getId() + ", " +  placeLikelihood.getPlace().getTypes() + ", " + placeLikelihood.getLikelihood());
//                }
//            } else {
//                Exception exception = task.getException();
//                if (exception instanceof ApiException) {
//                    ApiException apiException = (ApiException) exception;
//                    Log.e("location", "Place not found: " + apiException.getStatusCode());
//                }
//            }
//        });
//    } else {
//        // A local method to request required permissions;
//        // See https://developer.android.com/training/permissions/requesting
////                getLocationPermission();
//    }
//
//
//}

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationService {
    static Context context;
    public static String location_id;
    public static String location_type;

    public static final int MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION = 100;

    public LocationService(Context context){
        LocationService.context = context;
        getLoc();
    }

    public static void getLoc() {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.TYPES, Place.Field.ID);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
        Places.initialize(context, "AIzaSyCuludz6FCrxBJMCRdFQ66DodFYEOq5ymk");
        PlacesClient placesClient = Places.createClient(context);

        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task2 -> {
                if (task2.isSuccessful()){
                    FindCurrentPlaceResponse response = task2.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        location_id = placeLikelihood.getPlace().getId();
                        location_type = Objects.requireNonNull(placeLikelihood.getPlace().getTypes()).get(0).toString();
                        Log.i("location", location_id + ", " +  location_type + ", " + placeLikelihood.getLikelihood());
                    }
                } else {
                    Exception exception = task2.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e("TAG", "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        } else {

        }
    }

    public static String get_location_id(){
        return location_id;
    }
    public static String get_location_name(){
        return location_type;
    }
}