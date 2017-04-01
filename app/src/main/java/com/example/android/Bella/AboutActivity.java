package com.example.android.Bella;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

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
        w = (WebView) findViewById(R.id.link);

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
                Intent intent = new Intent(AboutActivity.this,WebActivity.class);
                intent.putExtra("link", "https://github.com/somanath08");
                startActivity(intent);
            }
        });
        astrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this,WebActivity.class);
                intent.putExtra("link", "https://github.com/aj-ames");
                startActivity(intent);
            }
        });
        den.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this,WebActivity.class);
                intent.putExtra("link", "https://github.com/ashishraman96");
                startActivity(intent);
            }
        });
        raiden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this,WebActivity.class);
                intent.putExtra("link", "https://ujwalp15.github.io");
                startActivity(intent);
            }
        });
    }
}
