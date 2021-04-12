package com.example.theweathermate.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.theweathermate.ModelClasses.CurrentLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeviceLocation {

    public static final String TAG = "mTAG";
    public static final long START_TIME = 8000;
    public static long TIMER_MILLIS = START_TIME;

    Context context;
    CountDownTimer countDownTimer;
    LocationCallback locationCallback;
    locationListener locationListener;
    FusedLocationProviderClient locationProviderClient;

    public void getDeviceLocation(Context context, locationListener locationListener) {

        startTimer();
        this.context = context;
        this.locationListener = locationListener;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

                Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation().getLongitude());
                stopTimer();
                // Reverse GeoCoding
                startWorkerThread(locationResult.getLastLocation());
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                Log.d(TAG, "onLocationAvailability: " + locationAvailability);
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
        locationProviderClient.requestLocationUpdates(getLocationRequest(), locationCallback, Looper.getMainLooper());
    }

    private void stopTimer() {
        countDownTimer.cancel();
        TIMER_MILLIS = START_TIME;
    }


    private void startTimer() {

        countDownTimer = new CountDownTimer(TIMER_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TIMER_MILLIS = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish: timerFinished");
                TIMER_MILLIS = START_TIME;
                removeLocationUpdates();
                if (context != null) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED) {
                        locationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Location location = task.getResult();
                                if (location != null) {
                                    //Reverse Geo Coding
                                    startWorkerThread(location);
                                } else
                                    locationListener.onTaskFailed("Unable to get Location." +
                                            " Error: location object is null");
                            } else
                                locationListener.onTaskFailed("Unable to get Location." +
                                        " Error: " + Objects.requireNonNull(task.getException()).getMessage() + ".");
                        });
                    }
                }
            }
        };
        countDownTimer.start();
    }

    private void startWorkerThread(Location location) {
        new FetchAddress(context, new FetchAddress.workerThreadListener() {
            @Override
            public void onTaskCompleted(Address address) {
                Log.d(TAG, "onTaskCompleted: " + address);
                getAddressLine(address);
            }

            @Override
            public void onTaskFailed(String exception) {
                locationListener.onTaskFailed(exception);
            }
        }).execute(location);
    }


    private void getAddressLine(Address address) {

        List<String> addressParts = new ArrayList<>();
        if (address != null) {
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
//        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        return locationRequest;
    }

    public void removeLocationUpdates() {
        locationProviderClient.removeLocationUpdates(locationCallback);
    }


    public interface locationListener {
        void onTaskCompleted(CurrentLocation currentLocation);

        void onTaskFailed(String exception);
    }
}
