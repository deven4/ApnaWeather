package com.example.theweathermate.ModelClasses;

import java.util.List;

public class WeatherData {

    double lat;
    double lon;
    String timezone;
    double timezone_offset;
    CurrentWeather current;
    List<CurrentWeather> hourly;
    List<DailyWeather> daily;

    public WeatherData(double lat, double lon, String timezone, double timezone_offset,
                       CurrentWeather current, List<CurrentWeather> hourly,
                       List<DailyWeather> daily) {
        this.lat = lat;
        this.lon = lon;
        this.timezone = timezone;
        this.timezone_offset = timezone_offset;
        this.current = current;
        this.hourly = hourly;
        this.daily = daily;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public double getTimezone_offset() {
        return timezone_offset;
    }

    public void setTimezone_offset(double timezone_offset) {
        this.timezone_offset = timezone_offset;
    }

    public CurrentWeather getCurrent() {
        return current;
    }

    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }

    public List<CurrentWeather> getHourly() {
        return hourly;
    }

    public void setHourly(List<CurrentWeather> hourly) {
        this.hourly = hourly;
    }

    public List<DailyWeather> getDaily() {
        return daily;
    }

    public void setDaily(List<DailyWeather> daily) {
        this.daily = daily;
    }
}
