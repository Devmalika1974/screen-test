package com.test.screenmirroring;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        WebView webView = findViewById(R.id.privacy_policy_webview);
        webView.setWebViewClient(new WebViewClient()); // Keep navigation inside the WebView
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript if needed by the page

        String url = getString(R.string.privacy_policy_url);
        webView.loadUrl(url);

        // Optional: Add back button support in WebView
        // if (getSupportActionBar() != null) {
        //     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //     getSupportActionBar().setDisplayShowHomeEnabled(true);
        // }
    }

    // Optional: Handle back button press to navigate WebView history
    // @Override
    // public boolean onSupportNavigateUp() {
    //     WebView webView = findViewById(R.id.privacy_policy_webview);
    //     if (webView.canGoBack()) {
    //         webView.goBack();
    //     } else {
    //         onBackPressed();
    //     }
    //     return true;
    // }

    // @Override
    // public void onBackPressed() {
    //     WebView webView = findViewById(R.id.privacy_policy_webview);
    //     if (webView.canGoBack()) {
    //         webView.goBack();
    //     } else {
    //         super.onBackPressed();
    //     }
    // }
}

