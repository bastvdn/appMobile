package com.example.firetest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.firetest.classes_layout.Avis;
import com.example.firetest.classes_layout.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserPage extends AppCompatActivity {
    private Button boutonOff;
    private Button bouton_inscr;
    private Button bouton_avis;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private FirebaseUser user;
    private String username;
    private ArrayList<String> userCours;
    private User currentUser;
    private FirebaseAuth firebaseAuth;

    private void notif(Avis v){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            ;
            NotificationChannel channel = new NotificationChannel("n","n", v.getImportance()+2);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        int red;

        if (v.getImportance()==2){
            red = R.drawable.importance_2;
        }
        else{
            red = R.drawable.importance_1;
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"n")
                .setContentText("SchoolInfo")
                .setSmallIcon(red)
                .setAutoCancel(true)
                .setContentText(v.getContent());

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999,builder.build());


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        this.bouton_inscr = (Button) findViewById(R.id.classInscrBoutton);
        bouton_inscr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAct = new Intent(getApplicationContext(), ClassInscr.class);
                startActivity(newAct);


            }
        });

        this.bouton_avis = (Button) findViewById(R.id.avisBoutton);
        bouton_avis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("state","launch");
                Intent newAct = new Intent(getApplicationContext(), AvisActivityList.class);
                startActivity(newAct);


            }
        });
        boutonOff = findViewById(R.id.decoBoutton);
        boutonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent newAct = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(newAct);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            Log.d("ok",user.getEmail());
            String test[] = user.getEmail().split("\\@");
            this.username = test[0];
        }

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    User user = dataSnapshot1.getValue(User.class);
                    if (user.getName().equals(username)){
                        currentUser = user;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference("Avis");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Avis v = dataSnapshot.getValue(Avis.class);
                if (currentUser!=null){
                    for(String n : currentUser.getCours()){
                        if (v.getTitre().equals(n)){

                            notif(v);

                        }
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("ok",dataSnapshot.getValue().toString());
                Avis v = dataSnapshot.getValue(Avis.class);
                if (currentUser!=null) {
                    for (String n : currentUser.getCours()) {
                        if (v.getTitre().equals(n)) {

                            notif(v);

                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
