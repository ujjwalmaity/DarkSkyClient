package dev.ujjwal.darkskyclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import events.ErrorEvent;
import events.WeatherEvent;
import models.Currently;
import services.WeatherServiceProvider;
import util.WeatherIconUtil;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_tempTextView)
    TextView tempTextView;

    @BindView(R.id.main_summeryTextView)
    TextView summeryTextView;

    @BindView(R.id.main_iconImageView)
    ImageView iconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        requestCurrentWeather(22.575504, 88.432871);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWeatherEvent(WeatherEvent weatherEvent) {
        Currently currently = weatherEvent.getWeather().getCurrently();
        Double fahrenheit = currently.getTemperature();
        Double celsius = (fahrenheit - 32) * 5 / 9;
        tempTextView.setText(String.valueOf(Math.round(celsius)));
        summeryTextView.setText(currently.getSummary());

        iconImageView.setImageResource(WeatherIconUtil.ICONS.get(currently.getIcon()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent errorEvent) {
        Toast.makeText(getApplicationContext(), errorEvent.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    private void requestCurrentWeather(double lat, double lng) {
        WeatherServiceProvider weatherServiceProvider = new WeatherServiceProvider();
        weatherServiceProvider.getWeather(lat, lng);
    }
}
