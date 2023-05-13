package com.panburikat.shuttlepass_driver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    public static final String SHARED_PREFS = "saved_ACCID";
    public static final String ACC_ID = "-1";

    String accID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView img = findViewById(R.id.img);
        img.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                accID = sp.getString(ACC_ID, "NO_ACCID");
                if (accID.equals("NO_ACCID")) {
                    startActivity(new Intent(SplashScreen.this, Login.class));
                } else {
                    Intent intent = new Intent(SplashScreen.this, MainPage.class);
                    intent.putExtra("accID", accID);
                    startActivity(intent);
                }
                finish();
            }
        }, 2200);
    }
}

