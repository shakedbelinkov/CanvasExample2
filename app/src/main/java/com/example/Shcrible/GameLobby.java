package com.example.Shcrible;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class GameLobby extends AppCompatActivity {

    private TextView textView;//message for the user-"hi name"
    private Dialog d;//dialog- ask for the gameCode
    private EditText gameRoomCode;//editText of the dialog-give the game room code
    private Button btnCustomLogin;//button of the dialog- end the dialog
    private String code;// UID of the Game Room
    private String name;//the name of the player
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_lobby);
        Intent takeDetails=getIntent();
        int keepPlaying=takeDetails.getIntExtra("playAgain",2);
        //1-keep playing,2-came from LogIn.class/OpeningPage.class
        if (keepPlaying==1)
        {
            String uidRef=takeDetails.getStringExtra("uidRef");
            connectToTheGameRoom(uidRef);
        }

        name = DBAuth.getUserName();
        textView=findViewById(R.id.playerName);
        textView.setText("Hi "+name);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void createNewRoom(View view) {
        //move activity=> CreateNewRoomPage
        Intent intent=new Intent(GameLobby.this, CreateNewRoomPage.class);
        startActivity(intent);
    }

    public void createDialog(View view) {
        //create dialog
        d= new Dialog(this);
        d.setContentView(R.layout.dialoglogin);
        d.setTitle("Login");
        d.setCancelable(true);
        gameRoomCode=(EditText)d.findViewById(R.id.gameRoomCode);
        btnCustomLogin=(Button)d.findViewById(R.id.loginButton);
        btnCustomLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    gameRoomCode=findViewById(R.id.gameRoomCode);
                code=gameRoomCode.getText().toString();
                d.dismiss();
                connectToTheGameRoom(code);
            }
        });
        d.show();
    }
    public void connectToTheGameRoom(String code)
            //add the user to the game room
    {
        //get the data
        db.collection("GameRooms").document(code).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    Log.d("onComplete", "onComplete: ");
                    if(task.getResult().exists()) {
                        GameRoom gameRoom = task.getResult().toObject(GameRoom.class);

                        // this means we do not exceed the max num of players

                            //LOCALLY!!
                            if(gameRoom.AddUser(DBAuth.getUserUID(), DBAuth.getUserName())) {


                                // set in the firebase
                                db.collection("GameRooms").document(code).set(gameRoom).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("GAME ROOM ", "onComplete: added player name success");

                                            // move to waiting room
                                            Intent intent=new Intent(GameLobby.this,WaitingRoom.class);
                                            intent.putExtra("gameRoomCode",code);
                                            intent.putExtra("isHost",2);
                                            startActivity(intent);
                                        }
                                        else
                                            Log.d("GAME ROOM ", "onComplete: added player name FAIL");
                                    }
                                });
                            }

                        }
                    }
                }


        });

    }
}