package com.panburikat.shuttlepass_driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TripHistoryAdapter extends ArrayAdapter<TripHistoryList> {
    private static final String TAG = "TripHistoryAdapter";
    private Context mContext;

    int mResource;

    public TripHistoryAdapter(Context context, int resource, ArrayList<TripHistoryList> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String origin = getItem(position).getOrigin();
        String destination = getItem(position).getDestination();
        String via = getItem(position).getVia();
        String timestamp = getItem(position).getTimestamp();
        String departureTime = getItem(position).getDepartureTime();

        TripHistoryList tripHistoryList = new TripHistoryList(origin, destination, via, timestamp, departureTime);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView place = (TextView) convertView.findViewById(R.id.place);
        TextView trip_date = (TextView) convertView.findViewById(R.id.trip_date);
        TextView dept_time = (TextView) convertView.findViewById(R.id.dept_time);

        place.setText(origin + " - " + destination + " Via " + via);
        trip_date.setText(timestamp);
        dept_time.setText(departureTime);

        return convertView;
    }
}
