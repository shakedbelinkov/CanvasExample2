package com.example.canvasexample2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameLobby extends AppCompatActivity {

    private TextView textView;
    private Dialog d;
    private EditText gameRoomCode;
    private Button btnCustomLogin;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_lobby);
        Intent takeDetails = getIntent();
        String name = takeDetails.getStringExtra("name");
        textView=findViewById(R.id.playerName);
        textView.setText("Hi "+name);
        loginButton=findViewById(R.id.LoginButton);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void createNewRoom(View view) {
        Intent intent=new Intent(GameLobby.this, CreateNewRoomPage.class);
        startActivity(intent);
    }
}