package com.panburikat.shuttlepass_driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;

public class DestinationList extends AppCompatActivity {

    ArrayList<TripList> tripAdapter = new ArrayList<>();
    ArrayList<TripList> filtered = new ArrayList<TripList>();
    TripAdapter ta;
    Boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_list);

        ListView destinationList = findViewById(R.id.destinationList);

        Gson gson = new Gson();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[1];
                field[0] = "test";
                //Creating array for data
                String[] data = new String[1];
                data[0] = "test";
                PutData putData = new PutData("https://jamora.leon.svdphs.ph/getTrip.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Error: Database connection") && !result.equals("No accountID")) {
                            if (!result.equals("No Results")) {
                                TripList[] tl = gson.fromJson(result, TripList[].class);
                                for (int x = 0; x < tl.length; x++) {
                                    tripAdapter.add(tl[x]);
                                }

                                ta = new TripAdapter(DestinationList.this, R.layout.destinations, tripAdapter);
                                destinationList.setAdapter(ta);

                            }
                        }

                    }
                }
            }
        }); //End Write and Read data with URL

        SearchView sv = findViewById(R.id.sb);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                isChanged = true;
                ArrayList<TripList> filter = new ArrayList<TripList>();

                for (TripList trip: tripAdapter) {
                    if (trip.getOrigin().toLowerCase().contains(newText.toLowerCase()) || trip.getDestination().toLowerCase().contains(newText.toLowerCase()) || trip.getVia().toLowerCase().contains(newText.toLowerCase())) {
                        filter.add(trip);
                    }
                }

                if (newText.isEmpty()) {
                    isChanged = false;
                } else {
                    filtered = filter;
                }

                TripAdapter ta = new TripAdapter(DestinationList.this, R.layout.destinations, filtered);
                destinationList.setAdapter(ta);
                return false;
            }
        });

        destinationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TripList tl;
                if (isChanged == false) {
                    tl = tripAdapter.get(i);
                } else {
                    tl = filtered.get(i);
                }
                String via;
                if (tl.getVia().equals("")) {
                    via = "---";
                } else {
                    via = tl.getVia();
                }

                Intent intent = new Intent();
                intent.putExtra("trip", tl.getTripCode());
                intent.putExtra("orig", tl.getOrigin());
                intent.putExtra("desti", tl.getDestination());
                intent.putExtra("via", via);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }
}