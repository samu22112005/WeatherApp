package com.example.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("weather")
    Call<WeatherResponse> getWeather(
            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units
    );

    // ✅ ADD THIS METHOD (THIS IS MISSING)
    @GET("weather")
    Call<WeatherResponse> getWeatherByLocation(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units
    );
}