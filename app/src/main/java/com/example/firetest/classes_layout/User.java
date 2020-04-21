package com.example.firetest.classes_layout;

import java.util.ArrayList;

public class User {


    public String name;
    public ArrayList<String> cours;

    public User(){}

    public void addCours(String cours){
        this.cours.add(cours);

    }

    public ArrayList<String> getCours() {
        return cours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCours(ArrayList<String> cours) {
        this.cours = cours;
    }
}

