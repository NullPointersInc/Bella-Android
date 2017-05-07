package com.example.android.Bella;

import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;

public class MaterialWeatherActivity extends AppCompatActivity {
    TextView ts;
    String value;
    float val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_weather);

        View someView = findViewById(R.id.weatherView);
        View root = someView.getRootView();

        value = getIntent().getStringExtra("value");
        val = Float.parseFloat(value);
        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= 21) {

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        WeatherView mWeatherView = (WeatherView) findViewById(R.id.weather);
        ts = (TextView)findViewById(R.id.weatherText);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Roboto-Light.ttf");

        ts.setTypeface(custom_font);

        if(val>=32) {
            mWeatherView.setWeather(Constants.weatherStatus.SUN)
                    .setCurrentLifeTime(2000)
                    .setCurrentFadeOutTime(1000)
                    .setCurrentParticles(43)
                    .setFPS(60)
                    .setCurrentAngle(-5)
                    .setOrientationMode(Constants.orientationStatus.ENABLE)
                    .startAnimation();

            ts.setText("It's Sunny");
            root.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_sunny));
            window.setStatusBarColor(getResources().getColor(R.color.orange_900));
        } else if ((val<32) && (val>=20)) {
            mWeatherView.setWeather(Constants.weatherStatus.RAIN)
                    .setCurrentLifeTime(2000)
                    .setCurrentFadeOutTime(1000)
                    .setCurrentParticles(43)
                    .setFPS(60)
                    .setCurrentAngle(-5)
                    .setOrientationMode(Constants.orientationStatus.ENABLE)
                    .startAnimation();

            ts.setText("It's Rainy");
            root.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background));
            window.setStatusBarColor(getResources().getColor(R.color.blue_900));
        } else {
            mWeatherView.setWeather(Constants.weatherStatus.RAIN)
                    .setCurrentLifeTime(2000)
                    .setCurrentFadeOutTime(1000)
                    .setCurrentParticles(43)
                    .setFPS(60)
                    .setCurrentAngle(-5)
                    .setOrientationMode(Constants.orientationStatus.ENABLE)
                    .startAnimation();

            ts.setText("It might Rain");
            root.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background));
            window.setStatusBarColor(getResources().getColor(R.color.blue_900));
        }
    }
}
