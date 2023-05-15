package com.panburikat.shuttlepass_driver;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class Trip extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    String accID, tripCode = "", timeSelected = "";
    Button timepick;
    Calendar c;

    ActivityResultLauncher<Intent> actLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        TextView or = findViewById(R.id.origin);
                        TextView des = findViewById(R.id.destination);
                        TextView vi = findViewById(R.id.via);
                        or.setText(result.getData().getStringExtra("orig"));
                        des.setText(result.getData().getStringExtra("desti"));
                        vi.setText(result.getData().getStringExtra("via"));

                        tripCode = result.getData().getStringExtra("trip");
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Start Trip");
        getSupportActionBar().setHomeActionContentDescription("");

        accID = getIntent().getStringExtra("accID");
        EditText seats = findViewById(R.id.seats);

        timepick = findViewById(R.id.timepick);
        timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment tp = new TimePicker();
                tp.show(getSupportFragmentManager(), "Departure Time");
            }
        });

        Button ready = findViewById(R.id.ready);
        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String seat = seats.getText().toString().trim();
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getDefault());
                AlertDialog.Builder dialog = new AlertDialog.Builder(Trip.this);
                if (c == null || timeSelected.equals("") || c.before(cal) || seat.equals("") || tripCode.equals("")) {
                    if (tripCode.equals("")) {
                        dialog.setTitle("Destination");
                        dialog.setMessage("Please choose a destination!");
                    } else if (c == null || timeSelected.equals("") || c.before(cal)) {
                        dialog.setTitle("Time Selection");
                        dialog.setMessage("Please choose an appropriate time!");
                    } else {
                        dialog.setTitle("Available Seat");
                        dialog.setMessage("Please choose an appropriate number of seats available!");
                    }
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                } else {
                    dialog.setTitle("Ready to Accept Passengers?");
                    dialog.setMessage("You will be deducted P100.00 for the terminal fee, ready to accept passengers?");
                    dialog.setPositiveButton("Lets Go!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //Starting Write and Read data with URL
                                    //Creating array for parameters
                                    String[] field = new String[4];
                                    field[0] = "accnum";
                                    field[1] = "tripCode";
                                    field[2] = "deptime";
                                    field[3] = "cap";
                                    //Creating array for data
                                    String[] data = new String[4];
                                    data[0] = accID;
                                    data[1] = tripCode;
                                    data[2] = timeSelected;
                                    data[3] = seat;
                                    PutData putData = new PutData("https://jamora.leon.svdphs.ph/setActiveTrip.php", "POST", field, data);
                                    if (putData.startPut()) {
                                        if (putData.onComplete()) {
                                            String result = putData.getResult();
                                            if (!result.equals("Error: Database connection") && !result.equals("All fields are required")) {
                                                if (result.equals("Balance Insufficient")) {
                                                    Toast.makeText(getApplicationContext(), "You have Insufficient Balance, Please top up P100.00 to start accepting passengers", Toast.LENGTH_LONG).show();
                                                } else if (result.equals("Success")) {
                                                    Intent intent = new Intent(Trip.this, ActiveTrip.class);
                                                    intent.putExtra("accID", accID);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                    }
                                }
                            }); //End Write and Read data with URL
                        }
                    });
                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                }
            }
        });

        ImageButton ct = findViewById(R.id.changeTrip);
        ct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actLaunch.launch(new Intent(Trip.this, DestinationList.class));
            }
        });

    }

    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, i);
        c.set(Calendar.MINUTE, i1);
        c.setTimeZone(TimeZone.getDefault());
        SimpleDateFormat form = new SimpleDateFormat("h:mm a");
        timeSelected = form.format(c.getTime());
        timepick.setText(timeSelected);
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