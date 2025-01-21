package com.example.Shcrible;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements DBDraw.AddDrawComplete,DBMassage.AddMassageComplete{

    private MyCanvasView myCanvasView;
    private String uidRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DBDraw dbDraw=new DBDraw(this);
    private DBMassage dbMassage=new DBMassage(this);
    private GameRoom gameroom;
    private ArrayList<String> uIDs;
    private ArrayList<Draw> draws=new ArrayList<>();
    private int counter=0;//count whose turn is it
    private ListView lv;//list view
    private MessageAdapter messageAdapter;
    private ArrayList<Massage> massages=new ArrayList<>();


    ListenerRegistration registration = null;


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
            //start new turn
    {
        String name=whoseTurn();
        if ((DBAuth.getUserUID().equals(name)))
        //check whose turn is it
        {
            ConstraintLayout cl = findViewById(R.id.innerLayout);
            cl.setVisibility(View.VISIBLE);
            ListView listView=findViewById(R.id.listview_chat);
            listView.setVisibility(View.INVISIBLE);
            EditText editText=findViewById(R.id.massageBox);
            editText.setVisibility(View.INVISIBLE);
            //if it your turn start countDownTimer, every five seconds it update
            new CountDownTimer(gameroom.getRoundTime()*1000, 2000) {

                public void onTick(long millisUntilFinished) {
                    if(myCanvasView.getArrayList()==null || myCanvasView.getArrayList().size()==0)
                        return;
                    dbDraw.addDraw(myCanvasView.getArrayList(),uidRef);
                }


                public void onFinish() {
                    dbDraw.addDraw(myCanvasView.getArrayList(),uidRef);
                    if (counter==gameroom.getCounterOfPlayers()-1)
                        counter=0;
                    else
                        counter++;


                    //db.collection("GameRooms").document(uidRef).collection("Draw")
                }
            }.start();

        }
        else
        {
            ConstraintLayout cl = findViewById(R.id.innerLayout);
            cl.setVisibility(View.INVISIBLE);
            ListView listView=findViewById(R.id.listview_chat);
            listView.setVisibility(View.VISIBLE);
            EditText editText=findViewById(R.id.massageBox);
            editText.setVisibility(View.VISIBLE);
            listenForDraws(uidRef);
            new CountDownTimer(gameroom.getRoundTime()*1000, 2000) {
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    if (counter==gameroom.getCounterOfPlayers()-1)
                        counter=0;
                    else
                        counter++;

                    if(registration!=null)
                        registration.remove();
                }
            }.start();
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
        db.collection("GameRooms").document(uidRef).collection("Draw").document(uidRef).addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
           @Override
           public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
               if(!value.exists() || value.getData()==null)
                   return;
               Draw[] arr = new Draw[1];
               arr = TreeMapToDraw(value.getData());
               if(arr==null)
                   return;
          //     if(arr[0] !=null && arr[1] !=null && arr[2]!=null)

                    Log.d("log arr", "onEvent: " + arr[0].toString() + "size: " +arr.length);
               myCanvasView.drawFromDB(arr);

           }
       });

        /* Query query =
        registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    myCanvasView.drawFromDB(arr);


                }
            }
        });

         */
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
    public void ListView()
    {
        messageAdapter=new MessageAdapter(this,0,0,massages);
        lv=findViewById(R.id.listview_chat);
        lv.setAdapter(messageAdapter);
    }
    public String whoseTurn()
    {
        return uIDs.get(counter);
    }

    public void SendMassage(View view) {
        EditText massage=findViewById(R.id.massageBox);
        String massageText=massage.getText().toString();
        dbMassage.addMassage(massageText,uidRef);
    }
    public void ListenForMassage()
    {
        /*db.collection("GameRooms").document(uidRef).collection("Massage").addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.exists())
                {

                    Massage msg=value.toObject(Massage.class);
                    massages.add(msg);
                    lv=findViewById(R.id.listview_chat);
                    messageAdapter=new MessageAdapter(this,0,0,massages);
                }
            }
        });*/
    }

    @Override
    public void onMassageComplete(boolean s) {

    }
}