package com.example.dustmo.airquality;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AirQualityActivity extends AppCompatActivity {
    ConstraintLayout loadingView, dataView,errorView;//creates variables for controlling the view
    ImageView header;
    TextView temperature, humidity, airPressure, altitude;
    Button refreshButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_quality);//sets the layout to activity_air_quality.xml
        header = findViewById(R.id.header_image);//links variables to the element ID
        loadingView = findViewById(R.id.air_quality_loading);
        dataView = findViewById(R.id.air_quality_display);
        errorView = findViewById(R.id.air_quality_error);
        temperature = findViewById(R.id.sensor_temp_output);
        humidity = findViewById(R.id.sensor_humidity_output);
        airPressure = findViewById(R.id.sensor_pressure_output);
        altitude = findViewById(R.id.sensor_altitude_output);
        refreshButton=findViewById(R.id.sensor_refresh);
        header.setImageResource(R.drawable.airquality_logo_small);//Sets header image from drawable folder
        header.setContentDescription("Air Quality Screen");//sets content description for image
        requestSensorData();//requests first update from the sensor
        refreshButton.setOnClickListener(new View.OnClickListener() {//sets on click listener for the refresh button
            @Override
            public void onClick(View v) {
                loadingView.setVisibility(View.VISIBLE);//when refresh is pressed, the loading bar is made visible
                dataView.setVisibility(View.GONE);      //the data is hidden if displayed
                errorView.setVisibility(View.GONE);     //the error is hidden if displayed
                requestSensorData();//requests new sensor data
            }
        });
    }
    private void requestSensorData(){
        final URLBuilder buildURL = new URLBuilder();//constructs new URLBuilder variable
        String URLstring = buildURL.newURL(3,null,null);//requests url from URLBuilder with reason 3, inputs set to null
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring, //StringRequest requests new data from the constructed URL
                new com.android.volley.Response.Listener<String>() {//response listener waits for reply from server
                    @Override
                    public void onResponse(String response) {//if response is received store in String
                        try {
                            int i;//variable used for counting array size
                            int arrayLength =0;//used for storing array size
                            JSONObject obj = new JSONObject(response);//creates JSONObject from String "response"
                            JSONArray sensorObj = obj.getJSONArray("employees"); //JSON Arrays need to be extracted to JSONArray
                            for(i=0; i<sensorObj.length();i++){//finds size of JSONArray
                                arrayLength = i-1;//i-1 = final position (-1 for 0 counting) used to find latest update from sensor
                            }
                            JSONObject sensorArr = sensorObj.getJSONObject(arrayLength); //Converts the JSONArray index equal to arrayLength variable to JSONObject
                            temperature.setText(new StringBuilder().append(sensorArr.getString("temperature")).append("\u2103").toString());//sets the temperature output from JSON and appends with degrees C symbol
                            humidity.setText(new StringBuilder().append(sensorArr.getString("humidity")).append("%").toString());//sets the humidity output from JSON and appends with % symbol
                            airPressure.setText(new StringBuilder().append(sensorArr.getString("pressure")).append("hPa").toString());//sets the air pressure output from JSON and appends with hPa to indicate units
                            altitude.setText(new StringBuilder().append(sensorArr.getString("altitude")).append("m").toString());//sets the altitude output from JSON and appends with m to indicate units
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
}
