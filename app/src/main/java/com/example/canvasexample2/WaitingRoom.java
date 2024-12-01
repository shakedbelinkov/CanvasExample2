package com.example.canvasexample2;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class WaitingRoom extends AppCompatActivity implements DBGameRoom.GameRoomComplete
{
    private int numPlayers,numRounds,timeRounds;//information for the game room
    private DBGameRoom dbGameRoom;
    private int counterPlayers=0;
    private String uidRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> names;
    ListView lv;
    ArrayAdapter<String> arrayAdapter;
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
        //get the information from the
        Intent takeDetails = getIntent();
        numPlayers=takeDetails.getIntExtra("numPlayers",2);
        numRounds=takeDetails.getIntExtra("numRounds",1);
        timeRounds=takeDetails.getIntExtra("timeRounds",30);
        String name=takeDetails.getStringExtra("name");
        GameRoom gameRoom=new GameRoom(numPlayers,numRounds,timeRounds);
        gameRoom.setPlayerNum(numPlayers);
        gameRoom.setRoundNum(numRounds);
        gameRoom.setRoundTime(timeRounds);
        gameRoom.AddUser(DBAuth.getUserUID(),name);
        //add the game room to the firebase

        uidRef = DBAuth.getUserUID();
        dbGameRoom = new DBGameRoom(this);

        dbGameRoom.addGameRoom(gameRoom,uidRef);
        //put the game code
        TextView t=findViewById(R.id.GameRoomCodeUID);
        t.setText(uidRef);



    }

    @Override
    public void onGameRoomComplete(boolean s) {
        Toast.makeText(this,"GameRoom " + s,Toast.LENGTH_LONG).show();


        //add the player name to the list view
        db.collection("GameRooms").document(uidRef).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.exists())
                {
                    GameRoom gm=value.toObject(GameRoom.class);
                    names=gm.getNames();
                    lv=findViewById(R.id.playersList);
                    arrayAdapter=new ArrayAdapter<String>(WaitingRoom.this, android.R.layout.simple_list_item_1,names);
                    lv.setAdapter(arrayAdapter);
                }
            }
        });

    }

    public void copyCode(View view) {
        //copy the game room code to the clip data
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("game room code", uidRef);
                clipboard.setPrimaryClip(clip);
            }
        });
    }
}