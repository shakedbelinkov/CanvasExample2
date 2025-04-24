package com.example.Shcrible;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DBUser {
    public interface AddUserComplete
    {
        void onUserComplete(boolean s);
    }
    public interface LeaderBoardComplete
    {
        void onLeaderBoardComplete(List<Profile> arr);
    }


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AddUserComplete addUserComplete;
    private LeaderBoardComplete leaderListener;

    public DBUser(AddUserComplete a)
    {
        this.addUserComplete = a;
    }
    public DBUser(LeaderBoardComplete ldc)
    {
        this.leaderListener = ldc;

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



    // get top ten users, and show my location
    public void getUserLeaderBoard()
    {
        db.collection("profiles").orderBy("points", Query.Direction.DESCENDING).limit(10).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<Profile> arr = queryDocumentSnapshots.toObjects(Profile.class);
                leaderListener.onLeaderBoardComplete(arr);
            }
        });


    }

}
