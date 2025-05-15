package com.example.Shcrible;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class networkReceiver extends BroadcastReceiver {
    public static boolean isNetworkAvailable=true;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        networkReceiver.isNetworkAvailable=isNetworkAvailable(context);
        /*if(isNetworkAvailable)
            Toast.makeText(context,"Network is avialable",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"Network isn't avialable",Toast.LENGTH_SHORT).show();
        */
    }
    public boolean isNetworkAvailable(Context context)
            //check if there network available
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null)
        {
            NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo!=null&& activeNetworkInfo.isConnected();
        }
        return false;
    }
}