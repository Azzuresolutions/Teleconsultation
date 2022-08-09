package com.volumetree.newswasthyaingitopd.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class ConnectivityReceiver extends BroadcastReceiver
{

    public ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver()
    {
        super();
    }

 
    @Override
    public void onReceive(Context context, Intent arg1)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
 
        if (connectivityReceiverListener != null)
        {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }
 
    public interface ConnectivityReceiverListener
    {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}