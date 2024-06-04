package com.example.batterymetter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    EditText Password,ConfirmPassword,Email;
    FirebaseAuth mAuth;
    Button Goback,RegisterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Password = findViewById(R.id.passwordEditText);
        ConfirmPassword = findViewById(R.id.confirmPasswordEditText);
        Email = findViewById(R.id.emailEditText);
        Goback = findViewById(R.id.loginPageButton);
        RegisterBtn = findViewById(R.id.registerButton);
        mAuth = FirebaseAuth.getInstance();

        Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail,pass,cpass;
                mail=Email.getText().toString();
                pass=Password.getText().toString();
                cpass=ConfirmPassword.getText().toString();
                if (TextUtils.isEmpty(mail) || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    Toast.makeText(Register.this,"Niepoprawny adres e-mail",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass) || !JavaUtils.Password_Validation(pass)){
                    Toast.makeText(Register.this,"Hasło zbyt krótke bądź nie zawiera specjalnych znaków",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pass.equals(cpass)){
                    Toast.makeText(Register.this,"Hasło podane ponownie nie pasuje do oryginału",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(mail, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Konto utworzone!",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Register.this,Login.class));
                                } else {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(Register.this, "Konto z takim mailem już istnieje!", Toast.LENGTH_SHORT).show();

                                    }

                                    else {
                                        Toast.makeText(Register.this, "Niestety nie udało się utworzyć konta :(",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
    }

}