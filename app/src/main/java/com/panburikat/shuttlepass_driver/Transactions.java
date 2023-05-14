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

public class Transactions extends Fragment {
    String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        id = this.getArguments().getString("accID");

        ListView listView = view.findViewById(R.id.listView);

        ArrayList<TransactionList> transactionList = new ArrayList<>();
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
                data[1] = "none";
                PutData putData = new PutData("https://jamora.leon.svdphs.ph/getTransaction.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Error: Database connection") && !result.equals("No accountID")) {
                            if (!result.equals("No Results")) {
                                TransactionList[] tl = gson.fromJson(result, TransactionList[].class);
                                for (int x = 0; x < tl.length; x++) {
                                    TransactionList one = new TransactionList(tl[x].getTransactionID(), tl[x].getReferenceNum(), tl[x].getTransactionType(), tl[x].getDescription(), tl[x].getDate(), tl[x].getAmount());
                                    transactionList.add(one);
                                }
                                TransactionAdapter adapter = new TransactionAdapter(getActivity().getApplicationContext(), R.layout.transactions_layout, transactionList);
                                listView.setAdapter(adapter);
                            }
                        }

                    }
                }
            }
        });
        return view;
    }
}