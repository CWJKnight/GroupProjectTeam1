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

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;

public class WeatherActivity extends AppCompatActivity implements LocationListener {
    ConstraintLayout loadingView, dataView,errorView;//creates variables for controlling the view
    ImageView header,compass,weatherImg;
    TextView location,description,currentTemp,feelLike,minTemp,maxTemp,cloud,humidity,windSpeed,rainfall;
    Button refreshButton;
    double lat, lon;//used for storing latitude and longitude values
    String latString, lonString; //used for converting lat and lon to string values
    LocationManager lm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);//sets the layout to activity_weather.xml
        header = findViewById(R.id.header_image);//links variables to the element ID
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
        header.setImageResource(R.drawable.weather_logo_small);//Sets header image from drawable folder
        header.setContentDescription("Weather Screen");//sets content description for image
        getLocation();//runs getLocation to begin requesting data

        refreshButton.setOnClickListener(new View.OnClickListener() {//sets on click listener for the refresh button
            @Override
            public void onClick(View v) {
                loadingView.setVisibility(View.VISIBLE);//when refresh is pressed, the loading bar is made visible
                dataView.setVisibility(View.GONE);      //the data is hidden if displayed
                errorView.setVisibility(View.GONE);     //the error is hidden if displayed
                getLocation();//requests location update
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void getLocation() {
        //if permissions not already granted new request will be made.
        requestPermissions(new String[]{INTERNET,ACCESS_NETWORK_STATE,ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION,ACCESS_BACKGROUND_LOCATION},1);
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);//location manager requests location service
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 20,this);//requests location update from GPS
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);//gets the last known position from location manager
        lon = location.getLongitude();//stores longitude as double
        lat = location.getLatitude();//stores latitude as double
        lonString = String.valueOf(lon);//converts longitude to string value
        latString = String.valueOf(lat);//converts latitude to string value
        requestWeather();//launches requestWeather method
    }
    public void requestWeather(){
        final URLBuilder buildURL = new URLBuilder();//constructs new URLBuilder variable
        String URLstring = buildURL.newURL(1,latString,lonString);//requests url from URLBuilder with reason 1, inputsA set to latString and inputB set to lonString
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,//StringRequest requests new data from the constructed URL
                new com.android.volley.Response.Listener<String>() {//response listener waits for reply from server
                    @Override
                    public void onResponse(String response) {//if response is received store in String
                        try {
                                JSONObject obj = new JSONObject(response);//creates JSONObject from String "response"
                                JSONArray weatherObj = obj.getJSONArray("weather"); //JSON Arrays need to be extracted to JSONArray
                                JSONObject weatherArr = weatherObj.getJSONObject(0); //Converts the JSONArray index 0 to JSONObject
                                currentTemp.setText(new StringBuilder().append(obj.getJSONObject("main").getString("temp")).append("\u2103").toString()); //sets the temperature output from JSON and appends with degrees C symbol
                                feelLike.setText(new StringBuilder().append(obj.getJSONObject("main").getString("feels_like")).append("\u2103").toString());//sets the feels like temperature output from JSON
                                maxTemp.setText(new StringBuilder().append(obj.getJSONObject("main").getString("temp_max")).append("\u2103").toString());//sets the max temperature output from JSON
                                minTemp.setText(new StringBuilder().append(obj.getJSONObject("main").getString("temp_min")).append("\u2103").toString());//sets the minimum temperature output from JSON
                                humidity.setText(new StringBuilder().append(obj.getJSONObject("main").getString("humidity")).append("%"));//sets the humidity output from JSON and appends with %
                                cloud.setText(new StringBuilder().append(obj.getJSONObject("clouds").getString("all")).append("%").toString());//sets the cloud cover output from JSON and appends with %
                                if(obj.getJSONObject("wind").has("speed")){//checks if JSON contains the wind speed entry
                                windSpeed.setText(new StringBuilder().append(obj.getJSONObject("wind").getString("speed")).append(" m/s").toString());//sets the wind speed output from JSON
                                }else{
                                    windSpeed.setText(new StringBuilder().append("--").append(" m/s").toString()); //if no wind speed included set output to "--"
                                }
                                if(obj.getJSONObject("wind").has("deg")) {//checks if JSON contains wind direction
                                    compass.setRotation(obj.getJSONObject("wind").getInt("deg"));//sets image rotation from deg value from JSON
                                    compass.setVisibility(View.VISIBLE);//sets compass needle to visible
                                }else{
                                    compass.setVisibility(View.GONE);//if no wind direction, hides needle
                                    compass.setRotation(0);//sets direction to 0
                                }
                                location.setText(obj.get("name").toString());//sets the location output from JSON
                                description.setText(new StringBuilder().append("Description:  ").append(weatherArr.getString("description")).toString());//sets weather description
                                String imgCode = weatherArr.getString("icon");//gets weather icon image code from JSON and stores it
                                String imgURL = buildURL.newURL(2, imgCode, null); //URL for weather image is requested with stored image code
                                Picasso.get().load(imgURL).into(weatherImg); //Picasso is used to fetch ImageView src from URL to display current weather image
                                if(obj.has("rain"))//checks if JSON contains rain values
                                {
                                    if(obj.getJSONObject("rain").has("1h")){//checks if value is 1h
                                    rainfall.setText(new StringBuilder().append(obj.getJSONObject("rain").getString("1h")).append("mm").toString());
                                    }
                                    if(obj.getJSONObject("rain").has("3h")){//checks if value is 3h
                                        rainfall.setText(new StringBuilder().append(obj.getJSONObject("rain").getString("3h")).append("mm").toString());
                                    }
                                }else{
                                    rainfall.setText("0mm");//if no rain data in JSON set rainfall to "0mm"
                                }
                            loadingView.setVisibility(View.GONE);//loading is hidden
                            dataView.setVisibility(View.VISIBLE);//data is displayed
                            errorView.setVisibility(View.GONE);//error is hidden
                    } catch (JSONException e) {//catches error parsing JSON
                            e.printStackTrace();
                            loadingView.setVisibility(View.GONE);   //if error parsing JSON, loading is hidden
                            dataView.setVisibility(View.GONE);      //data is hidden
                            errorView.setVisibility(View.VISIBLE);  //error message is displayed
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {//listens for error in volley request
                        error.printStackTrace();
                        loadingView.setVisibility(View.GONE);   //if error retrieving JSON, loading is hidden
                        dataView.setVisibility(View.GONE);      //data is hidden
                        errorView.setVisibility(View.VISIBLE);  //error message is displayed
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this); //creates new volley request queue
        requestQueue.add(stringRequest);//adds new request to queue

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
