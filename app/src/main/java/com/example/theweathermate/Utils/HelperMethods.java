package com.example.theweathermate.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theweathermate.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.theweathermate.Utils.Constants.BROKEN_CLOUDS_DAY;
import static com.example.theweathermate.Utils.Constants.BROKEN_CLOUDS_NIGHT;
import static com.example.theweathermate.Utils.Constants.CLEAR_NIGHT;
import static com.example.theweathermate.Utils.Constants.CLOUDS_DAY;
import static com.example.theweathermate.Utils.Constants.CLOUDS_NIGHT;
import static com.example.theweathermate.Utils.Constants.MIST_DAY;
import static com.example.theweathermate.Utils.Constants.MIST_NIGHT;
import static com.example.theweathermate.Utils.Constants.RAIN_DAY;
import static com.example.theweathermate.Utils.Constants.RAIN_NIGHT;
import static com.example.theweathermate.Utils.Constants.SCATTERED_CLOUDS_DAY;
import static com.example.theweathermate.Utils.Constants.SCATTERED_CLOUDS_NIGHT;
import static com.example.theweathermate.Utils.Constants.SHOWER_RAIN_DAY;
import static com.example.theweathermate.Utils.Constants.SHOWER_RAIN_NIGHT;
import static com.example.theweathermate.Utils.Constants.SNOW_DAY;
import static com.example.theweathermate.Utils.Constants.SNOW_NIGHT;
import static com.example.theweathermate.Utils.Constants.THUNDERSTORM_DAY;
import static com.example.theweathermate.Utils.Constants.THUNDERSTORM_NIGHT;

public class HelperMethods {

    /**
     * Returns date in the format of Wed, July 4
     **/
    public static final String DATE_FORMAT = "EEE MMM dd";

    /**
     * Returns date in time eg: epoch time ---> 10 am
     **/
    public static final String TIME_FORMAT = "hh a";

    /**
     * Returns date in time eg: epoch time ---> 10am
     **/
    public static final String MIN_SEC_FORMAT = "h:mm";

    /**
     * Returns Date in format : Apr 06
     **/
    public static final String DAY_FORMAT = "MMM dd";


    public static String epochToDate(long epochTime, String dateFormat) {
        Date dateFromEpoch = new Date(epochTime * 1000L);
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return format.format(dateFromEpoch);
    }


    public static int KelvinToCelsius(float tempInKelvin) {
        float tempInCelsius = (float) (tempInKelvin - 273.15);
        return Math.round(tempInCelsius);
    }


    public static String capitalizeFirstLetter(String str) {

        char c = str.charAt(0);
        String newString = String.valueOf(c);
        return newString.toUpperCase().concat(str.substring(1));
    }


    public static void setIcon(Context context, ImageView imageView, String icon) {

        switch (icon) {
            case Constants.CLEAR_DAY:
                imageView.setImageResource(R.drawable.clear_sky);
                break;
            case CLEAR_NIGHT:
                imageView.setImageResource(R.drawable.ic_clear_night);
                break;
            case CLOUDS_DAY:
                imageView.setImageResource(R.drawable.ic_few_clouds_day);
                break;
            case CLOUDS_NIGHT:
                imageView.setImageResource(R.drawable.ic_few_clouds_night);
                break;
            case SCATTERED_CLOUDS_NIGHT:
            case SCATTERED_CLOUDS_DAY:
                imageView.setImageResource(R.drawable.ic_scattered_cloud);
                break;
            case BROKEN_CLOUDS_NIGHT:
            case BROKEN_CLOUDS_DAY:
                imageView.setImageResource(R.drawable.ic_broken_clouds);
                break;
            case SHOWER_RAIN_NIGHT:
            case SHOWER_RAIN_DAY:
                imageView.setImageResource(R.drawable.ic_shower_rain);
                break;
            case RAIN_DAY:
                imageView.setImageResource(R.drawable.ic_rain_day);
                break;
            case RAIN_NIGHT:
                imageView.setImageResource(R.drawable.ic_rain_night);
                break;
            case THUNDERSTORM_DAY:
            case THUNDERSTORM_NIGHT:
                imageView.setImageResource(R.drawable.ic_thuderstorm);
                break;
            case SNOW_DAY:
            case SNOW_NIGHT:
                imageView.setImageResource(R.drawable.ic_snow);
                break;
            case MIST_DAY:
            case MIST_NIGHT:
                imageView.setImageResource(R.drawable.ic_mist);
                break;
        }
    }


    public static void setBlinkingText(TextView textView) {
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        animation.setStartOffset(20);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        textView.startAnimation(animation);
    }


    public static void setBlinkingImg(ImageView imgView) {
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        animation.setStartOffset(20);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        imgView.startAnimation(animation);
    }


    public static void hideKeyboard(Activity activity) {

        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }


    public static Toast showToastInCenter(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }

    public static void showToastInCenter(Context context) {
        Toast toast = Toast.makeText(context, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
