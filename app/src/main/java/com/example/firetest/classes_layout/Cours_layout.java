package com.example.firetest.classes_layout;

import android.util.Log;

public class Cours_layout {
    public String name;
    public String prof;
    public Boolean inscr = false;

    public Cours_layout(String name, String prof){
        this.name = name;
        this.prof = prof;

    }

    public Cours_layout() {
    }

    public String getName(){ return name;}

    public String getProf(){ return prof;}

    public Boolean getInscr() { return inscr;}

    public void setInscr(Boolean inscr) {
        Log.d("classcours","setinscr");
        this.inscr = inscr;

    }
}
