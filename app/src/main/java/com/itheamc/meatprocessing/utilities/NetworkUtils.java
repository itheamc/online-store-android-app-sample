package com.itheamc.meatprocessing.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.itheamc.meatprocessing.R;

public class NetworkUtils {

    /*
   Function to
   Check Internet connection
    */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Possible network types
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
        NetworkInfo ethernetConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

        return (wifiConn != null && wifiConn.isConnected()) ||
                (mobileConn != null && mobileConn.isConnected()) ||
                (bluetoothConn != null && bluetoothConn.isConnected()) ||
                (ethernetConn != null && ethernetConn.isConnected());
    }
}
