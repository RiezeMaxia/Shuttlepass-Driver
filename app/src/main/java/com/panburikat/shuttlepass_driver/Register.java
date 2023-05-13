package com.panburikat.shuttlepass_driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.panburikat.shuttlepass_driver.Util.NetworkChangeListener;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class Register extends AppCompatActivity {

    NetworkChangeListener nc = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button regbtn = findViewById(R.id.regbtn);
        TextView log = findViewById(R.id.log);
        TextView err = findViewById(R.id.error);
        err.setTextColor(getResources().getColor(R.color.danger));
        TextInputLayout fnameInp = findViewById(R.id.fname);
        TextInputLayout lnameInp = findViewById(R.id.lname);
        TextInputLayout emailInp = findViewById(R.id.email);
        TextInputLayout passInp = findViewById(R.id.pass);
        TextInputLayout cpassInp = findViewById(R.id.cpass);
        ProgressBar prog = findViewById(R.id.prog);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                err.setText("");
                emailInp.setErrorEnabled(false);
                passInp.setErrorEnabled(false);
                cpassInp.setErrorEnabled(false);
                String fname = fnameInp.getEditText().getText().toString().trim();
                String lname = lnameInp.getEditText().getText().toString().trim();
                String email = emailInp.getEditText().getText().toString().trim().toLowerCase();
                String pass = passInp.getEditText().getText().toString().trim();
                String cpass = cpassInp.getEditText().getText().toString().trim();
                String idgen = UUID.randomUUID().toString();
                String accID = idgen.toString().replaceAll("-", "").substring(0, 15);

                if (!fname.equals("") && !lname.equals("") && !email.equals("") && !pass.equals("") && !cpass.equals("")) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        if (cpass.equals(pass)) {
                            err.setText("");
                            regbtn.setVisibility(View.GONE);
                            prog.setVisibility(View.VISIBLE);
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //Starting Write and Read data with URL
                                    //Creating array for parameters
                                    String[] field = new String[6];
                                    field[0] = "fname";
                                    field[1] = "lname";
                                    field[2] = "email";
                                    field[3] = "pass";
                                    field[4] = "accnum";
                                    field[5] = "type";
                                    //Creating array for data
                                    String[] data = new String[6];
                                    data[0] = StringUtils.capitalize(fname);
                                    data[1] = StringUtils.capitalize(lname);;
                                    data[2] = email;
                                    data[3] = pass;
                                    data[4] = accID;
                                    data[5] = "Driver";
                                    PutData putData = new PutData("https://jamora.leon.svdphs.ph/signup.php", "POST", field, data);
                                    if (putData.startPut()) {
                                        if (putData.onComplete()) {
                                            regbtn.setVisibility(View.VISIBLE);
                                            prog.setVisibility(View.GONE);
                                            String result = putData.getResult();
                                            if (result.equals("Sign Up Successful! Try Logging in")) {
//                                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Register.this, DriverDetails.class);
                                                intent.putExtra("accID", accID);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                err.setText(result);
                                            }
                                        }
                                    }
                                }
                            }); //End Write and Read data with URL
                        } else {
                            passInp.setError("Password Does not Match!");
                            cpassInp.setError("Password Does not Match!");
                        }
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