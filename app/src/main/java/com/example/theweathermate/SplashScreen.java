package com.example.theweathermate;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;

import com.example.theweathermate.ModelClasses.CurrentLocation;
import com.example.theweathermate.Utils.AlertDialogBox;
import com.example.theweathermate.Utils.Constants;
import com.example.theweathermate.Utils.DeviceLocation;
import com.example.theweathermate.Utils.HelperMethods;
import com.example.theweathermate.Utils.Permissions;

public class SplashScreen extends AppCompatActivity implements DeviceLocation.locationListener {

    private static final String TAG = "mTAG";
    private static final int GPS_REQUEST_CODE = 10;

    TextView fetchingLocation;
    DeviceLocation deviceLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        deviceLocation = new DeviceLocation();
        fetchingLocation = findViewById(R.id.textView);

        HelperMethods.setBlinkingText(fetchingLocation);

        getDeviceLocation();
    }

    private void getDeviceLocation() {
        if (Permissions.isPermissionsGranted(this)) {
            if (isGPSEnabled())
                deviceLocation.getDeviceLocation(this, this);
            else
                showAlertDialog();
        } else
            ActivityCompat.requestPermissions(this, Permissions.PERMISSIONS, Permissions.PERMISSION_CODE);
    }


    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return LocationManagerCompat.isLocationEnabled(locationManager);
    }


    private void showAlertDialog() {

        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setTitle("GPS Permission");
        alBuilder.setMessage("GPS is required for this app to work. Please enable GPS.");
        alBuilder.setPositiveButton("Yes", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, GPS_REQUEST_CODE);
        });
        alBuilder.setCancelable(false);
        alBuilder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean flag = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                flag = false;
                break;
            }
        }

        if (flag)
            getDeviceLocation();
        else
            openMainActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE && isGPSEnabled()) {
            Log.d(TAG, "onActivityResult: BC");
            getDeviceLocation();
        } else
            showAlertDialog();
    }

    private void openMainActivity() {
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }, 1000);
    }


    private void showDialog() {

        String message = "Please provide the location permission to use the app. Without allowing " +
                "the required permissions the app wouldn't start properly.";
        AlertDialogBox dialogBox = new AlertDialogBox(message, () ->
                ActivityCompat.requestPermissions(this, Permissions.PERMISSIONS, Permissions.PERMISSION_CODE));
        dialogBox.show(getSupportFragmentManager(), "ALERT_DIALOG");
    }

    @Override
    public void onTaskCompleted(CurrentLocation currentLocation) {

        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.putExtra(Constants.CURRENT_LOCATION, currentLocation);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }, 1000);
    }

    @Override
    public void onTaskFailed(String exception) {
        HelperMethods.showToastInCenter(this, exception).setDuration(Toast.LENGTH_LONG);
        openMainActivity();
    }
}