package com.example.Shcrible;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DBDraw.AddDrawComplete,DBMessage.AddMessageComplete,DBGameRoom.GameRoomComplete,DBUser.AddUserComplete,networkReceiver.checkNetworkComplete {

    private MyCanvasView myCanvasView;
    public static String uidRef="";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DBDraw dbDraw;
    private DBMessage dbMessage;
    private DBUser dbUser;
    private DBGameRoom dbGameRoom;
    private GameRoom gameroom;
    private ArrayList<String> uIDs,winners = new ArrayList<>();
    private ArrayList<Draw> draws = new ArrayList<>();
    private int counter = -1,roundCounter=0;//count whose turn is it
    private RecyclerView lv;
    private MessageAdapter messageAdapter;
    private CollectionReference msgRef;
    private int second;//second dor timer
    private Word word = new Word();
    private ListenerRegistration lr, wordListener,startListener;
    private int typePlayer=1;
    private Dialog dialog;
    private boolean isWin=false;
    private networkReceiver networkReceiver;
    private Dialog internetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dbMessage = new DBMessage(this);
        dbDraw = new DBDraw(this);
        dbGameRoom = new DBGameRoom(this);
        dbUser=new DBUser(this);
        myCanvasView = findViewById(R.id.canvas01);
        Intent takeDetails = getIntent();
        uidRef = takeDetails.getStringExtra("gameRoomCode");
        msgRef = db.collection("GameRooms").document(uidRef).collection("Message");
        //create object of networkReceiver and create new dialog
        networkReceiver = new networkReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
        internetDialog= new Dialog(this);
        internetDialog.setContentView(R.layout.internet_dialog);
        internetDialog.setCancelable(false);
        initUI();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.startButton), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void initUI() {
        setRecyclerView();
        //get the list of uids
        db.collection("GameRooms").document(uidRef).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    gameroom = task.getResult().toObject(GameRoom.class);
                    uIDs = gameroom.getUIDs();
                    //clean all the messages
                    dbGameRoom.cleanChat(uidRef);
                    startTurn();
                }
            }
        });
    }

    public void startTurn()
    //start new turn
    {
        //counter-whose turn is it (index)
        if (counter == gameroom.getCounterOfPlayers() - 1)
            counter = 0;
        else
            counter++;
        roundCounter++;
        //choose whose turn is it
        String name = whoseTurn();
        isWin=false;
        TextView textView = findViewById(R.id.word);
        second = gameroom.getRoundTime();
        Timer();
        //if all the player have gm.getRoundNum games the game stop
        if (gameroom.getRoundNum()*gameroom.getCounterOfPlayers()  < roundCounter) {
            SetEndGameDialog();
            return;
        }
        if ((DBAuth.getUserUID().equals(name)))
        //check whose turn is it
        {
            if (startListener!=null)
                startListener.remove();
            draws.clear();
            word.ChooseWord();
            textView.setText(word.getWord());
            gameroom.setWord(word.getWord());
            //dbGameRoom.addGameRoom(gameroom, uidRef);
            dbGameRoom.updateWord(uidRef,word.getWord());
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
            //if it your turn start Thread, every five seconds it update

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i <gameroom.getRoundTime()/2; i++) {

                        try {
                            int finalI = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //update the list of draws to firebase
                                    ArrayList<Draw> arr = myCanvasView.getArrayList();
                                    if (arr == null || arr.size() == 0)
                                        return;
                                    dbDraw.addDraw(arr);
                                    Log.d("addDrawCheck", "add draw " + finalI);
                                }
                            });
                            //there is delay so that the action occurs every two seconds
                            Thread.sleep(2000);


                        }
                        catch (Exception e)
                        {
                            Log.d("THREAD EXCEPTION! ", "run: " + e.getMessage());
                        }


                    }

                    Log.d("Testing sys clock", "LOOP ENDED");

                    // FINISHED THE GAME
                    // NEED TO ACCESS DISPLAY- RUN ON UI

                    // takes the code from the thread and moves it to the Activity (UITHread)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                dbDraw.removeDraw();
                                setPoint();
                                myCanvasView.delete();
                                draws.clear();
                                SetDialog();

                        }
                    });


                }
            }).start();
        }
        // Wannabe Van Gogh
        else {
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



            new Thread(new Runnable() {
                @Override
                public void run() {
                    //there is delay so that the action start at the end of the round
                    SystemClock.sleep(gameroom.getRoundTime()*1000);
                    // FINISHED THE GAME
                    // NEED TO ACCESS DISPLAY- RUN ON UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                lr.remove();
                                wordListener.remove();
                                setPoint();
                                myCanvasView.delete();
                                draws.clear();
                                SetDialog();
                                //  countDownTimer.cancel();
                                Log.d("GAME_TROUBLE ", "player end round " + DBAuth.getUserName() + SystemClock.currentThreadTimeMillis());

                        }
                    });
                }
            }).start();
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
    }

    public void listenForDraws(String uidRef)
    //listening for change-every time
    {
        DocumentReference dr =db.collection("GameRooms").document(uidRef).collection("Draw").document(uidRef);
        lr = dr.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.exists() || value.getData() == null)
                    return;
                if (typePlayer==2) {
                    Draw[] arr = new Draw[1];
                    arr = TreeMapToDraw(value.getData());
                    if (arr == null)
                        return;
                    myCanvasView.drawFromDB(arr);
                }
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
                                    if (task.isSuccessful()) {
                                        lr.remove();
                                        wordListener.remove();
                                        Intent intent=new Intent(MainActivity.this,GameLobby.class);
                                        startActivity(intent);

                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }
    //Set the recycleView of the chat
    public void setRecyclerView() {
        //all the messages will organize by date
        Query query = msgRef.orderBy("date", Query.Direction.ASCENDING);
        lv = findViewById(R.id.listview_chat);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(this));
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();
        messageAdapter = new MessageAdapter(options);
        lv.setAdapter(messageAdapter);
    }

    public String whoseTurn() {
        //check whose turn is it, and return his id
        if (counter == uidRef.length())
            counter = 0;
        return uIDs.get(counter);
    }

    public void SendMessage(View view) {
        //when someone click sent , the function take his text and add her to the firebase
        //take the text

        EditText message = findViewById(R.id.messageBox);
        String messageText = message.getText().toString();
        message.setText("");
        if (!isWin) {
            Message msg = new Message(DBAuth.getUserName(), messageText);
            //check if it the right answer
            if (word.isRight(messageText)) {
                msg.setRight(true);
                winners.add(DBAuth.getUserName());
                isWin = true;
            }
            //add to firebase
            dbMessage.addMessage(msg, uidRef);
        }
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
        //start timer for "timeRound" seconds
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
        //start a listener for change of the word
        wordListener=db.collection("GameRooms").document(uidRef).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override

            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    GameRoom gm = value.toObject(GameRoom.class);
                    word.setWord(gm.getWord());
                    TextView textView = findViewById(R.id.word);
                    textView.setText(word.Clue());
                    //when the player get the word he start listen for the draw
                    listenForDraws(uidRef);
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
        //if someone win this round
        if (winners!=null) {
            //go over all the winners list and give them point from first to last winner
            for (int i = 0; i < winners.size(); i++) {
                //if
                if (winners.get(i).equals(DBAuth.getUserName())) {
                    //update locally the points
                    gameroom.updatePoint(points, winners.get(i));
                    //add points for general scoring
                    dbUser.setPoint(points);
                }
                points -= 25;
            }
            //if it's the artist
            if (DBAuth.getUserName().equals(gameroom.getNames().get(counter))) {
                //if most of the players guess the word= good painting= extra points
                if (winners.size() > gameroom.getCounterOfPlayers() / 2 || winners == null) {
                    gameroom.updatePoint(125, gameroom.getNames().get(counter));
                    dbUser.setPoint(150);
                } else {
                    gameroom.updatePoint(50, gameroom.getNames().get(counter));
                    dbUser.setPoint(50);
                }
            }
            dbGameRoom.addGameRoom(gameroom,uidRef);
        }
        else
        {
            //if no one guess the word=> everyone get zero points
           for (int i=0;i<gameroom.getCounterOfPlayers();i++)
               gameroom.updatePoint(0,gameroom.getNames().get(i));
           //except the artist, he get 50 points
            if(DBAuth.getUserName().equals(gameroom.getNames().get(counter))) {
                gameroom.updatePoint(50, gameroom.getNames().get(counter));
                dbUser.setPoint(50);
            }
        }
    }
    public void SetDialog()
            // set dialog of end round
    {
        dbGameRoom.updateStartRound(uidRef,false);
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.end_round_dialog);
        TextView txWord=dialog.findViewById(R.id.theWordIs);
        txWord.setText(word.getWord());
        TextView txWinner=dialog.findViewById(R.id.winner);
        TextView txPoints=dialog.findViewById(R.id.points);
        Button b=dialog.findViewById(R.id.startRound);
        //this button oly visible for the host- start new round button
        if (DBAuth.getUserUID().equals(uidRef)) {
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //if the round start update everyone
                    dbGameRoom.updateStartRound(uidRef, true);
                    //close the dialog and start new turn
                    dialog.dismiss();
                    startTurn();
                }
            });
        }
        else
        //if we aren't the host, we don't see the button and start listen for the status of the game
        {
            b.setVisibility(View.INVISIBLE);
            listenForStartRound();
        }
        //if no one win
        if (winners!=null){
            txWinner.setText(gameroom.getNames().get(counter));
            txPoints.setText("+125");
        }
        else {
            txWinner.setText(winners.get(0));
            txPoints.setText("+"+(175-25*winners.size()));
        }
        dialog.setCancelable(false);
        dialog.show();
        winners.clear();
    }

    public void listenForStartRound()
    {
        //listen if the round start so he can start the fuction "startTurn"
        startListener=db.collection("GameRooms").document(uidRef).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d("listenForStartRound", "onEvent: ");
                if (value.exists())
                {
                    GameRoom gameRoom=value.toObject(GameRoom.class);
                    if (gameRoom.getIsRoundStart()) {
                        dialog.dismiss();
                        dbGameRoom.updateStartRound(uidRef,false);
                        startTurn();
                    }
                }
            }
        });
    }

    public void SetEndGameDialog()
    {
        //when the game was finish show the dialog
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.end_game_dialog);
        dialog.setCancelable(false);
        gameroom.orderListByPoints();
        ArrayList<String> profiles=gameroom.getNames();
        TextView t1=dialog.findViewById(R.id.firstPlace);
        TextView t2=dialog.findViewById(R.id.secondPlace);
        TextView t3=dialog.findViewById(R.id.thirdPlace);
        Button stopPlayButton=dialog.findViewById(R.id.stopPlayButton);
        Button keepPlayButton=dialog.findViewById(R.id.keepPlayButton);
        //listener for the button of stop playing (move to GameLobby)
        stopPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameLobby.class);
                startActivity(intent);
            }
        });
        //listener for the button of keep playing on the dialog
        keepPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uidRef.equals(DBAuth.getUserUID()))
                {
                    //if you are the host create the room again (go to CreateNewRoomPage)
                    Intent intent=new Intent(MainActivity.this,CreateNewRoomPage.class);
                    intent.putExtra("numPlayers",gameroom.getPlayerNum());
                    intent.putExtra("numRounds",gameroom.getRoundNum());
                    intent.putExtra("timeRounds",gameroom.getRoundTime());
                    //1-yes,2-no
                    intent.putExtra("openAgain",1);
                    startActivity(intent);
                }
                else
                {
                    //if you aren't the host, try to connect the room again (move to game lobby)
                    Intent intent=new Intent(MainActivity.this,GameLobby.class);
                    //1-yes,2-no
                    intent.putExtra("playAgain",1);
                    intent.putExtra("uidRef",uidRef);
                    //intent.putExtra("isHost",2);
                    startActivity(intent);
                }
            }
        });
        t1.setText(profiles.get(0));
        if (profiles.size()>=2)
        {
            t2.setVisibility(View.VISIBLE);
            t2.setText(profiles.get(1));
            if (profiles.size()>=3)
            {
                t3.setVisibility(View.VISIBLE);
                t3.setText(profiles.get(2));
            }
        }
        dialog.show();
    }

    @Override
    public void onUserComplete(boolean s) {
    }
    //if you have no internet
    @Override
    public void NetworkNotWorking() {
        internetDialog.show();
    }
    //if you have internet
    @Override
    public void NetworkIsWorking() {
        internetDialog.dismiss();
    }
}