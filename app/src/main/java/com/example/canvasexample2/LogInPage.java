package com.example.canvasexample2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogInPage extends AppCompatActivity implements DBAuth.AuthComplete, DBUser.AddUserComplete {

    private DBAuth mAuth= new DBAuth(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void LogIn(View view) {
        TextView emailTv=findViewById(R.id.emailLogIn);
        String email=emailTv.getText().toString();
        TextView passwordTv=findViewById(R.id.passwordLogIn);
        String password=passwordTv.getText().toString();
        mAuth.LogIn(email,password);
    }

    @Override
    public void onComplete(boolean s) {

    }

    @Override
    public void onUserComplete(boolean s) {

    }
}