package com.panburikat.shuttlepass_driver;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Profile extends Fragment {

    private String id;
    private String acc_email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        id = this.getArguments().getString("accID");
        TextView full_name = view.findViewById(R.id.full_name);
        TextView email = view.findViewById(R.id.email);
        Button log_out = view.findViewById(R.id.log_out);
        Button account_settings = view.findViewById(R.id.account_settings);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[1];
                field[0] = "id";
                //Creating array for data
                String[] data = new String[1];
                data[0] = id;
                PutData putData = new PutData("https://jamora.leon.svdphs.ph/getProfile.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Error: Database connection") || !result.equals("No accountID")) {
                            String[] array = result.split(";");
                            full_name.setText(array[0]);
                            email.setText(array[1]);
                            acc_email = array[1];
                        }
                    }
                }
            }
        }); //End Write and Read data with URL

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Log Out");
                dialog.setMessage("Are you sure you want to Log out?");
                dialog.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sp = getActivity().getSharedPreferences("saved_ACCID", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(getActivity(), Login.class));
                        getActivity().finish();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
            }
        });

        account_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                intent.putExtra("accID", id);
                intent.putExtra("email", acc_email);
                startActivity(intent);
            }
        });
        return view;
    }
}