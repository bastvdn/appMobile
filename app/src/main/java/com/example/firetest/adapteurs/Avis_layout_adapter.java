package com.example.firetest.adapteurs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firetest.R;
import com.example.firetest.classes_layout.Avis;
import com.example.firetest.classes_layout.Cours_layout;

import java.util.List;

public class Avis_layout_adapter extends RecyclerView.Adapter<Avis_layout_adapter.AvisViewHolder> {

    private List<Avis> liste_avis;
    private OnNoteListener mOnNoteListener;
    private String imp;
    private Context context;

    public Avis_layout_adapter(List<Avis> liste_avis, OnNoteListener mOnNoteListener) {
        this.liste_avis = liste_avis;
        this.mOnNoteListener = mOnNoteListener;
    }

    @NonNull
    @Override
    public Avis_layout_adapter.AvisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.avis_template, parent, false);
        Avis_layout_adapter.AvisViewHolder avh = new Avis_layout_adapter.AvisViewHolder(v, mOnNoteListener);
        context = parent.getContext();
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull Avis_layout_adapter.AvisViewHolder holder, int position) {
        Avis avis =  liste_avis.get(position);
        holder.mTitre.setText(avis.getTitre());
        holder.mContent.setText(avis.getContent());
        holder.mDate.setText(avis.getDate());

        imp = Integer.toString(avis.getImportance());
        String ressourceName = "importance_"+imp;

        int resID = context.getResources().getIdentifier(ressourceName,"drawable",context.getPackageName());
        holder.mImage.setImageResource(resID);

    }

    @Override
    public int getItemCount() {
        return liste_avis.size();
    }

    public static class AvisViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitre;
        public TextView mContent;
        public TextView mDate;
        public ImageView mImage;
        public Avis_layout_adapter.OnNoteListener onNoteListener;

        public AvisViewHolder(@NonNull View itemView, Avis_layout_adapter.OnNoteListener onNoteListener) {
            super(itemView);
            mTitre = itemView.findViewById(R.id.titre_avis);
            mContent = itemView.findViewById(R.id.contenu_avis);
            mImage = itemView.findViewById(R.id.image_avis);
            mDate = itemView.findViewById(R.id.dateAvis);

            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }





}
