package com.panburikat.shuttlepass_driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputLayout emailInp = findViewById(R.id.email);
        TextInputLayout passInp = findViewById(R.id.pass);
        TextView reg = findViewById(R.id.reg);
        TextView err = findViewById(R.id.error);
        err.setTextColor(getResources().getColor(R.color.danger));
        Button logbtn = findViewById(R.id.log);
        ProgressBar prog = findViewById(R.id.prog);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
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
                                            Intent intent = new Intent(Login.this, MainPage.class);
                                            intent.putExtra("accID", result[1]);
                                            startActivity(intent);
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
}