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
Button airQuality,weather,account,settings;//creates variables for controlling the view
ImageView header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//sets the layout to activity_main.xml
        header = findViewById(R.id.header_image);//links variables to the element ID
        airQuality = findViewById(R.id.air_quality_button);
        weather = findViewById(R.id.current_weather_button);
        account = findViewById(R.id.account_button);
        settings = findViewById(R.id.settings_button);
        header.setImageResource(R.drawable.dustmo_logo_small);
        header.setContentDescription("Dustmo Home Screen");//sets content description for image
        airQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//sets on click listener to launch AirQualityActivity when button is pressed
                Intent i =new Intent (getBaseContext(), AirQualityActivity.class);
                startActivity(i);
            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//sets on click listener to launch WeatherActivity when button is pressed
                Intent i =new Intent (getBaseContext(), WeatherActivity.class);
                startActivity(i);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//sets on click listener to launch AccountActivity when button is pressed
                Intent i =new Intent (getBaseContext(), AccountActivity.class);
                startActivity(i);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//sets on click listener to launch SettingsActivity when button is pressed
                Intent i =new Intent (getBaseContext(), SettingsActivity.class);
                startActivity(i);
            }
        });
    }
}
