package com.bonoworksdesign.tafsiribnukatsir.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonoworksdesign.tafsiribnukatsir.R;
import com.bonoworksdesign.tafsiribnukatsir.ViewPengantar;
import com.bonoworksdesign.tafsiribnukatsir.ViewTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSubTpcPengantar;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterSearchPengantar extends RecyclerView.Adapter<AdapterSearchPengantar.myHolder> {

    ArrayList<ModelDataSubTpcPengantar> listCari = new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;

    public AdapterSearchPengantar(ArrayList<ModelDataSubTpcPengantar> listCari, Context context) {
        this.listCari = listCari;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_card, viewGroup, false);
        return new myHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {
        myHolder.txtNoPengantar.setText(listCari.get(i).getNo_pengantar());
        myHolder.txtNoSubTopik.setText(listCari.get(i).getNo_sub_topik());
        String judul = "<b>" + listCari.get(i).getJudul() + "</b>, " + listCari.get(i).getJudul_sub_topik();
        myHolder.txtJudul.setText(Html.fromHtml(judul));
        myHolder.txtHal.setText(listCari.get(i).getHal());
        myHolder.txtHal1.setText(String.valueOf(listCari.get(i).getHal_1()));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String path;

        if (sharedPreferences.getString("Downloaded", "").equals("True")) {
            path = Environment.getExternalStoragePublicDirectory(listCari.get(i).getUri_img()).toString();
        } else {
            path = String.valueOf(listCari.get(i).getUri_img());
        }

        Glide.with(context).load(path).into(myHolder.imgPengantar);

        if (i == listCari.size() - 1) {
            myHolder.line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listCari.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtNoPengantar, txtNoSubTopik, txtJudul, txtHal, txtHal1;
        ImageView imgPengantar, line;

        Context context;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        public myHolder (View itemView, Context context) {
            super(itemView);

            txtNoPengantar = (TextView) itemView.findViewById(R.id.no_surat_text);
            txtNoSubTopik = (TextView) itemView.findViewById(R.id.no_sub_topik_text);
            txtJudul = (TextView) itemView.findViewById(R.id.judul_text);
            txtHal = (TextView) itemView.findViewById(R.id.hal_text);
            txtHal1 = (TextView) itemView.findViewById(R.id.hal1_text);
            imgPengantar = (ImageView) itemView.findViewById(R.id.surat_img);
            line = (ImageView) itemView.findViewById(R.id.line);

            this.context = context;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
            editor.putString("NoPengantar", txtNoPengantar.getText().toString()).apply();
            editor.putString("Hal", txtHal.getText().toString()).apply();
            editor.putInt("Pos", Integer.parseInt(txtHal.getText().toString()) - Integer.parseInt(txtHal1.getText().toString())).apply();

            Intent screenActivity = new Intent(context, ViewPengantar.class);
            context.startActivity(screenActivity);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
