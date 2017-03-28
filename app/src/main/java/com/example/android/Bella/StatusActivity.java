package com.example.android.Bella;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import java.io.IOException;

public class StatusActivity extends AppCompatActivity {

    Toolbar t;
    ToggleButton b1,b2,b3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_activity);

        Fade s = new Fade();
        s.setDuration(1000);
        getWindow().setEnterTransition(s);

        status();
        t = (Toolbar)findViewById(R.id.toolbar);
        b1 = (ToggleButton)findViewById(R.id.toggleButton2);
        b2 = (ToggleButton)findViewById(R.id.toggleButton3);
        b3 = (ToggleButton)findViewById(R.id.toggleButton4);

        setSupportActionBar(t);
        t.setNavigationIcon(R.drawable.ic_action_back);
        t.setTitleTextColor(0xFFFFFFFF);
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatusActivity.super.onBackPressed();
            }
        });
    }

    void status()
    {
        try {
            Bundle bundle = getIntent().getExtras();
            String status = bundle.getString("status");

            if (status.charAt(0) == 'T') {
                b1.setChecked(true);
            } else if (status.charAt(0) == 'F') {
                b1.setChecked(false);
            }
            if (status.charAt(1) == 'T') {
                b2.setChecked(true);
            } else if (status.charAt(1) == 'F') {
                b2.setChecked(false);
            }
            if (status.charAt(2) == 'T') {
                b3.setChecked(true);
            } else if (status.charAt(2) == 'F') {
                b3.setChecked(false);
            }
        } catch (NullPointerException ne) {
            Log.d("menu","triggered");
        }
    }
}
