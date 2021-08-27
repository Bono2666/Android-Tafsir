package com.bonoworksdesign.tafsiribnukatsir.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Shader;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonoworksdesign.tafsiribnukatsir.GlobalVar;
import com.bonoworksdesign.tafsiribnukatsir.R;
import com.bonoworksdesign.tafsiribnukatsir.ViewHelp;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataBantuan;

import java.util.ArrayList;

public class AdapterBantuan extends RecyclerView.Adapter<AdapterBantuan.myHolder> {

    ArrayList<ModelDataBantuan> listBantuan = new ArrayList<>();
    Context context;

    public AdapterBantuan(ArrayList<ModelDataBantuan> listBantuan, Context context) {
        this.listBantuan = listBantuan;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.help_card, viewGroup, false);
        return new myHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {
        myHolder.idBantuan.setText(listBantuan.get(i).getId_bantuan());
        myHolder.pertanyaan.setText(listBantuan.get(i).getPertanyaan());

        if (i == listBantuan.size() - 1) {
            myHolder.line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listBantuan.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView idBantuan, pertanyaan;
        ImageView line;

        Context context;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        public myHolder (View itemView, Context context) {
            super(itemView);

            idBantuan = (TextView) itemView.findViewById(R.id.id_bantuan);
            pertanyaan = (TextView) itemView.findViewById(R.id.pertanyaan);
            line = (ImageView) itemView.findViewById(R.id.line);

            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
            editor.putString("IdBantuan", idBantuan.getText().toString()).apply();

            Intent screenActivity = new Intent(context, ViewHelp.class);
            context.startActivity(screenActivity);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
