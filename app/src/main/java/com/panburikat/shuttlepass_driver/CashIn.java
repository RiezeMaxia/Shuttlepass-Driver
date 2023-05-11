package com.panburikat.shuttlepass_driver;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CashIn extends AppCompatActivity {
    private WebView webView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cash In");
        getSupportActionBar().setHomeActionContentDescription("");

        String id = getIntent().getStringExtra("accID");

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //ProgressDialog pd = ProgressDialog.show(CashIn.this, "Loading", "Please Wait", true);
        //pd.setCancelable(false);
        webView.setWebViewClient(new WebViewClient(){
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
        webView.loadUrl("https://jamora.leon.svdphs.ph/topup.php?accID=" + id);
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
}