package com.example.Shcrible;

import android.app.Dialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoard extends AppCompatActivity implements DBUser.LeaderBoardComplete,networkReceiver.checkNetworkComplete {
    private ListView listView;
    private ArrayAdapter<Player> player;
    private PlayerAdapter playerAdapter;
    private CollectionReference playerRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DBUser dbUser;
    private networkReceiver networkReceiver;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leader_board);
        //create object of networkReceiver and create new dialog
        networkReceiver = new networkReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
        dialog= new Dialog(this);
        dialog.setContentView(R.layout.internet_dialog);
        dialog.setCancelable(false);
        initUI();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void initUI()
    {
        dbUser=new DBUser(this);
        //get the top ten players
        dbUser.getUserLeaderBoard();
    }

    @Override
    public void onLeaderBoardComplete(List<Profile> arr) {
        //when i have the top ten player i show them on the list view
        listView = findViewById(R.id.leaderboardList);
        ArrayList<Player> players=new ArrayList<>();
        //add every player to the list
        for (int i=0;i<arr.size();i++)
        {
            //get the information and create object of Player
            Player p=new Player(arr.get(i).getName(),arr.get(i).getPoints(),i+1);
            players.add(p);
        }
        playerAdapter = new PlayerAdapter(this,0,0,players);
        listView.setAdapter(playerAdapter);
    }
    //if you have no internet
    @Override
    public void NetworkNotWorking() {
        dialog.show();
    }
    //if you have internet
    @Override
    public void NetworkIsWorking() {
        dialog.dismiss();
    }
}