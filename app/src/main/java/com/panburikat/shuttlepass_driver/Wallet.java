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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        id = this.getArguments().getString("accID");
        layout = view.findViewById(R.id.r_transaction);

        bal = view.findViewById(R.id.bal);
        empty = view.findViewById(R.id.empty);
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
                sw.setRefreshing(false);
            }
        });

        getWallet();
        getTransactions();

        // Inflate the layout for this fragment
        return view;
    }
}