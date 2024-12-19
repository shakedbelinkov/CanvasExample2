package com.example.Shcrible;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DBDraw.AddDrawComplete{

    private MyCanvasView myCanvasView;
    private String uidRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GameRoom gameroom;
    private ArrayList<String> uIDs;
    private ArrayList<Draw> draws=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
      //  myCanvasView=new MyCanvasView(this);
        myCanvasView = findViewById(R.id.canvas01);
        Intent takeDetails = getIntent();
        uidRef=takeDetails.getStringExtra("gameRoomCode");
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
        String name=gameroom.whoseTurn();
        if (!(DBAuth.getUserUID().equals(name)))
        {
            ConstraintLayout cl = findViewById(R.id.innerLayout);
            cl.setVisibility(View.INVISIBLE);
        }
    }



    public void changeBrushColor(View view) {

        String color = view.getTag().toString();
        int brushColor = Color.parseColor(color);
        myCanvasView.changeBrushColor(brushColor);
    }

    public void eraser(View view) {
        myCanvasView.eraser();
    }

    public void delete(View view) {
        myCanvasView.delete();
    }

    public void changeBrushSize(View view) {
        int size = Integer.valueOf((String) view.getTag());
        myCanvasView.changeBrushSize(size);
    }

    @Override
    public void onDrawComplete(boolean s) {
        Toast.makeText(this,"Draw " + s,Toast.LENGTH_LONG).show();
    }
    public void listenForDraws(String uidRef)
    {
        db.collection("Draw").document(uidRef).addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.exists())
                {
                    Draw draw=value.toObject(Draw.class);
                    draws.add(draw);
                    myCanvasView.drawFromDB(draw);


                }
            }
        });
    }
}