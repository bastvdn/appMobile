package com.example.firetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference reff;
    private FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;

    private Button button;
    private Button boutonDebug;
    private Button boutonInscr;

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
        button = (Button) findViewById(R.id.button);


        boutonInscr = findViewById(R.id.buttonInscr);


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    if (firebaseAuth.getCurrentUser().getEmail().equals("admin@admin.com")){
                        Intent newAct = new Intent(getApplicationContext(), AdminPage.class);
                        startActivity(newAct);
                        finish();
                    }
                    else {
                        Intent newAct = new Intent(getApplicationContext(), UserPage.class);
                        startActivity(newAct);
                        finish();
                    }
                }
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startSignIn();

            }
        });

        boutonInscr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAct = new Intent(getApplicationContext(), NewUserActivity.class);
                startActivity(newAct);


            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn(){
        final String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if (!email.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "mauvais email ou mot de passe", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d("conaa", emailField.getText().toString());
                        if (emailField.getText().toString().equals("admin@admin.com")) {
                            Intent newAct = new Intent(getApplicationContext(), AdminPage.class);
                            startActivity(newAct);
                            finish();
                        } else {
                            Intent newAct = new Intent(getApplicationContext(), UserPage.class);
                            startActivity(newAct);
                            finish();
                        }
                    }
                }
            });

        }
        else{
            Toast.makeText(MainActivity.this, "Champ vide", Toast.LENGTH_LONG).show();

        }
    }
}
