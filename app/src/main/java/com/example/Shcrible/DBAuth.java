package com.example.Shcrible;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class DBAuth {
    public interface AuthComplete
    {
        void onComplete(boolean s);
    }
    private AuthComplete authComplete;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public static String getUserUID()
    {
        return  FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public DBAuth(AuthComplete authComplete)
    {
        this.authComplete=authComplete;
    }
    public DBAuth()
    {
    }

    public boolean isUserSigned()
    {
        return mAuth.getCurrentUser()!=null;
    }

    public static String getUserName()
    {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }


    public void updateUserName(String userName)
    {
        UserProfileChangeRequest request  = new UserProfileChangeRequest.Builder().
        setDisplayName(userName).
        build();

        FirebaseAuth.getInstance().getCurrentUser().updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                  authComplete.onComplete(task.isSuccessful());

            }
        });


    }


    public void AddUser(String email,String password,String userName)
    {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                    updateUserName(userName);

            }
        });
    }
    public void LogIn(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                    authComplete.onComplete(task.isSuccessful());
            }
        });
    }

    public String getUserEmail()
    {
        if(this.isUserSigned())
        {
            return mAuth.getCurrentUser().getEmail();
        }
        return "";
    }


    public String getUID()
    {
        return mAuth.getUid();
    }
}
