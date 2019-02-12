package org.cmucreatelab.android.flutterprek.to_be_deleted;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class WebIndexActivity extends AppCompatActivity {

    private WebView webView;
    int counter = 0;


    @Override
    public void onBackPressed() {
        if (webView == null) {
            return;
        }
        if (counter == 0) {
            webView.loadUrl("file:///android_asset/web/alt_index.html");
        } else {
            webView.loadUrl("file:///android_asset/web/index.html");
        }
        counter = (counter+1) % 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // still slow
        webView = new WebView(getApplicationContext());
        webView.setWebViewClient(new CustomWebViewClient(WebIndexActivity.this));
        webView.getSettings().setJavaScriptEnabled(true);
        setContentView(webView);
        webView.loadUrl("file:///android_asset/web/index.html");

//        setContentView(R.layout.activity_web_index);

        // TODO need to detect when nav bar is forced to display, so it can hide again after a few seconds
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

//        // slow
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                webView = findViewById(R.id.webView);
//                webView.setWebViewClient(new CustomWebViewClient(WebIndexActivity.this));
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.loadUrl("file:///android_asset/web/index.html");
//            }
//        });

//        // slow
//        webView = (WebView) findViewById(R.id.webView);
//        webView.setWebViewClient(new CustomWebViewClient(this));
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("file:///android_asset/web/index.html");
    }
}
