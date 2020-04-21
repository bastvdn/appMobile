package com.example.firetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatCallback;
import androidx.core.app.NotificationCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.firetest.classes_layout.Avis;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


public class NewAvis extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String CHANNEL_ID = "tamer";
    private Spinner impSpinner;
    private Spinner classSpiner;
    private EditText contentText;
    private Button buttonConfirm;
    private Avis avis;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private String KEYS;
    private int newKeys;

    private class Spinner2 implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Log.d("okk",adapterView.getItemAtPosition(i).toString());
            avis.setTitre(adapterView.getItemAtPosition(i).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_avis);
        avis = new Avis();
        avis.setImportance(0);

        mDatabase = FirebaseDatabase.getInstance().getReference("Avis");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    KEYS=datas.getKey();
                }
                newKeys = Integer.parseInt(KEYS)+1;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        this.impSpinner = findViewById(R.id.spinnerImp);
        this.classSpiner = findViewById(R.id.spinnerCours);
        this.buttonConfirm = findViewById(R.id.confirmAvis);
        this.contentText = findViewById(R.id.contentAvis);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this ,R.array.imp_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        impSpinner.setAdapter(adapter);
        impSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this ,R.array.class_array,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpiner.setAdapter(adapter1);
        classSpiner.setOnItemSelectedListener(new Spinner2());


        Date now = new Date();

        DateFormat dateformatter = DateFormat.getDateInstance(DateFormat.LONG);
        final String formattedDate = dateformatter.format(now);
        Log.d("okk",formattedDate);


        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!contentText.getText().toString().equals("")) {
                    Log.d("ok",contentText.getText().toString());
                    avis.setContent(contentText.getText().toString());
                    avis.setDate(formattedDate);


                    final CustomPopup customPopup = new CustomPopup(NewAvis.this);

                    customPopup.setTitle("Etes vous sur de vouloir créer cet avis");
                    customPopup.setSubTitle("Pour : " + avis.getTitre() + " - Importance : " + Integer.toString(avis.getImportance() + 1) + "/3");
                    customPopup.getYesButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateAvis(avis);
                            customPopup.dismiss();
                            finish();
                        }
                    });
                    customPopup.getNoButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(), "avis annulé", Toast.LENGTH_SHORT).show();
                            customPopup.dismiss();
                        }
                    });
                    customPopup.build();
                }
                else{
                    Toast.makeText(getApplicationContext(), "vous n'avez pas entré de contenu", Toast.LENGTH_SHORT).show();

                }




            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Log.d("okk",adapterView.getItemAtPosition(i).toString());
        avis.setImportance(i);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void updateAvis(Avis avis){
        Log.d("okk",KEYS);
        mDatabase1 = FirebaseDatabase.getInstance().getReference("Avis").child(Integer.toString(newKeys));
        avis.setId(newKeys);
        mDatabase1.setValue(avis);
        //sendNotif(avis);


    }
    private void sendNotif(Avis v){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.importance_1)
                .setContentTitle(v.getTitre())
                .setContentText(v.getContent())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);



    }
}
