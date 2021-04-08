package com.example.theweathermate;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.theweathermate.ModelClasses.CurrentLocation;
import com.example.theweathermate.Utils.AlertDialogBox;
import com.example.theweathermate.Utils.Constants;
import com.example.theweathermate.Utils.DeviceLocation;
import com.example.theweathermate.Utils.HelperMethods;
import com.example.theweathermate.Utils.Permissions;

public class SplashScreen extends AppCompatActivity implements DeviceLocation.locationListener {

    private static final String TAG = "SPLASH_SCREEN";
    TextView fetchingLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        fetchingLocation = findViewById(R.id.textView);

        HelperMethods.setBlinkingText(fetchingLocation);

        if (Permissions.isPermissionsGranted(this))
            new DeviceLocation().getDeviceLocation(this, this);

        else
            ActivityCompat.requestPermissions(this, Permissions.PERMISSIONS, Permissions.PERMISSION_CODE);
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
            new DeviceLocation().getDeviceLocation(this, this);
        else
            openMainActivity();
    }


    private void openMainActivity() {
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        },1000);
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
        }, 2000);
    }

    @Override
    public void onTaskFailed(String exception) {
        HelperMethods.showToastInCenter(this, exception).setDuration(Toast.LENGTH_LONG);
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }, 2000);
    }
}