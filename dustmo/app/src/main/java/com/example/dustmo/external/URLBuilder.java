package com.example.dustmo.external;


public class URLBuilder {
        private String sensor_api = "http://dustmo.com/api/sensors";//URL for air quality sensor data
        private String weather_url = "http://api.openweathermap.org/data/2.5/weather";//Base URL for current weather data API
        private String weather_api_key = "b22765d57f287a740aa4bfcccca160f8";//API key for weather data
        private String weather_img_url = "http://openweathermap.org/img/wn/";//Base URL for weather icons
    public String newURL (int reason, String inputA, String inputB){//newURL will return a String when called, reason determines the URL to be constructed
        StringBuilder builtURL = new StringBuilder();//builtURL will be used to store the URL as it is constructed by StringBuilder
        if(reason == 1){//if reason equals 1
            builtURL.append(weather_url);//first uses the base URL for the weather API
            builtURL.append("?lat=");//Adds the tag for latitude
            builtURL.append(inputA);//adds the latitude value from inputA
            builtURL.append("&lon=");//Adds the tag for longitude
            builtURL.append(inputB);//Adds the longitude value from inputB
            builtURL.append("&APPID=");//Adds the API key tag to the url
            builtURL.append(weather_api_key);//Adds the API key to the url
            builtURL.append("&units=metric");//Adds the units tag, currently set to metric
          }
        if(reason == 2){//if reason equals 2
            builtURL.append(weather_img_url);//First uses the base URL for weather icons
            builtURL.append(inputA);//Adds the icon code from inputA, creating the first part of the file name
            builtURL.append("@2x.png");//completes the file name of the icon
        }
        if (reason == 3){//if reason equals 3
            builtURL.append(sensor_api);//Adds the sensor data URL
        }

        return builtURL.toString();//returns constructed url as String
    }
}
