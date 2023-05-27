package com.panburikat.shuttlepass_driver;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Confirmation extends AppCompatActivity {

    String ID, amount, rec, chan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Confirm Transfer");
        getSupportActionBar().setHomeActionContentDescription("");
        ID = getIntent().getStringExtra("accID");
        amount = getIntent().getStringExtra("amount");
        rec = getIntent().getStringExtra("recipient");
        chan = getIntent().getStringExtra("channel");

        TextView am = findViewById(R.id.amount);
        TextView re = findViewById(R.id.rec);
        TextView cha = findViewById(R.id.channel);
        Button confirm = findViewById(R.id.confirm);
        Button cancel = findViewById(R.id.cancel);

        am.setText("₱ " + amount);
        re.setText(rec);
        cha.setText(chan);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        data[1] = amount;
                        data[2] = (chan.equals("Other Account")) ? rec : chan;
                        PutData putData = new PutData("https://jamora.leon.svdphs.ph/transfer.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if (!result.equals("Error: Database connection") && !result.equals("No accountID")) {
                                    if (result.equals("Success")) {
                                        Toast.makeText(getApplicationContext(), "Transfer Success!", Toast.LENGTH_SHORT).show();
                                    } else if (result.equals("Email not found!")) {
                                        Toast.makeText(getApplicationContext(), "Receiving account not found!", Toast.LENGTH_SHORT).show();
                                    } else if (result.equals("Balance Insufficient")) {
                                        Toast.makeText(getApplicationContext(), "You have Insufficient Balance to transfer this amount!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                finish();
                            }
                        }
                    }
                }); //End Write and Read data with URL
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}