package com.example.weatherapp;

import java.util.List;

public class WeatherResponse {

    Main main;
    List<Weather> weather;

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public class Main {
        float temp;
        int humidity;

        public float getTemp() {
            return temp;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public class Weather {
        String main;

        public String getMain() {
            return main;
        }
    }
}
