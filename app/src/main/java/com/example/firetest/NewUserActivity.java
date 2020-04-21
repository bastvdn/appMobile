package com.example.firetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firetest.classes_layout.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class NewUserActivity extends AppCompatActivity {

    DatabaseReference reff;
    private FirebaseAuth mAuth;

    private Button boutonConfirm;
    private EditText email;
    private EditText passWord;
    private EditText passWordVerif;

    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"U Signed In successfully",Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(this,"U Didnt signed in",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        boutonConfirm = findViewById(R.id.boutonConfirm);
        email = findViewById(R.id.newEmail);
        passWord = findViewById(R.id.newPassword);
        passWordVerif = findViewById(R.id.passwordVerif);
        boutonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email1 = email.getText().toString();
                String pass = passWord.getText().toString();
                String passVerif = passWordVerif.getText().toString();
                Log.d("inscri",email1+pass+passVerif);
                mAuth = FirebaseAuth.getInstance();


                if (email1.equals("") || pass.equals("") || passVerif.equals("")){
                    Toast.makeText(NewUserActivity.this,"Un des champs renseigné est vide",Toast.LENGTH_SHORT).show();

                }
                else {
                    if(passWord.getText().toString().equals(passWordVerif.getText().toString())){
                        if(email1.split("\\@")[0].equals("admin")){
                            Toast.makeText(NewUserActivity.this, "admin non admis", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mAuth.createUserWithEmailAndPassword(email1, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("inscro", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        DatabaseReference reff;

                                        String test[] = user.getEmail().split("\\@");
                                        String username = test[0];
                                        User newUser = new User();
                                        newUser.setName(username);
                                        ArrayList<String> arrayy = new ArrayList<String>();
                                        arrayy.add("all");
                                        newUser.setCours(arrayy);
                                        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(username);
                                        reff.setValue(newUser);

                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("inscri", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(NewUserActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }



                    }
                    else{
                        Toast.makeText(NewUserActivity.this,"Les deux mots de passe ne sont pas identiques",Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });


    }
}
