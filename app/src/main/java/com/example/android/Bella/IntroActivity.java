package com.example.android.Bella;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AhoyOnboarderActivity {

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Bella", "Your Personal Assistant powered by Artificial Intelligence and Machine Learning.", R.mipmap.bella_launcher);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Home Automation", "Control your house with voice commands.", R.drawable.home);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Interactive", "Bella is interactive and can help you with daily tasks.", R.drawable.machine);
        AhoyOnboarderCard ahoyOnboarderCard4 = new AhoyOnboarderCard("Added Functionality", "Bella can play Music, fetch the news, predict the weather and much more!", R.drawable.tasks);

        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard4.setBackgroundColor(R.color.black_transparent);

        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);
        pages.add(ahoyOnboarderCard4);



        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.grey_200);
            page.setTitleTextSize(dpToPixels(10, this));
            page.setDescriptionTextSize(dpToPixels(6, this));
            //page.setIconLayoutParams(width, height, marginTop, marginLeft, marginRight, marginBottom);
        }

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        setFont(face);

        setFinishButtonTitle("Get Started");
        showNavigationControls(true);
        setGradientBackground();

        setInactiveIndicatorColor(R.color.grey);
        setActiveIndicatorColor(R.color.white);

        setOnboardPages(pages);
    }
    @Override
    public void onFinishButtonPressed() {
        launchHomeScreen();
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroActivity.this, MainActivity.class));
        finish();
        }

}


