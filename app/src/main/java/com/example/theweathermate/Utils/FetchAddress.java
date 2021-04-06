package com.example.theweathermate.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Worker Thread to fetch address from the current device location. Since this process of fetching
 * address is very time consuming, which can hang the main UI thread.
 **/
public class FetchAddress extends AsyncTask<Location, Void, Address> {

    Context context;
    workerThreadListener mListener;

    public FetchAddress(Context context, workerThreadListener mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    protected Address doInBackground(Location... locations) {

        List<Address> addresses;
        Location locationsResult = locations[0];

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(locationsResult.getLatitude(),
                    locationsResult.getLongitude(), 1);
            if (addresses == null || addresses.size() == 0)
                mListener.onTaskFailed("Unable to get your location (AddressLine is Empty).");
            else
                return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            mListener.onTaskFailed("Invalid coordinates were supplied to GeoCoder.");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Address address) {
        super.onPostExecute(address);
        mListener.onTaskCompleted(address);
    }

    public interface workerThreadListener {
        void onTaskCompleted(Address address);

        void onTaskFailed(String exception);
    }
}
