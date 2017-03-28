package com.example.android.Bella;


import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public class StatusActivity extends AppCompatActivity {

    Toolbar t;
    TextView t1,t2,t3;
    StickySwitch b1,b2,b3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_activity);

        Fade s = new Fade();
        s.setDuration(1000);
        getWindow().setEnterTransition(s);

        status();
        t = (Toolbar)findViewById(R.id.toolbar);
        t1 = (TextView)findViewById(R.id.textView10);
        t2 = (TextView)findViewById(R.id.textView11);
        t3 = (TextView)findViewById(R.id.textView12);
        b1 = (StickySwitch) findViewById(R.id.toggleButton2);
        b2 = (StickySwitch) findViewById(R.id.toggleButton3);
        b3 = (StickySwitch) findViewById(R.id.toggleButton4);

        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        t3.setVisibility(View.INVISIBLE);
        b1.setVisibility(View.INVISIBLE);
        b2.setVisibility(View.INVISIBLE);
        b3.setVisibility(View.INVISIBLE);

        setSupportActionBar(t);
        t.setNavigationIcon(R.drawable.ic_action_back);
        t.setTitleTextColor(0xFFFFFFFF);
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatusActivity.super.onBackPressed();
            }
        });

        t1.postDelayed(new Runnable() {
            @Override
            public void run() {
                revealEffect(t1);
                revealEffect(b1);
            }
        },300);
        t2.postDelayed(new Runnable() {
            @Override
            public void run() {
                revealEffect(t2);
                revealEffect(b2);
            }
        },600);
        t3.postDelayed(new Runnable() {
            @Override
            public void run() {
                revealEffect(t3);
                revealEffect(b3);
            }
        },900);
    }

    void revealEffect(View v) {
        if(Build.VERSION.SDK_INT > 20) {
            int cx = v.getMeasuredWidth()/2;
            int cy = v.getMeasuredHeight()/2;
            int finalRadius = Math.max(v.getWidth(),v.getHeight());
            Animator a = ViewAnimationUtils.createCircularReveal(v,cx,cy,0,finalRadius);
            a.setDuration(1000);
            v.setVisibility(View.VISIBLE);
            a.start();
        }
    }

    void status()
    {
        try {
            Bundle bundle = getIntent().getExtras();
            String status = bundle.getString("status");

            if (status.charAt(0) == 'T') {
                b1.setDirection(StickySwitch.Direction.RIGHT);
            } else if (status.charAt(0) == 'F') {
                b1.setDirection(StickySwitch.Direction.LEFT);
            }
            if (status.charAt(1) == 'T') {
                b2.setDirection(StickySwitch.Direction.RIGHT);
            } else if (status.charAt(1) == 'F') {
                b2.setDirection(StickySwitch.Direction.LEFT);
            }
            if (status.charAt(2) == 'T') {
                b3.setDirection(StickySwitch.Direction.RIGHT);
            } else if (status.charAt(2) == 'F') {
                b3.setDirection(StickySwitch.Direction.LEFT);
            }
        } catch (NullPointerException ne) {
            Log.d("menu","triggered");
        }
    }
}
