package com.example.Shcrible;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class networkReceiver extends BroadcastReceiver {
    public static boolean isNetworkAvailable=true;
    private checkNetworkComplete checkNetworkComplete;
    public interface checkNetworkComplete{
        public void NetworkNotWorking();
        public void NetworkIsWorking();
    }
    public networkReceiver(checkNetworkComplete checkNetworkComplete)
    {
        this.checkNetworkComplete=checkNetworkComplete;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        networkReceiver.isNetworkAvailable=isNetworkAvailable(context);
        //check if you have internet
        if (isNetworkAvailable)
            checkNetworkComplete.NetworkIsWorking();
        else
            checkNetworkComplete.NetworkNotWorking();
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