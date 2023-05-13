package com.panburikat.shuttlepass_driver;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.panburikat.shuttlepass_driver.Util.NetworkChangeListener;
import com.panburikat.shuttlepass_driver.databinding.ActivityMainPageBinding;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.apache.commons.lang3.StringUtils;

public class MainPage extends AppCompatActivity {

    NetworkChangeListener nc = new NetworkChangeListener();
    ActivityMainPageBinding bind;
    String ID;
    int tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ID = getIntent().getStringExtra("accID");
        bind = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        bind.bottomNavView.setBackground(null);
        for (int i = 0; i < bind.bottomNavView.getMenu().size(); i++) {
            MenuItem menuItem = bind.bottomNavView.getMenu().getItem(i);
            View view = bind.bottomNavView.findViewById(menuItem.getItemId());
            TooltipCompat.setTooltipText(view, "");
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // your long click listener code here
                    return true;
                }
            });
        }
        replaceFragment(new Wallet());
        tag = 0;
        bind.bottomNavView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.wallet:
                    replaceFragment(new Wallet());
                    tag = 0;
                    break;
                case R.id.trip:
                    replaceFragment(new TripHistory());
                    tag = 1;
                    break;
                case R.id.transactions:
                    replaceFragment(new Transactions());
                    tag = 2;
                    break;
                case R.id.profile:
                    replaceFragment(new Profile());
                    tag = 3;
                    break;
            }
            return true;

        });

        bind.bottomNavView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
            }
        });

        FloatingActionButton qr = findViewById(R.id.active);

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Starting Write and Read data with URL
                        //Creating array for parameters
                        String[] field = new String[1];
                        field[0] = "accID";
                        //Creating array for data
                        String[] data = new String[1];
                        data[0] = ID;
                        PutData putData = new PutData("https://jamora.leon.svdphs.ph/checkVerification.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if (result.equals("no record")) {
                                    Intent intent = new Intent(MainPage.this, DriverDetails.class);
                                    intent.putExtra("accID", ID);
                                    startActivity(intent);
                                } else if (result.equals("not verified")) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainPage.this);
                                    dialog.setTitle("Still not Verified!");
                                    dialog.setMessage("Please wait for approval but at the meantime, you can use the wallet.");
                                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    }).show();
                                } else {
                                    Intent intent;
                                    if (!result.equals("activeTrip")) {
                                        intent = new Intent(MainPage.this, Trip.class);
                                    } else {
                                        intent = new Intent(MainPage.this, ActiveTrip.class);
                                    }
                                    intent.putExtra("accID", ID);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                }); //End Write and Read data with URL
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Log Out");
        dialog.setMessage("Are you sure you want to Log out?");
        dialog.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences sp = getSharedPreferences("saved_ACCID", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(MainPage.this, Login.class));
                finish();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
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

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bund = new Bundle();
        bund.putString("accID", ID);
        fragment.setArguments(bund);
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }
}