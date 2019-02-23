package com.nsb.visions.varun.mynsb;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;



// TODO: this activity is DEAD and has to be updated post myNSB Beta
public class Webview extends AppCompatActivity {

    @SuppressLint("All")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        // Get the intent data from a pending intent
        Bundle extras = this.getIntent().getExtras();
        String url = (String) extras.get("url");

        // Load the article
        webView.loadUrl(url);
    }
}
