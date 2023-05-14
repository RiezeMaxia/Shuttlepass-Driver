package com.panburikat.shuttlepass_driver;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Wallet extends Fragment {

    private TextView bal, empty;
    private LinearLayout layout;
    private LinearLayout layoutTH;
    private String id;

    private void getWallet() {
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
                PutData putData = new PutData("https://jamora.leon.svdphs.ph/getBalance.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (result.equals("Error: Database connection") || result.equals("No accountID")) {
                            bal.setText("Error: Please Restart the App");
                        } else {
                            bal.setText("₱ " + result);
                        }
                    }
                }
            }
        }); //End Write and Read data with URL
    }

    private void getTransactions() {
        Gson gson = new Gson();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[2];
                field[0] = "id";
                field[1] = "limit";
                //Creating array for data
                String[] data = new String[2];
                data[0] = id;
                data[1] = "5";
                PutData putData = new PutData("https://jamora.leon.svdphs.ph/getTransaction.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Error: Database connection") && !result.equals("No accountID")) {
                            if (!result.equals("No Results")) {
                                layout.removeAllViews();
                                TransactionList[] tl = gson.fromJson(result, TransactionList[].class);
                                for (int x = 0; x < tl.length; x++) {
                                    addItem(tl[x].getTransactionID(), tl[x].getTransactionType(), tl[x].getDate(), tl[x].getAmount());
                                }
                            }
                        }

                    }
                }
            }
        }); //End Write and Read data with URL
    }

    private void getTripHistory() {
        Gson gson = new Gson();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[2];
                field[0] = "id";
                field[1] = "limit";
                //Creating array for data
                String[] data = new String[2];
                data[0] = id;
                data[1] = "1";
                PutData putData = new PutData("https://jamora.leon.svdphs.ph/getTripHistory.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Error: Database connection") && !result.equals("No accountID")) {
                            if (!result.equals("No Results")) {
                                layoutTH.removeAllViews();
                                TripHistoryList tl = gson.fromJson(result, TripHistoryList.class);
                                    addItemTH(tl.getTripID(), tl.getOrigin(), tl.getDestination(), tl.getVia(), tl.getTimestamp(), tl.getDepartureTime());
                            }
                        }

                    }
                }
            }
        }); //End Write and Read data with URL
    }

    private void addItem(String id, String type, String date, String amount) {
        View view = getLayoutInflater().inflate(R.layout.recent_transaction, null);
        TextView tid = view.findViewById(R.id.t_id);
        TextView ttype = view.findViewById(R.id.t_type);
        TextView tdate = view.findViewById(R.id.t_date);
        TextView tamount = view.findViewById(R.id.t_amount);

        tid.setText(id);
        ttype.setText(type);
        tdate.setText(date);
        tamount.setText(amount);
        layout.addView(view);
    }

    private void addItemTH(String id, String origin, String destination, String via, String timestamp, String departureTime) {
        View view = getLayoutInflater().inflate(R.layout.recent_trip_history, null);
        TextView trip_id = view.findViewById(R.id.trip_id);
        TextView place = view.findViewById(R.id.place);
        TextView trip_date = view.findViewById(R.id.trip_date);
        TextView dept_time = view.findViewById(R.id.dept_time);

        trip_id.setText(id);
        place.setText(origin + " - " + destination + " Via " + via);
        trip_date.setText(timestamp);
        dept_time.setText(departureTime);
        layoutTH.addView(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        id = this.getArguments().getString("accID");
        layout = view.findViewById(R.id.r_transaction);
        layoutTH = view.findViewById(R.id.r_trip_history);

        bal = view.findViewById(R.id.bal);
        empty = view.findViewById(R.id.empty);
//        emptyTH = view.findViewById(R.id.emptyTH);
        Button cashin = view.findViewById(R.id.cashin);

        cashin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CashIn.class);
                intent.putExtra("accID", id);
                startActivity(intent);
            }
        });

        SwipeRefreshLayout sw = view.findViewById(R.id.swipe);
        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWallet();
                getTransactions();
                getTripHistory();
                sw.setRefreshing(false);
            }
        });

        getWallet();
        getTransactions();
        getTripHistory();

        // Inflate the layout for this fragment
        return view;
    }
}