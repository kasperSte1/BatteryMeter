package com.example.batterymetter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



public class Tutorial extends AppCompatActivity {

    Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        menu = findViewById(R.id.MenuButton);
        menu.setOnClickListener(v -> {
            Intent intent = new Intent(Tutorial.this, MainActivity.class);
            startActivity(intent);
        });
    }


}
