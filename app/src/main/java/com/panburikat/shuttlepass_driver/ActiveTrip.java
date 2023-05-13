package com.panburikat.shuttlepass_driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.panburikat.shuttlepass_driver.Util.NetworkChangeListener;

public class ActiveTrip extends AppCompatActivity {

    NetworkChangeListener nc = new NetworkChangeListener();

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_trip);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Active Trip");
        getSupportActionBar().setHomeActionContentDescription("");

        String id = getIntent().getStringExtra("accID");

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //ProgressDialog pd = ProgressDialog.show(CashIn.this, "Loading", "Please Wait", true);
        //pd.setCancelable(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //pd.show();
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //pd.dismiss();
                //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                if (url.equals("https://jamora.leon.svdphs.ph/complete.php")) {
                    finish();
                }
            }
        });
        webView.loadUrl("https://jamora.leon.svdphs.ph/trip.php?accID=" + id);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(nc, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(nc);
        super.onStop();
    }
}