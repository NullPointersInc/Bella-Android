package com.example.android.Bella;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.View;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class GarbageActivity extends AppCompatActivity {

    Toolbar toolbar;
    CircularProgressBar circularProgressBar,circularProgressBar2,circularProgressBar3,circularProgressBar4,circularProgressBar5,circularProgressBar6;
    TextView t1,t2,t3,t4,t5,t6;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Fade s = new Fade();
        s.setDuration(1000);
        getWindow().setEnterTransition(s);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage);
        circularProgressBar = (CircularProgressBar)findViewById(R.id.circle_progress1);
        circularProgressBar2 = (CircularProgressBar)findViewById(R.id.circle_progress2);
        circularProgressBar3 = (CircularProgressBar)findViewById(R.id.circle_progress3);
        circularProgressBar4 = (CircularProgressBar)findViewById(R.id.circle_progress4);
        circularProgressBar5 = (CircularProgressBar)findViewById(R.id.circle_progress5);
        circularProgressBar6 = (CircularProgressBar)findViewById(R.id.circle_progress6);
        t1=(TextView)findViewById(R.id.textView24);
        t2=(TextView)findViewById(R.id.textView25);
        t3=(TextView)findViewById(R.id.textView26);
        t4=(TextView)findViewById(R.id.textView27);
        t5=(TextView)findViewById(R.id.textView28);
        t6=(TextView)findViewById(R.id.textView29);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GarbageActivity.super.onBackPressed();
            }
        });

        int animationDuration = 2500; // 2500ms = 2,5s
        circularProgressBar.setProgressWithAnimation(65, animationDuration);
        t1.setText(": "+"65%");
        circularProgressBar2.setProgressWithAnimation(30, animationDuration);
        t2.setText(": "+"65%");
        circularProgressBar3.setProgressWithAnimation(85, animationDuration);
        t3.setText(": "+"65%");
        circularProgressBar4.setProgressWithAnimation(65, animationDuration);
        t4.setText(": "+"65%");
        circularProgressBar5.setProgressWithAnimation(52, animationDuration);
        t5.setText(": "+"65%");
        circularProgressBar6.setProgressWithAnimation(65, animationDuration);
        t6.setText(": "+"65%");


    }
}
