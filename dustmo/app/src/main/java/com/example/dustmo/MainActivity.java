package com.example.dustmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dustmo.account.AccountActivity;
import com.example.dustmo.airquality.AirQualityActivity;
import com.example.dustmo.settings.SettingsActivity;
import com.example.dustmo.weather.WeatherActivity;

public class MainActivity extends AppCompatActivity {
Button airQuality,weather,account,settings;
ImageView header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        header = findViewById(R.id.header_image);
        airQuality = findViewById(R.id.air_quality_button);
        weather = findViewById(R.id.current_weather_button);
        account = findViewById(R.id.account_button);
        settings = findViewById(R.id.settings_button);

        header.setImageResource(R.drawable.dustmo_logo_small);
        header.setContentDescription("Dustmo Home Screen");
        airQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent (getBaseContext(), AirQualityActivity.class);
                startActivity(i);
            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent (getBaseContext(), WeatherActivity.class);
                startActivity(i);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent (getBaseContext(), AccountActivity.class);
                startActivity(i);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent (getBaseContext(), SettingsActivity.class);
                startActivity(i);
            }
        });
    }
}
