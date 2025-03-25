package com.example.Shcrible;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.security.PublicKey;

public class DBGameRoom {


    public interface GameRoomComplete
    {
        void onGameRoomComplete(boolean s);
    }
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DBGameRoom.GameRoomComplete addGameRoomComplete;
    public DBGameRoom(DBGameRoom.GameRoomComplete a)
    {
        this.addGameRoomComplete = a;
    }
    public DBGameRoom(){
    }
    public void addGameRoom(GameRoom gm,String uID)
    {
        db.collection("GameRooms").document(uID).set(gm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addGameRoomComplete.onGameRoomComplete(task.isSuccessful());
            }
        });
    }
    public void UpdatePoints(String uidRef,int point)
    {
        db.collection("profiles").document(uidRef).update("points",point).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    addGameRoomComplete.onGameRoomComplete(task.isSuccessful());
            }
        });
    }

    public void updateWord(String uidRef, String word) {

        db.collection("GameRooms").document(uidRef).update("word",word).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("DBGameRoom", "word update : " + task.isSuccessful());
            }
        });
    }
    public void updateStartRound(String uidRef,boolean isStartRound)
    {
        db.collection("GameRooms").document(uidRef).update("isRoundStart",isStartRound).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("updateStartRound", "is the round start"+isStartRound);
            }
        });
    }


}
