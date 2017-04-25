package com.example.android.Bella;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.thefinestartist.finestwebview.FinestWebView;

public class AboutActivity extends AppCompatActivity {

    ImageView levi;
    ImageView astrix;
    ImageView den;
    ImageView raiden;
    Toolbar toolbar;
    WebView w;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Fade s = new Fade();
        s.setDuration(1000);
        getWindow().setEnterTransition(s);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        levi = (ImageView) findViewById(R.id.imageView3);
        astrix = (ImageView) findViewById(R.id.imageView5);
        den = (ImageView) findViewById(R.id.imageView7);
        raiden = (ImageView) findViewById(R.id.imageView4);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //w = (WebView) findViewById(R.id.link);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutActivity.super.onBackPressed();
            }
        });
        
        levi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FinestWebView.Builder(AboutActivity.this).titleDefault("Hardware Dev")
                        .toolbarScrollFlags(0)
                        .titleColorRes(R.color.finestWhite)
                        .statusBarColorRes(R.color.colorPrimaryDark)
                        .toolbarColorRes(R.color.colorPrimary)
                        .iconDefaultColorRes(R.color.finestWhite)
                        .progressBarColorRes(R.color.finestWhite)
                        .menuSelector(R.drawable.selector_light_theme)
                        .dividerHeight(0)
                        .webViewJavaScriptEnabled(true)
                        .showSwipeRefreshLayout(true)
                        .gradientDivider(false)
                        //                    .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                        .setCustomAnimations(R.anim.fade_in_fast, R.anim.fade_out_medium, R.anim.fade_in_medium, R.anim.fade_out_fast)
                        .disableIconBack(false)
                        .disableIconClose(false)
                        .disableIconForward(false)
                        .disableIconMenu(false)
                        .show("https://github.com/somanath08");
            }
        });
        astrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FinestWebView.Builder(AboutActivity.this).titleDefault("Hardware Dev")
                        .toolbarScrollFlags(0)
                        .titleColorRes(R.color.finestWhite)
                        .statusBarColorRes(R.color.colorPrimaryDark)
                        .toolbarColorRes(R.color.colorPrimary)
                        .iconDefaultColorRes(R.color.finestWhite)
                        .progressBarColorRes(R.color.finestWhite)
                        .menuSelector(R.drawable.selector_light_theme)
                        .dividerHeight(0)
                        .webViewJavaScriptEnabled(true)
                        .showSwipeRefreshLayout(true)
                        .gradientDivider(false)
                        //                    .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                        .setCustomAnimations(R.anim.fade_in_fast, R.anim.fade_out_medium, R.anim.fade_in_medium, R.anim.fade_out_fast)
                        .disableIconBack(false)
                        .disableIconClose(false)
                        .disableIconForward(false)
                        .disableIconMenu(false)
                        .show("https://github.com/aj-ames");
            }
        });
        den.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FinestWebView.Builder(AboutActivity.this).titleDefault("AI Dev")
                        .toolbarScrollFlags(0)
                        .titleColorRes(R.color.finestWhite)
                        .statusBarColorRes(R.color.colorPrimaryDark)
                        .toolbarColorRes(R.color.colorPrimary)
                        .iconDefaultColorRes(R.color.finestWhite)
                        .progressBarColorRes(R.color.finestWhite)
                        .menuSelector(R.drawable.selector_light_theme)
                        .dividerHeight(0)
                        .webViewJavaScriptEnabled(true)
                        .showSwipeRefreshLayout(true)
                        .gradientDivider(false)
                        //                    .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                        .setCustomAnimations(R.anim.fade_in_fast, R.anim.fade_out_medium, R.anim.fade_in_medium, R.anim.fade_out_fast)
                        .disableIconBack(false)
                        .disableIconClose(false)
                        .disableIconForward(false)
                        .disableIconMenu(false)
                        .show("https://github.com/ashishraman96");
            }
        });
        raiden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FinestWebView.Builder(AboutActivity.this).titleDefault("AI Dev")
                        .toolbarScrollFlags(0)
                        .titleColorRes(R.color.finestWhite)
                        .statusBarColorRes(R.color.colorPrimaryDark)
                        .toolbarColorRes(R.color.colorPrimary)
                        .iconDefaultColorRes(R.color.finestWhite)
                        .progressBarColorRes(R.color.finestWhite)
                        .menuSelector(R.drawable.selector_light_theme)
                        .dividerHeight(0)
                        .webViewBuiltInZoomControls(true)
                        .webViewDisplayZoomControls(true)
                        .webViewJavaScriptEnabled(true)
                        .showSwipeRefreshLayout(true)
                        .gradientDivider(false)
                        //                    .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                        .setCustomAnimations(R.anim.fade_in_medium, R.anim.fade_out_medium, R.anim.fade_in_medium, R.anim.fade_out_medium)
                        .disableIconBack(false)
                        .disableIconClose(false)
                        .disableIconForward(false)
                        .disableIconMenu(false)
                        .show("https://ujwalp15.github.io");
            }
        });
    }
}
