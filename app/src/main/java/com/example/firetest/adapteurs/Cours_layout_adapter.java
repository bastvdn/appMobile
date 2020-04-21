package com.example.firetest.adapteurs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firetest.R;
import com.example.firetest.classes_layout.Cours_layout;

import java.util.List;

public class Cours_layout_adapter extends RecyclerView.Adapter<Cours_layout_adapter.CoursViewHolder> {

    public static class CoursViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mNomCours;
        public TextView mProf;
        public Switch mSwitch;
        public OnNoteListener onNoteListener;

        public CoursViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            mNomCours = itemView.findViewById(R.id.nom_cours);
            mProf = itemView.findViewById(R.id.nom_prof);
            mSwitch = itemView.findViewById(R.id.check_inscr);

            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface  OnNoteListener{
        void onNoteClick(int position);
    }

    private Context context;
    private List<Cours_layout> cours_layout_list;

    private OnNoteListener mOnNoteListener;

    public Cours_layout_adapter(List<Cours_layout> cours_layout_list, OnNoteListener onNoteListener){

        this.cours_layout_list = cours_layout_list;

        this.mOnNoteListener = onNoteListener;
    }



    @NonNull
    @Override
    public CoursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cours_template, parent, false);
        CoursViewHolder cvh = new CoursViewHolder(v, mOnNoteListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CoursViewHolder holder, int position) {
        Cours_layout cours  =  cours_layout_list.get(position);
        holder.mProf.setText(cours.getProf().toString());
        holder.mNomCours.setText(cours.getName().toString());
        if (cours.getInscr()){holder.mSwitch.toggle();}

    }

    @Override
    public int getItemCount() {
        return cours_layout_list.size();
    }





}
