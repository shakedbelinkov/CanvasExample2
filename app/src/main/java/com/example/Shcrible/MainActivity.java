package com.example.Shcrible;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.rpc.context.AttributeContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements DBDraw.AddDrawComplete,DBMessage.AddMessageComplete,DBGameRoom.GameRoomComplete {

    private MyCanvasView myCanvasView;
    public static String uidRef="", answer;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DBDraw dbDraw;
    private DBMessage dbMessage;
    private DBGameRoom dbGameRoom;
    private GameRoom gameroom;
    private ArrayList<String> uIDs,winners = new ArrayList<>();
    private ArrayList<Draw> draws = new ArrayList<>();
    private int counter = 0;//count whose turn is it
    private RecyclerView lv;
    private MessageAdapter messageAdapter;
    private CollectionReference msgRef;
    private ArrayList<Message> messages = new ArrayList<>();
    private int second;//second dor timer
    private int turn = 1;
    private Word word = new Word();
    private ListenerRegistration lr;
    private int typePlayer=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dbMessage = new DBMessage(this);
        dbDraw = new DBDraw(this);
        dbGameRoom = new DBGameRoom(this);
        myCanvasView = findViewById(R.id.canvas01);
        Intent takeDetails = getIntent();
        uidRef = takeDetails.getStringExtra("gameRoomCode");
        msgRef = db.collection("GameRooms").document(uidRef).collection("Message");
        initUI();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.startButton), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void initUI() {
        setRecyclerView();
        db.collection("GameRooms").document(uidRef).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    gameroom = task.getResult().toObject(GameRoom.class);
                    uIDs = gameroom.getUIDs();
                    startTurn();
                }
            }
        });
    }

    public void startTurn()
    //start new turn
    {
        //choose whose turn is it
        String name = whoseTurn();
        TextView textView = findViewById(R.id.word);
        second = gameroom.getRoundTime();
        Timer();
        if ((DBAuth.getUserUID().equals(name)))
        //check whose turn is it
        {
            word.ChooseWord();
            textView.setText(word.getWord());
            gameroom.setWord(word.getWord());
            dbGameRoom.addGameRoom(gameroom, uidRef);
            ConstraintLayout cl = findViewById(R.id.innerLayout);
            cl.setVisibility(View.VISIBLE);
            RecyclerView listView = findViewById(R.id.listview_chat);
            listView.setVisibility(View.INVISIBLE);
            EditText editText = findViewById(R.id.messageBox);
            editText.setVisibility(View.INVISIBLE);
            Button sendButton = findViewById(R.id.sendButton);
            sendButton.setVisibility(View.INVISIBLE);
            typePlayer=1;
            myCanvasView.ChangePlayerType(typePlayer);
            //if it your turn start countDownTimer, every five seconds it update
            new CountDownTimer(gameroom.getRoundTime() * 1000, 2000) {

                public void onTick(long millisUntilFinished) {
                    ArrayList<Draw> arr =myCanvasView.getArrayList();
                    if ( arr== null || arr.size() == 0)
                        return;
                    dbDraw.addDraw(arr);
                }

                public void onFinish() {
                    if (turn <= gameroom.getRoundNum()) {
                        dbDraw.addDraw(myCanvasView.getArrayList());
                        if (counter == gameroom.getCounterOfPlayers() - 1)
                            counter = 0;
                        else
                            counter++;
                        dbDraw.removeDraw();
                        setPoint();
                        myCanvasView.delete();
                        startTurn();
                    }
                }
            }.start();

        } else {
            ConstraintLayout cl = findViewById(R.id.innerLayout);
            cl.setVisibility(View.INVISIBLE);
            RecyclerView listView = findViewById(R.id.listview_chat);
            listView.setVisibility(View.VISIBLE);
            EditText editText = findViewById(R.id.messageBox);
            editText.setVisibility(View.VISIBLE);
            Button sendButton = findViewById(R.id.sendButton);
            sendButton.setVisibility(View.VISIBLE);
            typePlayer=2;
            myCanvasView.ChangePlayerType(typePlayer);
            listenForWord();
            listenForDraws(uidRef);
            new CountDownTimer(gameroom.getRoundTime() * 1000, 2000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    if (turn <= gameroom.getRoundNum()) {
                        if (counter == gameroom.getCounterOfPlayers() - 1)
                            counter = 0;
                        else
                            counter++;
                        lr.remove();
                        setPoint();
                        myCanvasView.delete();
                        startTurn();
                    }
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
        Toast.makeText(this, "Draw " + s, Toast.LENGTH_LONG).show();
    }

    public void listenForDraws(String uidRef)
    //listening for change-every time
    {
        lr= db.collection("GameRooms").document(uidRef).collection("Draw").document(uidRef).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.exists() || value.getData() == null)
                    return;
                Draw[] arr = new Draw[1];
                arr = TreeMapToDraw(value.getData());
                if (arr == null)
                    return;
                //     if(arr[0] !=null && arr[1] !=null && arr[2]!=null)

                Log.d("log arr", "onEvent: " + arr[0].toString() + "size: " + arr.length);
                myCanvasView.drawFromDB(arr);

            }
        });

    }

    // TREEMAP
    // Each String represent index of the object in the draw array
    // each value / Object represent an hashmap that is converted to a DRAW object
    public Draw[] TreeMapToDraw(Map<String, Object> map) {
        if (map.size() == 0)
            return null;
        Draw[] arrDraw = new Draw[map.size()];

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            // key - map to an index in the array
            String key = entry.getKey();
            // each value map to draw object
            Map<String, Object> value = (Map<String, Object>) entry.getValue();

            Draw d = new Draw();
            d.hashmapToDraw(value);

            // d should be a Draw object

            int index = Integer.parseInt(key);
            Log.d("TESTING!!!", "TreeMapToDraw: " + d);


            // add d to the arraylist
            arrDraw[index] = d;
        }


        return arrDraw;
    }

    public void leaveGame(View view) {
        //leave the game
        db.collection("GameRooms").document(uidRef).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        GameRoom gameRoom = task.getResult().toObject(GameRoom.class);

                        //LOCALLY!!
                        if (gameRoom.deleteUser(DBAuth.getUserUID())) {
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

    public void setRecyclerView() {
        Query query = msgRef.orderBy("timestamp", Query.Direction.DESCENDING);
        lv = findViewById(R.id.listview_chat);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(this));
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(msgRef, Message.class)
                .build();
        messageAdapter = new MessageAdapter(options);
        lv.setAdapter(messageAdapter);
    }

    public String whoseTurn() {
        if (counter == uidRef.length())
            counter = 0;
        return uIDs.get(counter);
    }

    public void SendMessage(View view) {
        EditText message = findViewById(R.id.messageBox);
        String messageText = message.getText().toString();
        message.setText("");
        Message msg = new Message(DBAuth.getUserName(), messageText);
        if (word.isRight(messageText)) {
            msg.setRight(true);
            winners.add(DBAuth.getUserName());
        }
        dbMessage.addMessage(msg, uidRef);
    }

    @Override
    protected void onStart() {
        super.onStart();
        messageAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        messageAdapter.stopListening();
    }

    @Override
    public void onMessageComplete(boolean s) {

    }

    public void Timer() {
        new CountDownTimer(gameroom.getRoundTime() * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                TextView timer = findViewById(R.id.timer);
                if (second < 10)
                    timer.setText("00:0" + second);
                else
                    timer.setText("00:" + second);
                second--;
            }

            public void onFinish() {
                second = gameroom.getRoundTime();
            }
        }.start();
    }

    public void listenForWord() {
        db.collection("GameRooms").document(uidRef).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override

            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    GameRoom gm = value.toObject(GameRoom.class);
                    word.setWord(gm.getWord());
                    TextView textView = findViewById(R.id.word);
                    textView.setText(word.Clue());
                }
            }
        });
    }
    @Override
    public void onGameRoomComplete(boolean s) {

    }
    public void setPoint()
    {

        int points=150;
        if (winners!=null) {
            for (int i = 0; i < winners.size(); i++) {
                if (winners.get(i).equals(DBAuth.getUserName()))
                    dbGameRoom.UpdatePoints(DBAuth.getUserUID(), points);
                points -= 25;
            }

        }
    }
    public void SetDialog()
    {
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.end_round_dialog);
        TextView txWord=dialog.findViewById(R.id.theWordIs);
        txWord.setText(word.getWord());
        TextView txWinner=dialog.findViewById(R.id.winner);
        if (winners==null)
            txWinner.setText("you fail");
        txWinner.setText(winners.get(0));
        winners.clear();

    }
}