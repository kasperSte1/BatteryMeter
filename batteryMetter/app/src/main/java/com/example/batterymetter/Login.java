package com.example.batterymetter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Login extends AppCompatActivity {

    EditText Email,Password;
    FirebaseAuth mAuth;
    Button LoginBtn,Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginBtn = findViewById(R.id.loginButton);
        Register = findViewById(R.id.registerPageButton);
        Email = findViewById(R.id.loginEmailEditText);
        Password = findViewById(R.id.loginPasswordEditText);
        mAuth = FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });
        LoginBtn.setOnClickListener((View v)->{
            String mail = Email.getText().toString();
            String pass = Password.getText().toString();


            if(TextUtils.isEmpty(mail)){
                Toast.makeText(Login.this,"Niewpisany adres email",Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(pass)){
                Toast.makeText(Login.this,"Nie wpisałeś hasła!",Toast.LENGTH_SHORT).show();
                return;
            }



            mAuth.signInWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(Login.this, "Zalogowałeś sie!",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this,MainActivity.class));



                            } else {

                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(Login.this, "Nie ma konta o podanym mailu lub błędne hasło!", Toast.LENGTH_SHORT).show();

                                }

                            }
                        }
                    });
        });
    }
}