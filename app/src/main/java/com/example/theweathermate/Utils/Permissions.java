package com.example.theweathermate.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class Permissions {

    public static final int PERMISSION_CODE = 101;

    public static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static boolean isPermissionsGranted(Context context) {
        for (String per : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(context,per) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }
}
