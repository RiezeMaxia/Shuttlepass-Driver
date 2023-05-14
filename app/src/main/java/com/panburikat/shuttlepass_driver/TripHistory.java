package com.panburikat.shuttlepass_driver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;

public class TripHistory extends Fragment {
    String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_history, container, false);
        id = this.getArguments().getString("accID");

        ListView listView = view.findViewById(R.id.listView);


//        TripHistoryList one = new TripHistoryList("a", "a", "a", "a", "a");
//        TripHistoryList two = new TripHistoryList("b", "b", "b", "b", "b");
//        TripHistoryList three = new TripHistoryList("c", "c", "c", "c", "c");

        ArrayList<TripHistoryList> tripHistoryList = new ArrayList<>();
        Gson gson = new Gson();
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
                PutData putData = new PutData("https://jamora.leon.svdphs.ph/getTripHistory.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Error: Database connection") && !result.equals("No accountID")) {
                            if (!result.equals("No Results")) {
                                TripHistoryList[] tl = gson.fromJson(result, TripHistoryList[].class);
                                for (int x = 0; x < tl.length; x++) {
                                    TripHistoryList one = new TripHistoryList(tl[x].getTripID(), tl[x].getOrigin(), tl[x].getDestination(), tl[x].getVia(), tl[x].getTimestamp(), tl[x].getDepartureTime());
                                    tripHistoryList.add(one);
                                }
                            }
                        }

                    }
                }
            }
        });

//        tripHistoryList.add(one);
//        tripHistoryList.add(two);
//        tripHistoryList.add(three);

        TripHistoryAdapter adapter = new TripHistoryAdapter(getActivity().getApplicationContext(), R.layout.trip_history_layout, tripHistoryList);
        listView.setAdapter(adapter);

        return view;
    }
}