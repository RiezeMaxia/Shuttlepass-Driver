package com.panburikat.shuttlepass_driver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class TripHistory extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_history, container, false);
        //here

        ListView listView = view.findViewById(R.id.listView);

        TripHistoryList one = new TripHistoryList("a", "a", "a", "a", "a");
        TripHistoryList two = new TripHistoryList("b", "b", "b", "b", "b");
        TripHistoryList three = new TripHistoryList("c", "c", "c", "c", "c");

        ArrayList<TripHistoryList> tripHistoryList = new ArrayList<>();
        tripHistoryList.add(one);
        tripHistoryList.add(two);
        tripHistoryList.add(three);

        TripHistoryAdapter adapter = new TripHistoryAdapter(getActivity().getApplicationContext(), R.layout.trip_history_layout, tripHistoryList);
        listView.setAdapter(adapter);

        return view;
    }
}