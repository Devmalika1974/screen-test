package com.test.screenmirroring;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WhatsWebActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_web);

        webView = findViewById(R.id.whats_web_webview);
        webView.setWebViewClient(new WebViewClient()); // Keep navigation inside the WebView

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true); // Needed for WhatsApp Web
        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"); // Desktop user agent

        webView.loadUrl("https://web.whatsapp.com/");

        // Optional: Add back button support
        // if (getSupportActionBar() != null) {
        //     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //     getSupportActionBar().setDisplayShowHomeEnabled(true);
        // }
    }

    // Optional: Handle back button press to navigate WebView history
    // @Override
    // public boolean onSupportNavigateUp() {
    //     if (webView.canGoBack()) {
    //         webView.goBack();
    //     } else {
    //         onBackPressed();
    //     }
    //     return true;
    // }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

