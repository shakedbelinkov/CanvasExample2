package com.example.Shcrible;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class DBMassage {
    public interface AddMassageComplete
    {
        void onMassageComplete(boolean s);
    }
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DBMassage.AddMassageComplete addMassageComplete;
    public DBMassage(DBMassage.AddMassageComplete a)
    {
        this.addMassageComplete = a;
    }



    public void addMassage(String msg, String uidRef)
    //add draw to collection on firebase
    {
        db.collection("GameRooms").document(uidRef).collection("Massage").add(msg).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                addMassageComplete.onMassageComplete(task.isSuccessful());
            }
        });
    }
}
