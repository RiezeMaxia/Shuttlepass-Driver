package com.panburikat.shuttlepass_driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Transfer extends AppCompatActivity {

    String channel = "", ID;
    boolean isOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transfer");
        getSupportActionBar().setHomeActionContentDescription("");
        ID = getIntent().getStringExtra("accID");

        LinearLayout gcash = findViewById(R.id.gcash);
        LinearLayout maya = findViewById(R.id.maya);
        LinearLayout other = findViewById(R.id.other);
        TextInputLayout rec1 = findViewById(R.id.recipient1);
        TextInputLayout rec2 = findViewById(R.id.recipient2);
        TextInputLayout amount = findViewById(R.id.amount);
        Button transfer = findViewById(R.id.transfer);

        gcash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channel = "GCash";
                isOther = false;
                gcash.setBackground(getResources().getDrawable(R.drawable.layout_border));
                maya.setBackground(null);
                other.setBackground(null);
                amount.setEnabled(true);
                rec2.setVisibility(View.GONE);
                rec1.setVisibility(View.VISIBLE);
            }
        });

        maya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channel = "Maya";
                isOther = false;
                maya.setBackground(getResources().getDrawable(R.drawable.layout_border));
                gcash.setBackground(null);
                other.setBackground(null);
                amount.setEnabled(true);
                rec2.setVisibility(View.GONE);
                rec1.setVisibility(View.VISIBLE);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channel = "other";
                isOther = true;
                other.setBackground(getResources().getDrawable(R.drawable.layout_border));
                gcash.setBackground(null);
                maya.setBackground(null);
                amount.setEnabled(true);
                rec1.setVisibility(View.GONE);
                rec2.setVisibility(View.VISIBLE);
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String am = amount.getEditText().getText().toString().trim();
                String rec = (isOther) ? rec2.getEditText().getText().toString().trim() : channel;

                if (!am.equals("") && !rec.equals("")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[3];
                            field[0] = "accid";
                            field[1] = "amount";
                            field[2] = "recipient";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = ID;
                            data[1] = am;
                            data[2] = rec;
                            PutData putData = new PutData("https://jamora.leon.svdphs.ph/transfer.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (!result.equals("Error: Database connection") && !result.equals("No accountID")) {
                                        if (result.equals("Success")) {
                                            Toast.makeText(getApplicationContext(), "Transfer Success!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else if (result.equals("Email not found!")) {
                                            Toast.makeText(getApplicationContext(), "Receiving account not found!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (result.equals("Balance Insufficient")) {
                                        Toast.makeText(getApplicationContext(), "You have Insufficient Balance to transfer!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }); //End Write and Read data with URL

                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}