package com.example.firetest;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.TextView;

public class CustomPopup extends Dialog {

    private String title;
    private String subtitle;
    private Button yesButton;
    private Button noButton;
    private TextView titleView, subTitleView;

    public CustomPopup(Activity activity){

        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.my_popup_template);
        this.title = "montitre";
        this.subtitle = "monsoustitre";
        this.yesButton = findViewById(R.id.yesButtonPopUp);
        this.noButton = findViewById(R.id.noButtonPopUp);
        this.titleView = findViewById(R.id.titlePopUp);
        this.subTitleView = findViewById(R.id.subtitlePopUp);
    }

    public void setTitle(String title){ this.title = title ;}
    public void setSubTitle(String subtitle){ this.subtitle = subtitle ;}
    public Button getYesButton(){return yesButton;}
    public Button getNoButton(){return noButton;}

    public void build(){
        show();
        titleView.setText(title);
        subTitleView.setText(subtitle);
    }
}
