package com.panburikat.shuttlepass_driver.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

public class NetworkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Common.internetConnection(context)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Internet Connectivity");
            dialog.setMessage("There seems to be a problem with your internet connection. Please check your connection and try again!");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onReceive(context, intent);
                }
            }).show();
        }
    }
}
