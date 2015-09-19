package com.digitalmedia.millenials.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Digital Media on 9/19/2015.
 */
public class Utilities {
    public static boolean isOnline(Context context) {
        /*Check if the device is connected to the internet
        * returns true if connected and false and a toast message if connection is not available*/
        ConnectivityManager cm = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }
}
