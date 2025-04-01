package com.example.Shcrible;

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

public class LeaderBoard extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<Player> player;
    private PlayerAdapter playerAdapter;
    private CollectionReference playerRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leader_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void initUI()
    {
        playerRef=db.collection("profiles");
        Query query= playerRef.orderBy("points", Query.Direction.ASCENDING);
        listView = findViewById(R.id.leaderboardList);
        //listView.setHasFixedSize(true);
        //listView.setLayoutManager(new LinearLayoutManager(this));
        FirestoreRecyclerOptions<Player> options = new FirestoreRecyclerOptions.Builder<Player>()
                .setQuery(query,Player.class)
                .build();
        playerAdapter = new PlayerAdapter(options);
        listView.setAdapter(playerAdapter);
    }
}