package com.example.android.Bella;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ashish Nayak on 07-Mar-17.
 */

public class NewsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.newslayout);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.newscontainer, new NewsFragment())
                    .commit();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }
}
