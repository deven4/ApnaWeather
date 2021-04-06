package com.example.theweathermate.ModelClasses;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentLocation implements Parcelable {

    String city;
    String state;
    double latitude;
    double longitude;
    String addressLine;

    public CurrentLocation(String city, String state, double latitude,
                           double longitude, String addressLine) {
        this.city = city;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressLine = addressLine;
    }

    protected CurrentLocation(Parcel in) {
        city = in.readString();
        state = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        addressLine = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(state);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(addressLine);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CurrentLocation> CREATOR = new Creator<CurrentLocation>() {
        @Override
        public CurrentLocation createFromParcel(Parcel in) {
            return new CurrentLocation(in);
        }

        @Override
        public CurrentLocation[] newArray(int size) {
            return new CurrentLocation[size];
        }
    };

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }
}
