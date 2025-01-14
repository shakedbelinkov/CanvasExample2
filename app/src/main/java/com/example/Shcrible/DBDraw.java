package com.example.Shcrible;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class DBDraw {
    public interface AddDrawComplete
    {
        void onDrawComplete(boolean s);
    }
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DBDraw.AddDrawComplete addDrawComplete;
    public DBDraw(DBDraw.AddDrawComplete a)
    {
        this.addDrawComplete = a;
    }



    private  TreeMap<String,HashMap<String,Object>> arrayToMap(ArrayList<Draw> draws)
    {

        TreeMap<String,HashMap<String,Object>> drawsAsMap = new TreeMap<>();
        for (int i = 0; i < draws.size(); i++) {
            // pass through arraylist
            // for each draw in the arraylist create a dictionary - hashmap
            // add this hashmap to "big" hashmap
            drawsAsMap.put(""+i,draws.get(i).drawToHashmap());
        }
        return drawsAsMap;
    }

    public void addDraw(ArrayList<Draw> draws, String uidRef)
            //add draw to collection on firebase
    {
        db.collection("GameRooms").document(uidRef).collection("Drawss").add(arrayToMap(draws)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                addDrawComplete.onDrawComplete(task.isSuccessful());
            }
        });
    }
}
