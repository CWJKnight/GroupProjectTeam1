package com.example.dustmo.airquality;

import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AirQualityActivity extends AppCompatActivity {
    ConstraintLayout loadingView, dataView,errorView;
    ImageView header;
    TextView temperature, humidity, airPressure, altitude;
    Button refreshButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_quality);
        header = findViewById(R.id.header_image);
        header.setImageResource(R.drawable.airquality_logo_small);
        header.setContentDescription("Air Quality Screen");
        loadingView = findViewById(R.id.air_quality_loading);
        dataView = findViewById(R.id.air_quality_display);
        errorView = findViewById(R.id.air_quality_error);
        temperature = findViewById(R.id.sensor_temp_output);
        humidity = findViewById(R.id.sensor_humidity_output);
        airPressure = findViewById(R.id.sensor_pressure_output);
        altitude = findViewById(R.id.sensor_altitude_output);
        refreshButton=findViewById(R.id.sensor_refresh);
        requestSensorData();
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingView.setVisibility(View.VISIBLE);
                dataView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                requestSensorData();
            }
        });
    }
    private void requestSensorData(){
        final URLBuilder buildURL = new URLBuilder();
        String URLstring = buildURL.newURL(3,null,null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int i;
                            int arrayLength =0;
                            JSONObject obj = new JSONObject(response);
                            JSONArray sensorObj = obj.getJSONArray("employees"); //JSON Arrays need to be extracted to JSONArray
                            for(i=0; i<sensorObj.length();i++){
                                arrayLength = i-1;
                            }
                            JSONObject sensorArr = sensorObj.getJSONObject(arrayLength); //Converts the JSONArray index 0 to JSONObject
                            Log.d("sensorJSON", ">>"+sensorArr.getString("temperature"));
                            temperature.setText(new StringBuilder().append(sensorArr.getString("temperature")).append("\u2103").toString());
                            humidity.setText(new StringBuilder().append(sensorArr.getString("humidity")).append("%").toString());
                            airPressure.setText(new StringBuilder().append(sensorArr.getString("pressure")).append("hPa").toString());
                            altitude.setText(new StringBuilder().append(sensorArr.getString("altitude")).append("m").toString());
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
}
