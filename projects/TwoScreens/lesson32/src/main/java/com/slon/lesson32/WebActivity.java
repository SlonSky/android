package com.slon.lesson32;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        WebView webView = (WebView) findViewById(R.id.webView);
        Uri data = getIntent().getData();
        webView.loadUrl(data.toString());
    }
}
