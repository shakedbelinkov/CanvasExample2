package com.example.Shcrible;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class WaitingRoom extends AppCompatActivity implements DBGameRoom.GameRoomComplete
{
    private Intent takeDetails;
    private int numPlayers,numRounds,timeRounds;//information for the game room
    private DBGameRoom dbGameRoom;
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

        // 1 -if I am the host

        // 2 - if I am a player that joins an existing room
        //          if i am not the host
        //              get the code from the intent
        takeDetails = getIntent();
        int isHost=takeDetails.getIntExtra("isHost",1);
        if (isHost==1)
            host();
        else
            player();
        //put the game code
        TextView t=findViewById(R.id.GameRoomCodeUID);
        t.setText(uidRef);
    }
    public void host()
            //create new game room
    {
        numPlayers=takeDetails.getIntExtra("numPlayers",2);
        numRounds=takeDetails.getIntExtra("numRounds",1);
        timeRounds=takeDetails.getIntExtra("timeRounds",30);
        String name=DBAuth.getUserName();
        GameRoom gameRoom=new GameRoom(numPlayers,numRounds,timeRounds);
        gameRoom.setPlayerNum(numPlayers);
        gameRoom.setRoundNum(numRounds);
        gameRoom.setRoundTime(timeRounds);
        gameRoom.AddUser(DBAuth.getUserUID(),name);
        //add the game room to the firebase

        uidRef = DBAuth.getUserUID();
        dbGameRoom = new DBGameRoom(this);

        dbGameRoom.addGameRoom(gameRoom,uidRef);
        Button startButton=findViewById(R.id.startGameButton);
        startButton.setVisibility(View.VISIBLE);
    }
    public void player()
    {
        // room code
        uidRef=takeDetails.getStringExtra("gameRoomCode");
        listenForGameUsers(uidRef);
    }
    @Override
    public void onGameRoomComplete(boolean s) {
        Toast.makeText(this,"GameRoom " + s,Toast.LENGTH_LONG).show();
        listenForGameUsers(uidRef);
    }

    private void listenForGameUsers(String uidRef) {
        //add the player name to the list view
        //remove the listener- else it wiil crash once leaved- therefore adding "this" to the method call
        db.collection("GameRooms").document(uidRef).addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
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

    public void leaveRoom(View view) {
        //delete the user from the list and close the app
        db.collection("GameRooms").document(uidRef).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        GameRoom gameRoom = task.getResult().toObject(GameRoom.class);

                        //LOCALLY!!
                        if(gameRoom.deleteUser(DBAuth.getUserUID())) {
                            // set in the firebase
                            db.collection("GameRooms").document(uidRef).set(gameRoom).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                        finish();
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public void StartGame(View view) {
        Intent intent=new Intent(WaitingRoom.this, MainActivity.class);
        intent.putExtra("gameRoomCode",uidRef);
        startActivity(intent);
    }
}