package com.example.Shcrible;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoard extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Player> players;
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
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    List<Profile> profiles=task.getResult().toObjects(Profile.class);
                    int size=10;
                    if (profiles.size()<10)
                        size=profiles.size();
                    for (int i=0;i<=size;i++)
                    {
                        Player player=new Player(profiles.get(i).getName(),profiles.get(i).getPoints(),i);
                        players.add(player);
                    }
                }
            }
        });
        listView = findViewById(R.id.leaderboardList);
        playerAdapter=new PlayerAdapter(this,0,0,players);
        listView.setAdapter(playerAdapter);
    }
}