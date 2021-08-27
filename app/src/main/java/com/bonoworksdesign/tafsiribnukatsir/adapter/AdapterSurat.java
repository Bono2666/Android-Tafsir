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

import com.bonoworksdesign.tafsiribnukatsir.GlobalVar;
import com.bonoworksdesign.tafsiribnukatsir.R;
import com.bonoworksdesign.tafsiribnukatsir.SubTopics;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSurat;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterSurat extends RecyclerView.Adapter<AdapterSurat.myHolder> {

    ArrayList<ModelDataSurat> listSurat = new ArrayList<>();

    Context context;
    SharedPreferences sharedPreferences;

    public AdapterSurat(ArrayList<ModelDataSurat> listSurat, Context context) {
        this.listSurat = listSurat;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.surat_card, viewGroup, false);
        return new myHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {
        myHolder.noSurat.setText(listSurat.get(i).getNoSurat());
        myHolder.nmSurat.setText(listSurat.get(i).getNmSurat());
        myHolder.jmlAyat.setText("Surat ke-" + myHolder.noSurat.getText() + " : " + listSurat.get(i).getJmlAyat() + " ayat");
        myHolder.perTurun.setText(listSurat.get(i).getPerTurun());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String path;

        if (sharedPreferences.getString("Downloaded", "").equals("True")) {
            path = Environment.getExternalStoragePublicDirectory(listSurat.get(i).getUriImg()).toString();
        } else {
            path = String.valueOf(listSurat.get(i).getUriImg());
        }
        Glide.with(context).load(path).into(myHolder.imgSurat);
    }

    @Override
    public int getItemCount() {
        return listSurat.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView noSurat;
        TextView nmSurat;
        TextView jmlAyat;
        TextView perTurun;
        ImageView imgSurat;

        Context context;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        public myHolder (View itemView, Context context) {
            super(itemView);

            noSurat = (TextView) itemView.findViewById(R.id.no_surat);
            nmSurat = (TextView) itemView.findViewById(R.id.surat_label);
            jmlAyat = (TextView) itemView.findViewById(R.id.ayat_label);
            perTurun = (TextView) itemView.findViewById(R.id.periode_label);
            imgSurat = (ImageView) itemView.findViewById(R.id.surat_img);

            this.context = context;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
            editor.putString("NoSurat", noSurat.getText().toString()).apply();
            editor.putString("NmSurat", nmSurat.getText().toString()).apply();

            context.startActivity(new Intent(context, SubTopics.class));
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
