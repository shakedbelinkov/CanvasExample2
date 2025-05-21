package com.example.Shcrible;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
            //take arr and turn it to hashMap
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

    public void addDraw(ArrayList<Draw> draws)
            //add draw to collection on firebase
    {



        db.collection("GameRooms").document(MainActivity.uidRef).collection("Draw").document(MainActivity.uidRef).set(arrayToMap(draws)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addDrawComplete.onDrawComplete(task.isSuccessful());

            }
        });
    }
    public void removeDraw()
    // remove draw from collection on firebase
    {
        db.collection("GameRooms").document(MainActivity.uidRef).collection("Draw").document(MainActivity.uidRef).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addDrawComplete.onDrawComplete(task.isSuccessful());

            }
        });
    }

}
