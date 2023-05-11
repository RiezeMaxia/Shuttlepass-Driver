package com.panburikat.shuttlepass_driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TripAdapter extends ArrayAdapter<TripList> {

    private Context mcontext;
    int mResource;

    public TripAdapter(@NonNull Context context, int resource, @NonNull List<TripList> objects) {
        super(context, resource, objects);
        mcontext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String ori = getItem(position).getOrigin();
        String des = getItem(position).getDestination();
        String via;
        if (getItem(position).getVia().equals("")) {
            via = "---";
        } else {
            via = getItem(position).getVia();
        }

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView frm = convertView.findViewById(R.id.from);
        TextView to = convertView.findViewById(R.id.to);
        TextView vi = convertView.findViewById(R.id.via);

        frm.setText(ori);
        to.setText(des);
        vi.setText(via);

        return convertView;
    }
}
