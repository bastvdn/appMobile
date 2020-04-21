package com.example.firetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminPage extends AppCompatActivity {

    private Button boutonOff;
    private Button boutonList;
    private Button bouton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        bouton = (Button) findViewById(R.id.buttonAvisPage);

        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAct = new Intent(getApplicationContext(), NewAvis.class);
                startActivity(newAct);


            }
        });

        boutonList = findViewById(R.id.buttonAvisList);
        boutonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAct = new Intent(getApplicationContext(), AvisActivityList.class);
                startActivity(newAct);
            }
        });

        boutonOff = findViewById(R.id.decoBouttonAdm);
        boutonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent newAct = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(newAct);
            }
        });
    }
}
