package com.example.hikertracker;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class WifiState {

    Context context;

    public WifiState(Context context) {
        this.context = context;
    }

    public Boolean haveNetworkConnection() {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;

        }

        if (!haveConnectedWifi && !haveConnectedMobile) {

            //do something to handle if wifi & mobiledata is disabled

        } else {

        }

        return haveConnectedMobile || haveConnectedWifi;
    }
}


