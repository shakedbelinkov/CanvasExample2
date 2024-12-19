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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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

    public void leaveGame(View view) {
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
}