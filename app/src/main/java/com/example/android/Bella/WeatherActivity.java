package com.example.android.Bella;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;
import android.view.Window;

import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;

public class WeatherActivity extends AppCompatActivity {
    public static String S;
    WeatherView mWeatherView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fade s = new Fade();
        s.setDuration(1000);
        getWindow().setEnterTransition(s);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.framelayout);
        Window window = getWindow();
        mWeatherView = (WeatherView) findViewById(R.id.weather);
        S = getIntent().getStringExtra("city");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        mWeatherView.setWeather(Constants.weatherStatus.RAIN)
                .setCurrentLifeTime(2000)
                .setCurrentFadeOutTime(1000)
                .setCurrentParticles(43)
                .setFPS(60)
                .setCurrentAngle(-5)
                .setOrientationMode(Constants.orientationStatus.ENABLE)
                .startAnimation();

        window.setStatusBarColor(getResources().getColor(R.color.blue_900));

    }

}
