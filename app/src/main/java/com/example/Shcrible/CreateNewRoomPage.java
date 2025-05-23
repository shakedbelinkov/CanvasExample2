package com.example.Shcrible;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.BlockingDeque;

public class CreateNewRoomPage extends AppCompatActivity implements networkReceiver.checkNetworkComplete  {
     private Intent intent;
    Spinner spinnerPlayer,spinnerRounds,spinnerTime;
    private networkReceiver networkReceiver;
    private Dialog dialog;
    private int numPlayers=2,numRounds=1,time=20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_new_room_page);
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


        initUI();



    }

    private void initUI() {
        //spinners have information for the game room
        Intent takeDetails=getIntent();
        int openAgain=takeDetails.getIntExtra("openAgain",2);
        //if i am the host and i want to open the same room again
        if (openAgain==1)
        {
            numPlayers=takeDetails.getIntExtra("numPlayers",2);
            numRounds=takeDetails.getIntExtra("numRounds",1);
            time=takeDetails.getIntExtra("timeRounds",20);
            startNextActivity(findViewById(R.id.startButton));
        }
        spinnerPlayer=findViewById(R.id.numOfPlayers);
        //create new spinner for num of player
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.numberOfPlayer,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayer.setAdapter(adapter);
        //create new spinner for num of rounds
        spinnerRounds=findViewById(R.id.numberOfRounds);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.numberOfRounds,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRounds.setAdapter(adapter2);
        //create new spinner for time for a round
        spinnerTime=findViewById(R.id.timeForRound);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.timeForRound
                ,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapter3);
        //start a listener for every spinner and update his value
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String answer= adapterView.getItemAtPosition(i).toString();//answer
                int result = Integer.valueOf(answer);
                time=result;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerRounds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String answer= adapterView.getItemAtPosition(i).toString();//answer
                int result = Integer.valueOf(answer);
                numRounds=result;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerPlayer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String answer= adapterView.getItemAtPosition(i).toString();//answer
                int result = Integer.valueOf(answer);
                numPlayers=result;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public void startNextActivity(View view) {
        //when the user finish to choose we move to the next activity=> WaitingRoom
        Intent takeDetails = getIntent();
        intent=new Intent(CreateNewRoomPage.this,WaitingRoom.class);
        intent.putExtra("numPlayers",numPlayers);
        intent.putExtra("numRounds",numRounds);
        intent.putExtra("timeRounds",time);
        intent.putExtra("isHost",1);
        startActivity(intent);
        finish();

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