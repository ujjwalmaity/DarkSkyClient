package dev.ujjwal.darkskyclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.Currently;
import models.Weather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.WeatherServiceProvider;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_tempTextView)
    TextView tempTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        requestCurrentWeather(22.575504, 88.432871);
    }

    private void requestCurrentWeather(double lat, double lng) {
        WeatherServiceProvider weatherServiceProvider = new WeatherServiceProvider();

        Callback callback = new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Currently currently = response.body().getCurrently();
                Log.i(TAG, "Temperature = " + currently.getTemperature());
                tempTextView.setText(String.valueOf(Math.round(currently.getTemperature())));
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.i(TAG, "onFailure: Unable to get weather data");
            }
        };

        weatherServiceProvider.getWeather(lat, lng, callback);
    }
}
