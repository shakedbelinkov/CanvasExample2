package com.example.Shcrible;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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


    public void addDraw(ArrayList<Draw> draws, String uidRef)
    {
        db.collection("GameRoom").document(uidRef).collection("Draw").add(draws).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                addDrawComplete.onDrawComplete(task.isSuccessful());
            }
        });
    }
}
