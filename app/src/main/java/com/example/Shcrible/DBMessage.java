package com.example.Shcrible;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DBMessage {
    public interface AddMessageComplete
    {
        void onMessageComplete(boolean s);
    }
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DBMessage.AddMessageComplete addMessageComplete;
    public DBMessage(DBMessage.AddMessageComplete a)
    {
        this.addMessageComplete = a;
    }



    public void addMessage(Message msg, String uidRef)
    //add draw to collection on firebase
    {
        db.collection("GameRooms").document(uidRef).collection("Message").add(msg).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                addMessageComplete.onMessageComplete(task.isSuccessful());
            }
        });
    }
}
