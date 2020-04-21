package com.example.firetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.firetest.adapteurs.Avis_layout_adapter;
import com.example.firetest.adapteurs.Cours_layout_adapter;
import com.example.firetest.classes_layout.Avis;
import com.example.firetest.classes_layout.Cours_layout;
import com.example.firetest.classes_layout.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

public class AvisActivityList extends AppCompatActivity implements Avis_layout_adapter.OnNoteListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabaseAvis;
    private DatabaseReference mDatabaseUser;
    private ArrayList<Avis> liste_avis;
    private FirebaseUser user;
    private String username;
    private ArrayList<String> userCours;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avis_list);

        Log.d("state","starte");
        liste_avis = new ArrayList<Avis>();

        mRecyclerView = findViewById(R.id.avis_liste);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        userCours = new ArrayList<String>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            Log.d("ok",user.getEmail());
            String test[] = user.getEmail().split("\\@");
            this.username = test[0];
        }

        userCours.add("all");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    User user = dataSnapshot1.getValue(User.class);
                    if (user.getName().equals(username)){
                        for (String n : user.getCours()) {
                            userCours.add(n);
                            Log.d("last", userCours.toString());
                        }
                    }
                }
                mDatabaseAvis = FirebaseDatabase.getInstance().getReference().child("Avis");
                mDatabaseAvis.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            Avis avis = dataSnapshot1.getValue(Avis.class);
                            if (username.equals("admin")){
                                Boolean flagAvis = false;
                                for (Avis a : liste_avis){
                                    if (a.getId()==avis.getId()){
                                        flagAvis = true;
                                    }
                                }
                                if (!flagAvis){liste_avis.add(avis);}
                            }
                            else {
                                if (userCours.contains(avis.getTitre())) {
                                    Log.d("avis", avis.getContent());
                                    Boolean flagAvis = false;
                                    for (Avis a : liste_avis) {
                                        if (a.getId()==avis.getId()) {
                                            flagAvis = true;
                                        }
                                    }
                                    Log.d("last",avis.getContent().toString());
                                    if (!flagAvis) {
                                        liste_avis.add(avis);

                                    }
                                }
                            }

                        }
                        mAdapter = new Avis_layout_adapter(liste_avis, AvisActivityList.this);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void suppAvis(Avis v){
        final DatabaseReference tDatabase;
        tDatabase = FirebaseDatabase.getInstance().getReference().child("Avis").child(Integer.toString(v.getId()));
        final Avis vv = v;
        tDatabase.removeValue();
//        tDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    @Override
    public void onNoteClick(int position) {
        if (username.equals("admin")) {

            final Avis v = liste_avis.get(position);

            final CustomPopup customPopup = new CustomPopup(AvisActivityList.this);

            customPopup.setTitle("Etes vous sur de vouloir supprimer cet avis?");
            customPopup.setSubTitle("Pour : "+ v.getTitre() + " - Importance : " + Integer.toString(v.getImportance()+1) + "/3");
            customPopup.getYesButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    suppAvis(v);

                    customPopup.dismiss();
                    finish();
                }
            });
            customPopup.getNoButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customPopup.dismiss();
                }
            });
            customPopup.build();


        }
    }
}