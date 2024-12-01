package com.example.canvasexample2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class OpeningPage extends AppCompatActivity implements DBAuth.AuthComplete, DBUser.AddUserComplete {

    private DBAuth mauth;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_opening_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.startButton), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mauth = new DBAuth(this); // pass the activity reference to the class

        if(mauth.isUserSigned())
        {
            // move to Main activity>>
            Intent intent=new Intent(OpeningPage.this, GameLobby.class);
            intent.putExtra("name",username);
            startActivity(intent);
        }

    }

    public void SignUp(View view) {
        //get the information of the user
        TextView emailTv=findViewById(R.id.email);
        TextView passwordTv=findViewById(R.id.password);
        String email=emailTv.getText().toString();
        String password=passwordTv.getText().toString();
        TextView usernameTv=findViewById(R.id.username);

        username=usernameTv.getText().toString();
        mauth.AddUser(email,password,username);
    }

    @Override
    public void onComplete(boolean s) {

      if(s) // this means we authenticated successfully
      {

          // update FirebaseUser with name



          // set email, points, name


          Profile p = new Profile();
          p.setPoints(Consts.INITIAL_POINTS);
          p.setEmail(mauth.getUserEmail());
          p.setName(username);
          p.setuID(mauth.getUID());

          DBUser  dbUser = new DBUser(this);

          dbUser.addProfile(p);
          // call DBUsers to add this profile to the DB

      }

    }
    @Override
    public void onUserComplete(boolean s) {
        Toast.makeText(this,"profile " + s,Toast.LENGTH_LONG).show();

        if(s)
        {
            Intent intent=new Intent(OpeningPage.this, GameLobby.class);
            startActivity(intent);
        }
    }

    public void LogIn(View view) {
        Intent intent=new Intent(OpeningPage.this, LogInPage.class);
        startActivity(intent);
    }
}