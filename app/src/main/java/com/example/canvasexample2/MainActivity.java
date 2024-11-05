package com.example.canvasexample2;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorKt;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private MyCanvasView myCanvasView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
      //  myCanvasView=new MyCanvasView(this);
        myCanvasView = findViewById(R.id.canvas01);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void changeBrushColor(View view) {

        String color = view.getTag().toString();
        int brushColor = Color.parseColor(color);
        myCanvasView.changeBrushColor(brushColor);
    }

    public void eraser(View view) {
        myCanvasView.eraser();
    }

    public void delete(View view) {
        myCanvasView.delete();
    }
}