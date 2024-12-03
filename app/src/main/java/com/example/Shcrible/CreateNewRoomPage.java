package com.example.Shcrible;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateNewRoomPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
     private Intent intent;
    Spinner spinnerPlayer,spinnerRounds,spinnerTime;

    private int numPlayers=2,numRounds=1,time=30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_new_room_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        initUI();



    }

    private void initUI() {
        //spinners have information for the game room
        spinnerPlayer=findViewById(R.id.numOfPlayers);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.numberOfPlayer,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayer.setAdapter(adapter);
        spinnerRounds=findViewById(R.id.numberOfRounds);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.numberOfRounds,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRounds.setAdapter(adapter2);
        spinnerTime=findViewById(R.id.timeForRound);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.timeForRound
                ,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapter3);

        spinnerTime.setOnItemSelectedListener(this);
        spinnerRounds.setOnItemSelectedListener(this);
        spinnerPlayer.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        //when someone select option on one of the spinner. i put the value on the correct variable

        if(!(view instanceof Spinner))
        {
            return;
        }
        String answer= parent.getItemAtPosition(pos).toString();
        String name = view.getTag().toString();

        int result = Integer.valueOf(answer);



        if(name.equals(spinnerRounds.getTag()))
        {
            numRounds = result;

        }
        else if (name.equals(spinnerTime.getTag()))
            time = result;
        else
            numPlayers = result;



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //if nothing selected
    }

    public void startNextActivity(View view) {
        //when the user finish to choose we move to the next activity=> WaitingRoom
        Intent takeDetails = getIntent();
        intent=new Intent(CreateNewRoomPage.this,WaitingRoom.class);
        intent.putExtra("numPlayers",numPlayers);
        intent.putExtra("numRounds",numRounds);
        intent.putExtra("timeRounds",time);
        intent.putExtra("isHost",1);
        startActivity(intent);
        finish();

    }
}