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


        // kHqSAzgOX0PNlvo8dLCWNQn0Sx33

     //   listenForDraws("kHqSAzgOX0PNlvo8dLCWNQn0Sx33");

        name = DBAuth.getUserName();
        textView=findViewById(R.id.playerName);
        textView.setText("Hi "+name);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

/*
    ///
    public void listenForDraws(String uidRef)
    //listening for change-every time
    {
        Query query = db.collection("GameRooms").document(uidRef).collection("Draw");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.getDocuments().size()==0)
                    return;
                Draw[] arr = new Draw[1];
                for (DocumentChange doc:value.getDocumentChanges())
                {
                    switch (doc.getType())
                    {
                        case ADDED:
                            arr = TreeMapToDraw(doc.getDocument().getData());
                            myCanvasView.drawFromDB(arr);
                            break;
                        case MODIFIED:
                            arr = TreeMapToDraw(doc.getDocument().getData());
                            myCanvasView.drawFromDB(arr);
                            break;


                    }
                }
                for (DocumentSnapshot doc:value.getDocuments()) {
                    // each doc is TreeMap of Hashmap
                    // each hashmap represent a draw object
                    arr = TreeMapToDraw(doc.getData());
                    /////    myCanvasView.drawFromDB(arr);


                }
            }
        });
    }

    // TREEMAP
    // Each String represent index of the object in the draw array
    // each value / Object represent an hashmap that is converted to a DRAW object
    public Draw[] TreeMapToDraw(Map<String,Object> map)
    {
        if(map.size()==0)
            return null;
        Draw[] arrDraw =new Draw[map.size()];

        for (Map.Entry<String,Object> entry:map.entrySet())

        {
            // key - map to an index in the array
            String key = entry.getKey();
            // each value map to draw object
            Map<String,Object> value = (Map<String,Object>)entry.getValue();

            Draw d = new Draw();
            d.hashmapToDraw(value);

            // d should be a Draw object

            int index =Integer.parseInt(key);
            Log.d("TESTING!!!", "TreeMapToDraw: " +d);


            // add d to the arraylist
            arrDraw[index] =d;
        }



        return arrDraw;
    }

 */



    ///

    public void createNewRoom(View view) {
        //move activity=> CreateNewRoomPage
        Intent intent=new Intent(GameLobby.this, CreateNewRoomPage.class);
        startActivity(intent);
        finish();
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
                                            finish();
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