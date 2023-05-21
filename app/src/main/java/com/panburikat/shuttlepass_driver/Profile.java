package com.panburikat.shuttlepass_driver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Profile extends Fragment {

    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        id = this.getArguments().getString("accID");
        TextView full_name = view.findViewById(R.id.full_name);
        TextView email = view.findViewById(R.id.email);
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
                        if (result.equals("Error: Database connection") || result.equals("No accountID")) {
                            String[] array = result.split(";");
                            full_name.setText(array[0]);
                            email.setText(array[1]);
                        }
                    }
                }
            }
        }); //End Write and Read data with URL
        return view;
    }
}