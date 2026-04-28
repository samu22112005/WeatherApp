package com.example.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.*;

import retrofit2.*;

public class MainActivity extends AppCompatActivity {

    EditText cityInput;
    Button getWeatherBtn;
    TextView resultText;
    ImageView weatherIcon;

    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityInput = findViewById(R.id.cityInput);
        getWeatherBtn = findViewById(R.id.getWeatherBtn);
        resultText = findViewById(R.id.resultText);
        weatherIcon = findViewById(R.id.weatherIcon);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getWeatherBtn.setOnClickListener(v -> {
            String city = cityInput.getText().toString().trim();

            if (!city.isEmpty()) {
                fetchWeather(city);
            } else {
                getLocationWeather(); // 📍 AUTO LOCATION
            }
        });
    }

    // 🔵 CITY WEATHER
    private void fetchWeather(String city) {

        WeatherApi api = RetrofitClient.getClient().create(WeatherApi.class);

        Call<WeatherResponse> call = api.getWeather(
                city,
                "ba50e437d81c05fe7347f0d2a05a8bee",
                "metric"
        );

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    float temp = response.body().getMain().getTemp();
                    int humidity = response.body().getMain().getHumidity();
                    String condition = response.body().getWeather().get(0).getMain();

                    setWeatherIcon(condition);

                    resultText.setText(
                            "🌡 Temp: " + temp + "°C\n" +
                                    "💧 Humidity: " + humidity + "%\n" +
                                    "🌤 " + condition
                    );

                } else {
                    resultText.setText("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                resultText.setText("Failed: " + t.getMessage());
            }
        });
    }

    // 📍 LOCATION WEATHER
    private void getLocationWeather() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        resultText.setText("Getting location...");

        fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, null
        ).addOnSuccessListener(location -> {

            if (location != null) {
                fetchWeatherByLocation(location.getLatitude(), location.getLongitude());
            } else {
                resultText.setText("Location not found. Turn on GPS.");
            }
        });
    }

    // 🌍 FETCH WEATHER BY LAT/LON
    private void fetchWeatherByLocation(double lat, double lon) {

        WeatherApi api = RetrofitClient.getClient().create(WeatherApi.class);

        Call<WeatherResponse> call = api.getWeatherByLocation(
                lat,
                lon,
                "ba50e437d81c05fe7347f0d2a05a8bee",
                "metric"
        );

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    float temp = response.body().getMain().getTemp();
                    int humidity = response.body().getMain().getHumidity();
                    String condition = response.body().getWeather().get(0).getMain();

                    setWeatherIcon(condition);

                    resultText.setText(
                            "📍 Your Location\n" +
                                    "🌡 Temp: " + temp + "°C\n" +
                                    "💧 Humidity: " + humidity + "%\n" +
                                    "🌤 " + condition
                    );

                } else {
                    resultText.setText("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                resultText.setText("Failed: " + t.getMessage());
            }
        });
    }

    // 🎨 ICON LOGIC
    private void setWeatherIcon(String condition) {
        if (condition.equalsIgnoreCase("Clear")) {
            weatherIcon.setImageResource(R.drawable.ic_sun);
        } else if (condition.equalsIgnoreCase("Clouds")) {
            weatherIcon.setImageResource(R.drawable.ic_cloud);
        } else {
            weatherIcon.setImageResource(R.drawable.ic_rain);
        }
    }
}