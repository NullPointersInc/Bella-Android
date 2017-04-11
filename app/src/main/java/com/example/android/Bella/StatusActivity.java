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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public class StatusActivity extends AppCompatActivity {

    Toolbar t;
    TextView t1,t2,t3,t4,t5,t6;
    StickySwitch b1,b2,b3,b4,b5,b6;
    String status;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_activity);

        status = getIntent().getStringExtra("status");

        Fade s = new Fade();
        s.setDuration(1000);
        getWindow().setEnterTransition(s);

        t = (Toolbar)findViewById(R.id.toolbar);
        t1 = (TextView)findViewById(R.id.textView10);
        t2 = (TextView)findViewById(R.id.textView11);
        t3 = (TextView)findViewById(R.id.textView12);
        t4 = (TextView)findViewById(R.id.textView13);
        t5 = (TextView)findViewById(R.id.textView14);
        t6 = (TextView)findViewById(R.id.textView15);
        b1 = (StickySwitch) findViewById(R.id.toggleButton2);
        b2 = (StickySwitch) findViewById(R.id.toggleButton3);
        b3 = (StickySwitch) findViewById(R.id.toggleButton4);
        b4 = (StickySwitch) findViewById(R.id.toggleButton5);
        b5 = (StickySwitch) findViewById(R.id.toggleButton6);
        b6 = (StickySwitch) findViewById(R.id.toggleButton7);

        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        t3.setVisibility(View.INVISIBLE);
        t4.setVisibility(View.INVISIBLE);
        t5.setVisibility(View.INVISIBLE);
        t6.setVisibility(View.INVISIBLE);
        b1.setVisibility(View.INVISIBLE);
        b2.setVisibility(View.INVISIBLE);
        b3.setVisibility(View.INVISIBLE);
        b4.setVisibility(View.INVISIBLE);
        b5.setVisibility(View.INVISIBLE);
        b6.setVisibility(View.INVISIBLE);
       b1.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               return true;
           }
       });
        b2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        b3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        b4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        b5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        b6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

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
        t4.postDelayed(new Runnable() {
            @Override
            public void run() {
                revealEffect(t4);
                revealEffect(b4);
            }
        },900);
        t5.postDelayed(new Runnable() {
            @Override
            public void run() {
                revealEffect(t5);
                revealEffect(b5);
            }
        },900);
        t6.postDelayed(new Runnable() {
            @Override
            public void run() {
                revealEffect(t6);
                revealEffect(b6);
            }
        },900);


        if (status.charAt(0) == 'T') {
            b1.setSwitchColor(getResources().getColor(R.color.green));
            b1.setDirection(StickySwitch.Direction.RIGHT);
        } else if (status.charAt(0) == 'F') {
            b1.setSwitchColor(getResources().getColor(R.color.red));
            b1.setDirection(StickySwitch.Direction.LEFT);
        }
        if (status.charAt(1) == 'T') {
            b2.setSwitchColor(getResources().getColor(R.color.green));
            b2.setDirection(StickySwitch.Direction.RIGHT);
        } else if (status.charAt(1) == 'F') {
            b2.setSwitchColor(getResources().getColor(R.color.red));
            b2.setDirection(StickySwitch.Direction.LEFT);
        }
        if (status.charAt(2) == 'T') {
            b3.setSwitchColor(getResources().getColor(R.color.green));
            b3.setDirection(StickySwitch.Direction.RIGHT);
        } else if (status.charAt(2) == 'F') {
            b3.setSwitchColor(getResources().getColor(R.color.red));
            b3.setDirection(StickySwitch.Direction.LEFT);
        }
        if(status.charAt(3) == 'T') {
            b4.setSwitchColor(getResources().getColor(R.color.green));
            b4.setDirection(StickySwitch.Direction.RIGHT);
        } else if (status.charAt(3) == 'F') {
            b4.setSwitchColor(getResources().getColor(R.color.red));
            b4.setDirection(StickySwitch.Direction.LEFT);
        }
        if(status.charAt(4) == 'T') {
            b5.setSwitchColor(getResources().getColor(R.color.green));
            b5.setDirection(StickySwitch.Direction.RIGHT);
        } else if (status.charAt(4) == 'F') {
            b5.setSwitchColor(getResources().getColor(R.color.red));
            b5.setDirection(StickySwitch.Direction.LEFT);
        }
        if(status.charAt(5) == 'T') {
            b6.setSwitchColor(getResources().getColor(R.color.green));
            b6.setDirection(StickySwitch.Direction.RIGHT);
        } else if (status.charAt(5) == 'F') {
            b6.setSwitchColor(getResources().getColor(R.color.red));
            b6.setDirection(StickySwitch.Direction.LEFT);
        }
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
}
