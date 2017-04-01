package com.example.android.Bella;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity{

    String link;
    WebView w;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        link = getIntent().getStringExtra("link");
        Log.e("link:",link);
        w = (WebView) findViewById(R.id.link);

        w.setWebViewClient(new WebViewClient());

        w.loadUrl(link);
    }
}
