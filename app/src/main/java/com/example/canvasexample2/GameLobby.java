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
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_lobby);
        Intent takeDetails = getIntent();
        String name = takeDetails.getStringExtra("name");
        textView=findViewById(R.id.playerName);
        textView.setText("Hi "+name);
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

    public void createDialog(View view) {
        d= new Dialog(this);
        d.setContentView(R.layout.dialoglogin);
        d.setTitle("Login");
        d.setCancelable(true);
        gameRoomCode=(EditText)d.findViewById(R.id.gameRoomCode);
        btnCustomLogin=(Button)d.findViewById(R.id.loginButton);
        btnCustomLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRoomCode=findViewById(R.id.gameRoomCode);
                code=gameRoomCode.getText().toString();
                d.dismiss();
            }
        });
        d.show();
    }
}