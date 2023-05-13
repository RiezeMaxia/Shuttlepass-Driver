package com.panburikat.shuttlepass_driver;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.panburikat.shuttlepass_driver.Util.NetworkChangeListener;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.apache.commons.lang3.StringUtils;

public class DriverDetails extends AppCompatActivity {

    NetworkChangeListener nc = new NetworkChangeListener();
    String accID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);
        getSupportActionBar().setTitle("Driver Details");

        accID = getIntent().getStringExtra("accID");

        Button submit = findViewById(R.id.d_submit);
        TextInputLayout lic = findViewById(R.id.licence);
        lic.getEditText().setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        TextInputLayout bcode = findViewById(R.id.bcode);
        bcode.getEditText().setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lictxt = lic.getEditText().getText().toString().trim().toUpperCase();
                String bcodetxt = bcode.getEditText().getText().toString().trim().toUpperCase();

                if (!lictxt.equals("") && !bcodetxt.equals("")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[3];
                            field[0] = "license";
                            field[1] = "buscode";
                            field[2] = "accnum";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = lictxt;
                            data[1] = bcodetxt;
                            data[2] = accID;
                            PutData putData = new PutData("https://jamora.leon.svdphs.ph/driverdet.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("Please wait for approval, Thank you!")) {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(DriverDetails.this);
                                        dialog.setTitle("Success!");
                                        dialog.setMessage("Please wait for approval but at the meantime, you can log in to your account.");
                                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        }).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }); //End Write and Read data with URL
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