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
import android.widget.Toast;

import com.bonoworksdesign.tafsiribnukatsir.GlobalVar;
import com.bonoworksdesign.tafsiribnukatsir.ListSurat;
import com.bumptech.glide.Glide;
import com.bonoworksdesign.tafsiribnukatsir.R;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataJilid;

import java.util.ArrayList;

public class AdapterJilid extends RecyclerView.Adapter<AdapterJilid.myHolder> {

    ArrayList<ModelDataJilid> listJilid = new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;

    public AdapterJilid(ArrayList<ModelDataJilid> listJilid, Context context) {
        this.listJilid = listJilid;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jilid_card, viewGroup, false);
        return new myHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {
        myHolder.txtNoBuku.setText(listJilid.get(i).getNo_buku());
        myHolder.txtJilid.setText(listJilid.get(i).getJilidBuku());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String path;

        if (sharedPreferences.getString("Downloaded", "").equals("True")) {
            path = Environment.getExternalStoragePublicDirectory(listJilid.get(i).getUriImg()).toString();
        } else {
            path = String.valueOf(listJilid.get(i).getUriImg());
        }
        Glide.with(context).load(path).into(myHolder.imgJilid);
    }

    @Override
    public int getItemCount() {
        return listJilid.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtNoBuku, txtJilid;
        ImageView imgJilid;

        Context context;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        public myHolder (View itemView, Context context) {
            super(itemView);

            txtNoBuku = (TextView) itemView.findViewById(R.id.no_buku_text);
            txtJilid = (TextView) itemView.findViewById(R.id.jilid_label);
            imgJilid = (ImageView) itemView.findViewById(R.id.jilid_img);

            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
            editor.putString("NoBuku", txtNoBuku.getText().toString()).apply();
            editor.putString("Jilid", txtJilid.getText().toString()).apply();

            Intent screenActivity = new Intent(context, ListSurat.class);
            context.startActivity(screenActivity);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
