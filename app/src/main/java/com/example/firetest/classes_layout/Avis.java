package com.example.firetest.classes_layout;

public class Avis {
    private String titre;
    private String content;
    private String date;
    private int importance;
    private int id;


    public int getImportance() {
        return importance;
    }

    public Avis(String titre, String content, String date, int importance ) {
        this.titre = titre;
        this.content = content;
        this.date = date;
        this.importance = importance;

    }
    public Avis(){

    }

    public String getTitre() {
        return titre;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
