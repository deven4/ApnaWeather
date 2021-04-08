package com.example.theweathermate.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.theweathermate.ModelClasses.CurrentLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class DeviceLocation {

    locationListener locationListener;

    public void getDeviceLocation(Context context, locationListener locationListener) {

        this.locationListener = locationListener;
        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

                new FetchAddress(context, new FetchAddress.workerThreadListener() {
                    @Override
                    public void onTaskCompleted(Address address) {
                        getAddressLine(address);
                    }

                    @Override
                    public void onTaskFailed(String exception) {
                        locationListener.onTaskFailed(exception);
                    }
                }).execute(locationResult.getLastLocation());
            }
        };

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationProviderClient.requestLocationUpdates(getLocationRequest(), locationCallback, null);
    }


    private void getAddressLine(Address address) {

        List<String> addressParts = new ArrayList<>();
        if(address!=null) {

            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressParts.add(address.getAddressLine(i));
            }

            CurrentLocation currentLocation = new CurrentLocation(address.getLocality(), address.getAdminArea(),
                    address.getLatitude(), address.getLongitude(), TextUtils.join("\n", addressParts));
            locationListener.onTaskCompleted(currentLocation);
        } else
            locationListener.onTaskFailed("Not able to get your current location Reason: Address is NULL.");
    }


    public static LocationRequest getLocationRequest() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }


    public interface locationListener {
        void onTaskCompleted(CurrentLocation currentLocation);

        void onTaskFailed(String exception);
    }
}
