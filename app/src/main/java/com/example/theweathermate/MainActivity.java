package com.example.theweathermate;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.theweathermate.Adapters.ForecastRecyclerViewAdapt;
import com.example.theweathermate.Adapters.HourlyWeatherAdapter;
import com.example.theweathermate.ModelClasses.CurrentLocation;
import com.example.theweathermate.ModelClasses.CurrentWeather;
import com.example.theweathermate.ModelClasses.DailyWeather;
import com.example.theweathermate.ModelClasses.Weather;
import com.example.theweathermate.ModelClasses.WeatherData;
import com.example.theweathermate.Utils.Constants;
import com.example.theweathermate.Utils.HelperMethods;
import com.example.theweathermate.databinding.ActivityMainBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.theweathermate.Utils.Constants.REQUEST_CODE;

public class MainActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "MAIN_ACTIVITY";

    String URL_ADDRESS;

    BroadcastReceiver receiver;
    ActivityMainBinding viewBinder;
    CurrentLocation currentLocation;
    NestedScrollView nestedScrollView;
    ForecastRecyclerViewAdapt forecastAdapt;
    HourlyWeatherAdapter hourlyWeatherAdapter;
    LocalBroadcastManager localBroadcastManager;
    BottomSheetBehavior<View> bottomSheetBehavior;
    List<DailyWeather> dailyWeatherList = new ArrayList<>();
    List<CurrentWeather> hourlyWeatherList = new ArrayList<>();
    LinearLayout locationLayout, tempLayout, cardViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinder = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinder.getRoot());

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        locationLayout = findViewById(R.id.linearLayout);
        tempLayout = findViewById(R.id.linearLayout2);
        cardViewLayout = findViewById(R.id.linearLayout3);
        nestedScrollView = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollView);


        HelperMethods.setBlinkingImg(viewBinder.imageView4);
        getIntentData();
        setListeners();
        setAdapters();
    }


    private void getIntentData() {

        Intent intent = getIntent();
        currentLocation = intent.getParcelableExtra(Constants.CURRENT_LOCATION);
        if (currentLocation != null)
            setLocation();
        else
            setDefaultLocation();
    }


    private void setDefaultLocation() {

        viewBinder.locationTxt.setText(getString(R.string.CITY_STATE, "CSMT", "Mumbai"));
        URL_ADDRESS = "https://api.openweathermap.org/data/2.5/onecall?" +
                "lat=18.94014901367893&lon=72.83546896156089&appid=27963459e042df3c8a7bf504d8c6abe1";
    }


    private void setLocation() {

        viewBinder.locationTxt.setText(getString(R.string.CITY_STATE, currentLocation.getCity(),
                currentLocation.getState()));
        URL_ADDRESS = "https://api.openweathermap.org/data/2.5/onecall?" +
                "lat=" + currentLocation.getLatitude() + "&lon=" + currentLocation.getLongitude()
                + "&appid=27963459e042df3c8a7bf504d8c6abe1";
    }


    private void getData() {

        Intent intent = new Intent(this, MyIntentService.class);
        intent.setData(Uri.parse(URL_ADDRESS));
        MyIntentService.enqueueWork(this, intent);
    }


    private void setAdapters() {

        // Forecast RecyclerView Adapter
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        forecastAdapt = new ForecastRecyclerViewAdapt(this, dailyWeatherList);
        viewBinder.recyclerView2.addItemDecoration(dividerItemDecoration);
        viewBinder.recyclerView2.setLayoutManager(layoutManager);
        viewBinder.recyclerView2.setAdapter(forecastAdapt);

        // Hourly Forecast Adapter
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        hourlyWeatherAdapter = new HourlyWeatherAdapter(this, hourlyWeatherList);
        viewBinder.recyclerView.setLayoutManager(layoutManager1);
        viewBinder.recyclerView.setAdapter(hourlyWeatherAdapter);
    }


    private void getCardLayoutHeight() {

        ViewTreeObserver viewTreeObserver = cardViewLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(this);
        }
    }


    private void setListeners() {

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra(MyIntentService.SERVICE_PAYLOAD);
                Gson gson = new Gson();
                WeatherData weatherData = gson.fromJson(msg, WeatherData.class);
                viewBinder.swipeRefreshLayout.setRefreshing(false);
                if (weatherData != null) {
                    setWidgets(weatherData);
                } else
                    apiCallFailed();
            }
        };

        viewBinder.locationTxt.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        });
        viewBinder.imageView5.setOnClickListener(v -> HelperMethods.showToastInCenter(this, "Settings : not implemented."));
        viewBinder.swipeRefreshLayout.setOnRefreshListener(this::getData);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                viewBinder.swipeRefreshLayout.setEnabled(newState == BottomSheetBehavior.STATE_COLLAPSED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }


    private void setWidgets(WeatherData weatherData) {

        CurrentWeather currentWeather = weatherData.getCurrent();

        viewBinder.currentTemp.setText(getString(R.string.CURRENT_TEMP,
                HelperMethods.KelvinToCelsius(currentWeather.getTemp())));
        viewBinder.feelsLike.setText(getString(R.string.FEELS_LIKE_TEMP,
                HelperMethods.KelvinToCelsius(currentWeather.getFeels_like())));
        viewBinder.weatherDesc.setText(getString(R.string.WEATHER_DESC,
                HelperMethods.capitalizeFirstLetter
                        (currentWeather.getWeather().get(0).getDescription())));
        HelperMethods.setIcon(this, viewBinder.currentWeatherIcon, currentWeather.getWeather().get(0).getIcon());

        // CardView Widgets
        viewBinder.windSpeed.setText(getString(R.string.WIND_SPEED, currentWeather.getWind_speed()));
        viewBinder.humidity.setText(getString(R.string.HUMIDITY, currentWeather.getHumidity()));
        viewBinder.pressure.setText(getString(R.string.PRESSURE, currentWeather.getPressure()));
        viewBinder.uvIndex.setText(getString(R.string.UI_INDEX, currentWeather.getUvi()));
        viewBinder.visibility.setText(getString(R.string.VISIBILITY,
                (float) currentWeather.getVisibility() / 1000));
        viewBinder.dewPoint.setText(getString(R.string.DEW_POINT, HelperMethods.KelvinToCelsius(currentWeather.getDew_point())));

        // Forecast RecyclerView
        dailyWeatherList.clear();
        dailyWeatherList.addAll(weatherData.getDaily());
        forecastAdapt.notifyDataSetChanged();

        // Hourly RecyclerView
        hourlyWeatherList.clear();
        hourlyWeatherList.addAll(weatherData.getHourly());
        addSunsetSunrise();
        hourlyWeatherAdapter.notifyDataSetChanged();

        getCardLayoutHeight();


        viewBinder.swipeRefreshLayout.setVisibility(View.VISIBLE);
        viewBinder.progressBar.setVisibility(View.GONE);

        checkHourlyList();
    }


    private void checkHourlyList() {

        for (CurrentWeather ob : hourlyWeatherList)
            Log.d(TAG, "checkHourlyList: " + HelperMethods.epochToDate(ob.getDt(), HelperMethods.TIME_FORMAT));
    }


    private void addSunsetSunrise() {

        List<CurrentWeather> dummyList = new ArrayList<>(hourlyWeatherList);
        for (DailyWeather dailyWeather : dailyWeatherList) {

            for (int position = 0; position < dummyList.size(); position++) {

                if (position < dummyList.size() - 1) {
                    CurrentWeather currWeather = dummyList.get(position);
                    CurrentWeather nextWeather = dummyList.get(position + 1);

                    if (HelperMethods.epochToDate(dailyWeather.getDt(), HelperMethods.DAY_FORMAT)
                            .equals(HelperMethods.epochToDate(currWeather.getDt(), HelperMethods.DAY_FORMAT))) {

                        if (dailyWeather.getSunset() >= currWeather.getDt()
                                && dailyWeather.getSunset() <= nextWeather.getDt()) {

                            insertElementIntoHourlyList(position + 1, dailyWeather.getSunset(), false);
                        } else if (dailyWeather.getSunrise() >= currWeather.getDt()
                                && dailyWeather.getSunrise() <= nextWeather.getDt()) {

                            insertElementIntoHourlyList(position + 1, dailyWeather.getSunrise(), true);
                        }
                    }
                }
            }
        }

        Collections.sort(hourlyWeatherList, (o1, o2) -> Long.compare(o1.getDt(), o2.getDt()));
    }


    private void insertElementIntoHourlyList(int position, long sunrise, boolean value) {

        List<Weather> weatherList = new ArrayList<>();
        Weather weather;
        if (value) {
            weather = new Weather(250, Constants.SUNRISE, "", "");
        } else
            weather = new Weather(250, Constants.SUNSET, "", "");
        weatherList.add(weather);

        hourlyWeatherList.add(position, new CurrentWeather(0, sunrise,
                0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, weatherList));
    }


    public void apiCallFailed() {

        if (currentLocation != null)
            viewBinder.locationTxt.setText(getString(R.string.CITY_STATE, currentLocation.getCity(),
                    currentLocation.getState()));
        HelperMethods.showToastInCenter(MainActivity.this, "Unable to fetch data!")
                .setDuration(Toast.LENGTH_LONG);
        if (viewBinder.progressBar.getVisibility() == View.VISIBLE) {
            if (viewBinder.imageView4.getAnimation() != null)
                viewBinder.imageView4.getAnimation().cancel();
            viewBinder.textView9.setText("Something went wrong! Retry.");
            viewBinder.textView9.setTextSize(20);
            viewBinder.textView9.setTextColor(getColor(R.color.black));
            viewBinder.textView9.setOnClickListener(v -> {
                viewBinder.swipeRefreshLayout.post(() -> viewBinder.swipeRefreshLayout.setRefreshing(true));
                getData();
            });
        }
    }


    /**
     * Dynamically set BottomSheet's peek height so the CardView doesn't get overlapped with bottom
     * sheet. Since the Bottom is not hideAble.
     * Getting the root layout height once the layout is drawn, and then getting the cardView layout
     * location on screen also the height of cardView layout so to get the minimum  height to set on
     * the bottom sheet, so that it doesn't get overlapped.
     **/
    @Override
    public void onGlobalLayout() {
        float cardHeight = cardViewLayout.getHeight();

        if (cardHeight != 0) {
            int[] location = new int[2];
            cardViewLayout.getLocationOnScreen(location);
            float peekHeight = viewBinder.rootLayout.getHeight() - (cardHeight + location[1]);
            bottomSheetBehavior.setPeekHeight((int) peekHeight);

            Log.d(TAG, "root Layout height: " + viewBinder.rootLayout.getHeight());
            Log.d(TAG, "card Layout location on screen x: " + location[0]);
            Log.d(TAG, "card Layout location on screen y: " + location[1]);
            Log.d(TAG, "card Layout location on screen cardHeight: " + cardHeight);
            Log.d(TAG, "card Layout height till bottom from top: " + (cardHeight + location[1]));
            Log.d(TAG, "var peek Height: " + peekHeight);
            Log.d(TAG, "var peek Height (in dp): " + convertPixelsToDp(peekHeight));
            Log.d(TAG, "onGlobalLayout getPeekHeight (in DP): " + convertPixelsToDp(bottomSheetBehavior.getPeekHeight()));
        } else
            bottomSheetBehavior.setPeekHeight(150);

        cardViewLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }


    public float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            double lat = data.getDoubleExtra(SearchActivity.LATITUDE, 0);
            double log = data.getDoubleExtra(SearchActivity.LONGITUDE, 0);

            viewBinder.locationTxt.setText(lat + ", " + log);
            URL_ADDRESS = "https://api.openweathermap.org/data/2.5/onecall?" +
                    "lat=" + lat + "&lon=" + log
                    + "&appid=27963459e042df3c8a7bf504d8c6abe1";
            viewBinder.swipeRefreshLayout.setRefreshing(true);
            getData();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        localBroadcastManager.registerReceiver(receiver, new IntentFilter(MyIntentService.ACTION));
        getData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        localBroadcastManager.unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            nestedScrollView.smoothScrollTo(0, 0);
        } else
            super.onBackPressed();
    }
}