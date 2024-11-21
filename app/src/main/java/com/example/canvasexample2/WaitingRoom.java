package com.example.canvasexample2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WaitingRoom extends AppCompatActivity implements DBGameRoom.GameRoomComplete
{
    private int numPlayers,numRounds,timeRounds;
    private DBGameRoom dbGameRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_waiting_room);
      initUI();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initUI() {

        Intent takeDetails = getIntent();
        numPlayers=takeDetails.getIntExtra("numPlayers",2);
        numRounds=takeDetails.getIntExtra("numRounds",1);
        timeRounds=takeDetails.getIntExtra("timeRounds",30);
        GameRoom gameRoom=new GameRoom(numPlayers,numRounds,timeRounds);
        gameRoom.setPlayer(numPlayers);
        gameRoom.setRoundNum(numRounds);
        gameRoom.setRoundTime(timeRounds);
        dbGameRoom=new DBGameRoom(this);

        // add the game room to the firebase

        String uidRef = DBAuth.getUserUID();
        dbGameRoom.addGameRoom(gameRoom,uidRef);


    }

    @Override
    public void onGameRoomComplete(boolean s) {
        Toast.makeText(this,"GameRoom " + s,Toast.LENGTH_LONG).show();

    }
}