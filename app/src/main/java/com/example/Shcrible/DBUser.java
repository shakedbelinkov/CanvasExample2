package com.example.Shcrible;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class DBUser {
    public interface AddUserComplete
    {
        void onUserComplete(boolean s);
    }
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AddUserComplete addUserComplete;
    public DBUser(AddUserComplete a)
    {
        this.addUserComplete = a;
    }


    public void addProfile(Profile p)
    {
        db.collection("profiles").document(p.getuID()).set(p).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               addUserComplete.onUserComplete(task.isSuccessful());
            }
        });
    }
    public void setPoint(int point)
    {
        db.collection("profiles").document(DBAuth.getUserUID()).update("points", FieldValue.increment(point)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addUserComplete.onUserComplete(task.isSuccessful());
            }
        });
    }

}
