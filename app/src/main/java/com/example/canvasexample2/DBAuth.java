package com.example.canvasexample2;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DBAuth {
    public interface AuthComplete
    {
        void onComplete(boolean s);
    }
    private AuthComplete authComplete;
    private FirebaseAuth mAuth;

    public DBAuth(AuthComplete authComplete)
    {
        mAuth = FirebaseAuth.getInstance();
        this.authComplete=authComplete;
    }
    public DBAuth()
    {
        mAuth = FirebaseAuth.getInstance();
    }
    public void AddUser(String email,String password)
    {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                authComplete.onComplete(task.isSuccessful());
            }
        });
    }
}
