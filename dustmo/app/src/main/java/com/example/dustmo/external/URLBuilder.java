package com.example.dustmo.external;


public class URLBuilder {
        private String sensor_api = "http://dustmo.com/api/sensors";
        private String weather_url = "http://api.openweathermap.org/data/2.5/weather";
        private String weather_api_key = "b22765d57f287a740aa4bfcccca160f8";
        private String weather_img_url = "http://openweathermap.org/img/wn/";
    public String newURL (int reason, String inputA, String inputB){
        StringBuilder builtURL = new StringBuilder();
        if(reason == 1){

            builtURL.append(weather_url);
            builtURL.append("?lat=");
            builtURL.append(inputA);
            builtURL.append("&lon=");
            builtURL.append(inputB);
            builtURL.append("&APPID=");
            builtURL.append(weather_api_key);
            builtURL.append("&units=metric");
          }
        if(reason == 2){
            builtURL.append(weather_img_url);
            builtURL.append(inputA);
            builtURL.append("@2x.png");
        }
        if (reason == 3){
            builtURL.append(sensor_api);
        }

        return builtURL.toString();
    }
}
