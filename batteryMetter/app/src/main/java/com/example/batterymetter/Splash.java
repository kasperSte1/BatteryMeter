package com.example.batterymetter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    FirebaseAuth Out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Out = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = Out.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(Splash.this,MainActivity.class));
        }
        else
        {
            startActivity(new Intent(Splash.this,Login.class));
        }

    }


}