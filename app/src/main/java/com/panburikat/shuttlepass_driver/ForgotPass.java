package com.panburikat.shuttlepass_driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.panburikat.shuttlepass_driver.Util.NetworkChangeListener;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class ForgotPass extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        TextInputLayout emailInp = findViewById(R.id.email);
        TextView err = findViewById(R.id.error);
        Button submitBtn = findViewById(R.id.submit);
        ProgressBar prog = findViewById(R.id.prog);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setHomeActionContentDescription("");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                err.setText("");
                emailInp.setErrorEnabled(false);
                String email = emailInp.getEditText().getText().toString().trim().toLowerCase();

                if (!email.equals("")) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        err.setText("");
                        submitBtn.setVisibility(View.GONE);
                        prog.setVisibility(View.VISIBLE);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[1];
                                field[0] = "email";
                                //Creating array for data
                                String[] data = new String[2];
                                data[0] = email;
                                PutData putData = new PutData("https://jamora.leon.svdphs.ph/forgotPass.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        submitBtn.setVisibility(View.VISIBLE);
                                        prog.setVisibility(View.GONE);
                                        String res = putData.getResult();
                                        String[] result = res.split(";");
                                        if (result[0].equals("Check Your Email to change your password")) {
                                            finish();
                                        } else {
                                            err.setText(res);
                                        }
                                    }
                                }
                                //End Write and Read data with URL
                            }
                        });
                    } else {
                        emailInp.setError("Please Enter a Valid Email!");
                    }
                } else {
                    err.setText("Fill out all Required Fields!");
                }
            }
        });
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

}
