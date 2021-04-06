package com.example.theweathermate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.theweathermate.Utils.HelperMethods;
import com.example.theweathermate.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    ActivitySearchBinding viewBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinder = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(viewBinder.getRoot());


        viewBinder.button.setOnClickListener(v -> {
            HelperMethods.hideKeyboard(this);
            checkInputs();
        });
        viewBinder.imageView6.setOnClickListener(v -> finish());
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
}
