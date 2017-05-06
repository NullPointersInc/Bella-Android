package com.example.android.Bella;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.doctoror.particlesdrawable.ParticlesDrawable;

public class MachineLearning extends AppCompatActivity {

    private final ParticlesDrawable mDrawable = new ParticlesDrawable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_learning);
        findViewById(R.id.view).setBackground(mDrawable);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDrawable.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDrawable.stop();
    }

}
