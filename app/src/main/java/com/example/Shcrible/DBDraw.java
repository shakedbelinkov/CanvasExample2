package com.example.Shcrible;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

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


    public void addDraw(Draw draw,String uidRef)
    {
        db.collection("Draw").document(uidRef).set(draw).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addDrawComplete.onDrawComplete(task.isSuccessful());
            }
        });
    }
}
