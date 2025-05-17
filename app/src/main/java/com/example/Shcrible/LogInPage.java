package com.example.Shcrible;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogInPage extends AppCompatActivity implements DBAuth.AuthComplete, DBUser.AddUserComplete,networkReceiver.checkNetworkComplete {

    private DBAuth mAuth= new DBAuth(this);
    private networkReceiver networkReceiver;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in_page);
        //create object of networkReceiver and create new dialog
        networkReceiver = new networkReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
        dialog= new Dialog(this);
        dialog.setContentView(R.layout.internet_dialog);
        dialog.setCancelable(false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void LogIn(View view) {
        //get the information a
        TextView emailTv=findViewById(R.id.emailLogIn);
        String email=emailTv.getText().toString();
        TextView passwordTv=findViewById(R.id.passwordLogIn);
        String password=passwordTv.getText().toString();
        mAuth.LogIn(email,password);
    }

    @Override
    public void onComplete(boolean s) {
        //if you finish login moet to GameLobby
        Intent intent=new Intent(LogInPage.this,GameLobby.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUserComplete(boolean s) {

    }
    //if you have no internet
    @Override
    public void NetworkNotWorking() {
        dialog.show();
    }
    //if you have internet
    @Override
    public void NetworkIsWorking() {
        dialog.dismiss();
    }
}