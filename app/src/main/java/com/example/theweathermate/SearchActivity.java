package com.example.theweathermate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.theweathermate.ModelClasses.CurrentLocation;
import com.example.theweathermate.Utils.DeviceLocation;
import com.example.theweathermate.Utils.HelperMethods;
import com.example.theweathermate.Utils.LoadingDialog;
import com.example.theweathermate.Utils.Permissions;
import com.example.theweathermate.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity implements DeviceLocation.locationListener {

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String CITY = "CITY";
    public static final String STATE = "STATE";

    LoadingDialog loadingDialog;
    ActivitySearchBinding viewBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinder = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(viewBinder.getRoot());

        loadingDialog = new LoadingDialog("Getting your Location...");

        viewBinder.button.setOnClickListener(v -> {
            HelperMethods.hideKeyboard(this);
            checkInputs();
        });
        viewBinder.imageView6.setOnClickListener(v -> finish());
        viewBinder.linearLayout4.setOnClickListener(v -> {
            if (Permissions.isPermissionsGranted(this)) {
                loadingDialog.show(getSupportFragmentManager(), "");
                new DeviceLocation().getDeviceLocation(this, this);
            } else
                requestPermissions(Permissions.PERMISSIONS, 101);
        });
    }


    private void checkInputs() {

        String lat = viewBinder.editTextTextPersonName.getText().toString();
        String log = viewBinder.editTextTextPersonName2.getText().toString();

        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        try {
            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(log);
            intent.putExtra(LATITUDE, latitude);
            intent.putExtra(LONGITUDE, longitude);
            setResult(RESULT_OK, intent);
            finish();
        } catch (NumberFormatException e) {
            HelperMethods.showToastInCenter(this, "Invalid Coordinates Entered!");
        }
    }

    @Override
    public void onTaskCompleted(CurrentLocation currentLocation) {
        if (currentLocation != null) {
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
            intent.putExtra(LATITUDE, currentLocation.getLatitude());
            intent.putExtra(LONGITUDE, currentLocation.getLongitude());
            intent.putExtra(CITY, currentLocation.getCity());
            intent.putExtra(STATE, currentLocation.getState());
            setResult(RESULT_OK, intent);
            finish();
        } else
            HelperMethods.showToastInCenter(this, "Unable to get your device's current location.");
        loadingDialog.dismiss();
    }

    @Override
    public void onTaskFailed(String exception) {
        HelperMethods.showToastInCenter(this, exception);
        loadingDialog.dismiss();
    }
}
