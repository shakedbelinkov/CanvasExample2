package com.example.Shcrible;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DBDraw.AddDrawComplete{

    private MyCanvasView myCanvasView;
    private String uidRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DBDraw dbDraw=new DBDraw(this);
    private GameRoom gameroom;
    private ArrayList<String> uIDs;
    private ArrayList<Draw> draws=new ArrayList<>();
    private int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
      //  myCanvasView=new MyCanvasView(this);
        myCanvasView = findViewById(R.id.canvas01);
        Intent takeDetails = getIntent();
        uidRef=takeDetails.getStringExtra("gameRoomCode");
        initUI();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.startButton), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void initUI()
    {

       db.collection("GameRooms").document(uidRef).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if (task.isSuccessful())
               {
                   gameroom=task.getResult().toObject(GameRoom.class);
                   uIDs=gameroom.getUIDs();
                   startTurn();
               }
           }
       });
    }
    public void startTurn()
    {
        String name=whoseTurn();
        if ((DBAuth.getUserUID().equals(name)))
        {
            new CountDownTimer(gameroom.getRoundTime()*1000, 5000) {

                public void onTick(long millisUntilFinished) {
                    dbDraw.addDraw(myCanvasView.getArrayList(),uidRef);
                }

                public void onFinish() {
                    dbDraw.addDraw(myCanvasView.getArrayList(),uidRef);
                    if (counter==gameroom.getCounterOfPlayers()-1)
                        counter=0;
                    else
                        counter++;
                }
            }.start();

        }
        else
        {
            ConstraintLayout cl = findViewById(R.id.innerLayout);
            cl.setVisibility(View.INVISIBLE);
            listenForDraws(uidRef);
        }
    }



    public void changeBrushColor(View view) {
        //change the brush color
        String color = view.getTag().toString();
        int brushColor = Color.parseColor(color);
        myCanvasView.changeBrushColor(brushColor);
    }

    public void eraser(View view) {
        //change the brush color to the background color- eraser
        myCanvasView.eraser();
    }

    public void delete(View view) {
        myCanvasView.delete();
    }

    public void changeBrushSize(View view) {
        //change the brush size
        int size = Integer.valueOf((String) view.getTag());
        myCanvasView.changeBrushSize(size);
    }

    @Override
    public void onDrawComplete(boolean s) {
        Toast.makeText(this,"Draw " + s,Toast.LENGTH_LONG).show();
    }
    public void listenForDraws(String uidRef)
            //listening for change-every time
    {
        db.collection("GameRoom").document(uidRef).collection("Draw").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<Draw> drawList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        drawList.add(doc.toObject(Draw.class));
                    }
                }
                    myCanvasView.drawFromDB(drawList);
            }
        });
    }
    /*public ArrayList<Draw> HashMapToDraw(HashMap<Integer, HashMap<String,Object>> map)
    {
        ArrayList<Draw> drawArrayList=new ArrayList<>()
        HashMap<String,Object> map2=map.get(0);
        for (int i=0;i< map2.size()-1;i++)
        {
        //    drawArrayList.add(map2.get(Draw.class));
        }
        return drawArrayList;
    }*/

    public void leaveGame(View view) {
        //leave the game
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
    public String whoseTurn()
    {
        return uIDs.get(counter);
    }
}