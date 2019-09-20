package services;

import android.util.Log;

import models.Currently;
import models.Weather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherServiceProvider {

    private static final String TAG = WeatherServiceProvider.class.getSimpleName();
    private static final String BASE_URL = "https://api.darksky.net/forecast/bddb1d2e9718a35adba787145f467ec3/37.8267,-122.4233/";
    private Retrofit retrofit;

    private Retrofit getRetrofit() {
        if (this.retrofit == null) {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return this.retrofit;
    }

    public void getWeather() {
        WeatherService weatherService = getRetrofit().create(WeatherService.class);
        Call<Weather> weatherData = weatherService.getWeather();
        weatherData.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Currently currently = response.body().getCurrently();
                Log.i(TAG, "Temperature = " + currently.getTemperature());
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.i(TAG, "onFailure: Unable to get weather data");
            }
        });
    }
}