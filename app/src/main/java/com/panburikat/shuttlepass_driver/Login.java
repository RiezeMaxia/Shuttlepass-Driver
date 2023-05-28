package com.panburikat.shuttlepass_driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.BroadcastReceiver;
import com.google.android.material.textfield.TextInputLayout;
import com.panburikat.shuttlepass_driver.Util.NetworkChangeListener;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login extends AppCompatActivity {

    NetworkChangeListener nc = new NetworkChangeListener();
    public static final String SHARED_PREFS = "saved_ACCID";
    public static final String ACC_ID = "-1";
    public static final String ACTION_FINISH_ALL_ACTIVITIES = "com.panburikat.shuttlepass_driver.ACTION_FINISH_ALL_ACTIVITIES";
    static boolean isChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputLayout emailInp = findViewById(R.id.email);
        TextInputLayout passInp = findViewById(R.id.pass);
        TextView reg = findViewById(R.id.reg);
        TextView err = findViewById(R.id.error);
        TextView forgor = findViewById(R.id.forgor);
        err.setTextColor(getResources().getColor(R.color.danger));
        Button logbtn = findViewById(R.id.log);
        ProgressBar prog = findViewById(R.id.prog);
        CheckBox remember = findViewById(R.id.remember);


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = remember.isChecked();
            }
        });

        forgor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPass.class);
                startActivity(intent);
            }
        });

        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                err.setText("");
                emailInp.setErrorEnabled(false);
                String email = emailInp.getEditText().getText().toString().trim().toLowerCase();
                String pass = passInp.getEditText().getText().toString().trim();

                if (!email.equals("") && !pass.equals("")) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        err.setText("");
                        logbtn.setVisibility(View.GONE);
                        prog.setVisibility(View.VISIBLE);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[2];
                                field[0] = "email";
                                field[1] = "pass";
                                //Creating array for data
                                String[] data = new String[2];
                                data[0] = email;
                                data[1] = pass;
                                PutData putData = new PutData("https://jamora.leon.svdphs.ph/loginDriver.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        logbtn.setVisibility(View.VISIBLE);
                                        prog.setVisibility(View.GONE);
                                        String res = putData.getResult();
                                        String[] result = res.split(";");
                                        if (result[0].equals("Login Success")) {
                                            if (isChecked == true) {
                                                SharedPreferences sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sp.edit();
                                                editor.putString(ACC_ID, result[1]);
                                                editor.apply();
                                            }
                                            Intent intent = new Intent(Login.this, MainPage.class);
                                            intent.putExtra("accID", result[1]);
                                            startActivity(intent);
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