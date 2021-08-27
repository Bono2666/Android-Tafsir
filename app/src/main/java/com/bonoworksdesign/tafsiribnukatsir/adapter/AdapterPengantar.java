package com.bonoworksdesign.tafsiribnukatsir.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonoworksdesign.tafsiribnukatsir.ListSurat;
import com.bonoworksdesign.tafsiribnukatsir.PengantarSubTopics;
import com.bonoworksdesign.tafsiribnukatsir.R;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataJilid;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataPengantar;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterPengantar extends RecyclerView.Adapter<AdapterPengantar.myHolder> {

    ArrayList<ModelDataPengantar> listPengantar = new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;

    public AdapterPengantar(ArrayList<ModelDataPengantar> listPengantar, Context context) {
        this.listPengantar = listPengantar;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pengantar_card, viewGroup, false);
        return new myHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {
        myHolder.txtNoPengantar.setText(listPengantar.get(i).getNoPengantar());
        myHolder.txtJudul.setText(listPengantar.get(i).getJudul());
        myHolder.txtFooter.setText(listPengantar.get(i).getFooter());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String path;

        if (sharedPreferences.getString("Downloaded", "").equals("True")) {
            path = Environment.getExternalStoragePublicDirectory(listPengantar.get(i).getUriImg()).toString();
        } else {
            path = String.valueOf(listPengantar.get(i).getUriImg());
        }
        Glide.with(context).load(path).into(myHolder.imgPengantar);
    }

    @Override
    public int getItemCount() {
        return listPengantar.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtNoPengantar, txtJudul, txtFooter;
        ImageView imgPengantar;

        Context context;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        public myHolder (View itemView, Context context) {
            super(itemView);

            txtNoPengantar = (TextView) itemView.findViewById(R.id.no_pengantar_text);
            txtJudul = (TextView) itemView.findViewById(R.id.judul_text);
            txtFooter = (TextView) itemView.findViewById(R.id.footer_text);
            imgPengantar = (ImageView) itemView.findViewById(R.id.pengantar_img);

            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
            editor.putString("NoPengantar", txtNoPengantar.getText().toString()).apply();
            editor.putString("JudulPengantar", txtFooter.getText().toString()).apply();

            Intent screenActivity = new Intent(context, PengantarSubTopics.class);
            context.startActivity(screenActivity);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
