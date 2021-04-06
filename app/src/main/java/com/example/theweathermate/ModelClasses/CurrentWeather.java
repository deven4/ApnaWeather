package com.example.theweathermate.ModelClasses;

import java.util.List;

public class CurrentWeather {

    long dt;
    float uvi;
    float pop;
    int humidity;
    int pressure;
    long sunrise;
    long sunset;
    float temp;
    int visibility;
    float wind_speed;
    float dew_point;
    float feels_like;
    List<Weather> weather;

    public CurrentWeather(float pop, long dt, int uvi, int humidity, int pressure, long sunrise,
                          long sunset, float temp, int visibility, float wind_speed,
                          float dew_point, float feels_like, List<Weather> weather) {
        this.dt = dt;
        this.uvi = uvi;
        this.pop = pop;
        this.humidity = humidity;
        this.pressure = pressure;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.temp = temp;
        this.visibility = visibility;
        this.wind_speed = wind_speed;
        this.dew_point = dew_point;
        this.feels_like = feels_like;
        this.weather = weather;
    }

    public float getPop() {
        return pop;
    }

    public void setPop(float pop) {
        this.pop = pop;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public float getUvi() {
        return uvi;
    }

    public void setUvi(float uvi) {
        this.uvi = uvi;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public float getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(float wind_speed) {
        this.wind_speed = wind_speed;
    }

    public float getDew_point() {
        return dew_point;
    }

    public void setDew_point(float dew_point) {
        this.dew_point = dew_point;
    }

    public float getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(float feels_like) {
        this.feels_like = feels_like;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }
}
