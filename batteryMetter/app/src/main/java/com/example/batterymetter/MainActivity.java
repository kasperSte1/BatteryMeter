package com.example.batterymetter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button logout;
    private float testValue;



    Button Pomiar, PomiarBazowy, historyButton, tutorialButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout = findViewById(R.id.logoutButton);
        Pomiar=findViewById(R.id.Pomiar);
        PomiarBazowy=findViewById(R.id.PomiarBazowy);
        historyButton = findViewById(R.id.historyButton);
        tutorialButton=findViewById(R.id.tutorialButton);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("BaselineMeas", Context.MODE_PRIVATE);
        testValue = sp.getFloat("Baseline",0.0f);
        PomiarBazowy.setOnClickListener((View v)->{
            Intent intent = new Intent(MainActivity.this, baselineMeas.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        Pomiar.setOnClickListener((View v)->{
                if (testValue != 0.0f ){
                Intent intent = new Intent(MainActivity.this, ApplicationList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);}
                else {
                    Toast.makeText(MainActivity.this, "Wykonaj najpierw pomiar bazowy!", Toast.LENGTH_SHORT).show();
                }

        });
        logout.setOnClickListener((View v)->{
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        tutorialButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Tutorial.class);
            startActivity(intent);
        });


    }

}