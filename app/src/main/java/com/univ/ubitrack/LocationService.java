package com.univ.ubitrack;

import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.number.Precision;
import android.util.Log;

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
    private String location_id;
    private  String location_type;
    private float location_conf;

    public static final int MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION = 100;

    public LocationService(Context context){
        LocationService.context = context;
        getLoc();
    }

    public void getLoc() {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.TYPES, Place.Field.ID);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
        Places.initialize(context, "AIzaSyCuludz6FCrxBJMCRdFQ66DodFYEOq5ymk");
        PlacesClient placesClient = Places.createClient(context);

        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task2 -> {
                if (task2.isSuccessful()){
                    FindCurrentPlaceResponse response = task2.getResult();
                    PlaceLikelihood place = response.getPlaceLikelihoods().get(0);
                    this.location_conf = (float) place.getLikelihood();
                    this.location_id = place.getPlace().getId();
                    this.location_type = Objects.requireNonNull(place.getPlace().getTypes()).get(0).toString();
                    Log.i("Location", location_id + ", " +  location_type + ", " + location_conf);

                } else {
                    Exception exception = task2.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e("TAG", "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        }
    }
}