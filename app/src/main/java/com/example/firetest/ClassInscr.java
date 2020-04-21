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

import com.example.firetest.adapteurs.Cours_layout_adapter;
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
import java.util.List;

public class ClassInscr extends AppCompatActivity implements Cours_layout_adapter.OnNoteListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabaseCours;
    private DatabaseReference mDatabaseUser;
    private ArrayList<Cours_layout> list_cours;
    private FirebaseUser user;
    private String username;
    private ArrayList<String> userCours;
    private Button button;


    private void unSubCours(Cours_layout c){
        final DatabaseReference tDatabase;
        tDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(username).child("cours");
        final Cours_layout cc = c;
        tDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean flagInscr = true;
                int cle = 0;
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    if (d.getValue().equals(cc.getName())){
                        tDatabase.child(d.getKey()).removeValue();
                        flagInscr = false;
                    }
                    cle = Integer.parseInt(d.getKey())+1;
                }
                if(flagInscr){
                    tDatabase.child(Integer.toString(cle)).setValue(cc.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        if (c.getInscr()){
//            tDatabase.child("2").removeValue();
//            userCours.remove(c);
//        }
//        else{
//            tDatabase.child("5").setValue(c.getName());
//        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_inscr);
        list_cours = new ArrayList<Cours_layout>();

        mRecyclerView = findViewById(R.id.cours_liste);
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

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    User user1 = dataSnapshot1.getValue(User.class);
                    if (user1.getName().equals(username)){
                        if(!user1.getCours().equals(null)) {
                            for (String n : user1.getCours()) {
                                userCours.add(n);
                            }
                        }
                    }
                }
                mDatabaseCours = FirebaseDatabase.getInstance().getReference().child("Cours");
                mDatabaseCours.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            Cours_layout cours = dataSnapshot1.getValue(Cours_layout.class);

                            if (userCours.contains(cours.getName())){

                                cours.setInscr(true);
                            }
                            Boolean flagCours = false;
                            for (Cours_layout c : list_cours){
                                if (c.getName().equals(cours.getName())){
                                    flagCours = true;

                                }

                            }
                            if(!flagCours){list_cours.add(cours);}
                        }
                        mAdapter = new Cours_layout_adapter(list_cours, ClassInscr.this);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ClassInscr.this,"ooops something's wrong", Toast.LENGTH_LONG);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onNoteClick(int position) {
        Log.d("listener",list_cours.get(position).getName());
        final CustomPopup customPopup = new CustomPopup(ClassInscr.this);
        final Cours_layout c = list_cours.get(position);
        if (c.getInscr()){
            customPopup.setTitle("Etes vous sur de vouloir vous désinscrire des avis de cette classe?");
        }
        else{
            customPopup.setTitle("Etes vous sur de vouloir vous inscrire aux avis de cette classe?");
        }

        customPopup.setSubTitle("classe : " + c.getName() +" - Prof : "+ c.getProf());

        customPopup.getYesButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unSubCours(c);
                customPopup.dismiss();
                finish();
            }
        });
        customPopup.getNoButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"désinscription annulée",Toast.LENGTH_SHORT).show();
                customPopup.dismiss();
            }
        });
        customPopup.build();
    }
}
