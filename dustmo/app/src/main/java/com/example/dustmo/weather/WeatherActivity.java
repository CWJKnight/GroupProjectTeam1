package com.example.dustmo.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dustmo.R;
import com.example.dustmo.external.URLBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity implements LocationListener {
    ConstraintLayout loadingView, dataView,errorView;
    ImageView header,compass,weatherImg;
    TextView location,description,currentTemp,feelLike,minTemp,maxTemp,cloud,humidity,windSpeed,rainfall;
    double lat, lon;
    String latString, lonString;
    Button refreshButton;
    LocationManager lm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        header = findViewById(R.id.header_image);
        header.setImageResource(R.drawable.weather_logo_small);
        header.setContentDescription("Weather Screen");
        loadingView = findViewById(R.id.weather_loading);
        dataView = findViewById(R.id.weather_display);
        errorView = findViewById(R.id.weather_error);
        compass = findViewById(R.id.wind_direction_output);
        location = findViewById(R.id.location_title);
        description = findViewById(R.id.description_title);
        currentTemp = findViewById(R.id.temp_output);
        feelLike = findViewById(R.id.feels_like_output);
        minTemp = findViewById(R.id.min_temp_output);
        maxTemp = findViewById(R.id.max_temp_output);
        cloud = findViewById(R.id.cloud_output);
        humidity = findViewById(R.id.humidity_output);
        windSpeed = findViewById(R.id.wind_speed_output);
        weatherImg = findViewById(R.id.weather_image);
        refreshButton = findViewById(R.id.refresh_button);
        rainfall = findViewById(R.id.rainfall_output);
        getLocation();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingView.setVisibility(View.VISIBLE);
                dataView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                getLocation();
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void getLocation() {
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 20,this);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lon = location.getLongitude();
        lat = location.getLatitude();
        lonString = String.valueOf(lon);
        latString = String.valueOf(lat);
        requestWeather(latString,lonString);
    }
    public void requestWeather(String lat, String lon){
        final URLBuilder buildURL = new URLBuilder();
        String URLstring = buildURL.newURL(1,latString,lonString);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            String result = obj.get("cod").toString();
                            if (result.matches("200")) {
                                JSONArray weatherObj = obj.getJSONArray("weather"); //JSON Arrays need to be extracted to JSONArray
                                JSONObject weatherArr = weatherObj.getJSONObject(0); //Converts the JSONArray index 0 to JSONObject
                                currentTemp.setText(new StringBuilder().append(obj.getJSONObject("main").getString("temp")).append("\u2103").toString()); //TextViews Strings are built with data from the JSONObject
                                feelLike.setText(new StringBuilder().append(obj.getJSONObject("main").getString("feels_like")).append("\u2103").toString());
                                maxTemp.setText(new StringBuilder().append(obj.getJSONObject("main").getString("temp_max")).append("\u2103").toString());
                                minTemp.setText(new StringBuilder().append(obj.getJSONObject("main").getString("temp_min")).append("\u2103").toString());
                                humidity.setText(new StringBuilder().append(obj.getJSONObject("main").getString("humidity")).append("%"));
                                cloud.setText(new StringBuilder().append(obj.getJSONObject("clouds").getString("all")).append("%").toString());
                                if(obj.getJSONObject("wind").has("speed")){
                                windSpeed.setText(new StringBuilder().append(obj.getJSONObject("wind").getString("speed")).append(" m/s").toString());
                                }else{
                                    windSpeed.setText(new StringBuilder().append("0").append(" m/s").toString());
                                }
                                if(obj.getJSONObject("wind").has("deg")) {
                                    compass.setRotation(obj.getJSONObject("wind").getInt("deg"));
                                }else{
                                    compass.setRotation(0);
                                }
                                location.setText(obj.get("name").toString());
                                description.setText(new StringBuilder().append("Description:  ").append(weatherArr.getString("description")).toString());
                                String imgCode = weatherArr.getString("icon");
                                String imgURL = buildURL.newURL(2, imgCode, null); //URL for weather image is requested
                                Picasso.get().load(imgURL).into(weatherImg); //Picasso is used to fetch ImageView src from URL to display current weather image
                                if(obj.has("rain")){
                                    if(obj.getJSONObject("rain").has("1h")){
                                    rainfall.setText(new StringBuilder().append(obj.getJSONObject("rain").getString("1h")).append("mm").toString());
                                    }
                                    if(obj.getJSONObject("rain").has("3h")){
                                        rainfall.setText(new StringBuilder().append(obj.getJSONObject("rain").getString("3h")).append("mm").toString());
                                    }

                                }else{
                                    rainfall.setText("0mm");
                                }
                        }
                            loadingView.setVisibility(View.GONE);
                            dataView.setVisibility(View.VISIBLE);
                            errorView.setVisibility(View.GONE);
                    } catch (JSONException e) {
                            e.printStackTrace();
                            loadingView.setVisibility(View.GONE);
                            dataView.setVisibility(View.GONE);
                            errorView.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //displaying the error in toast if occurrs
                        loadingView.setVisibility(View.GONE);
                        dataView.setVisibility(View.GONE);
                        errorView.setVisibility(View.VISIBLE);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this); //requests new volley request to be queued
        requestQueue.add(stringRequest);

    }

    @Override //if location is changed update setLat, setLon in CurrentWeatherDataModel
    public void onLocationChanged(Location location) {
        lon = location.getLongitude();
        lat = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
