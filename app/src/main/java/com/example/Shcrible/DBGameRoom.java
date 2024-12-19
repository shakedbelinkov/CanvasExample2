package com.example.Shcrible;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

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



}
