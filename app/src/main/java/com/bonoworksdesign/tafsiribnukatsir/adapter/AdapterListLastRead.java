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
import com.bonoworksdesign.tafsiribnukatsir.ViewTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataLastRead;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterListLastRead extends RecyclerView.Adapter<AdapterListLastRead.myHolder> {

    ArrayList<ModelDataLastRead> listLastRead = new ArrayList<>();
    SharedPreferences sharedPreferences;
    Context context;

    public AdapterListLastRead(ArrayList<ModelDataLastRead> listLastRead, Context context) {
        this.listLastRead = listLastRead;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listlastread_card, viewGroup, false);
        return new myHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {
        myHolder.txtNoSurat.setText(String.valueOf(listLastRead.get(i).getNoSurat()));
        myHolder.txtHal.setText(String.valueOf(listLastRead.get(i).getHal()));
        myHolder.txtNmSurat.setText(listLastRead.get(i).getNmSurat());
        myHolder.txtJilid.setText("Jilid " + listLastRead.get(i).getJilid() + " hal ");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String path;

        if (sharedPreferences.getString("Downloaded", "").equals("True")) {
            path = Environment.getExternalStoragePublicDirectory(listLastRead.get(i).getSuratImg()).toString();
        } else {
            path = listLastRead.get(i).getSuratImg();
        }

        Glide.with(context).load(path).into(myHolder.imgSurat);
        myHolder.txtFirstPage.setText(String.valueOf(listLastRead.get(i).getFirstPage()));

        if (listLastRead.get(i).getPeriode().equals("Makkiyyah")) {
            myHolder.txtJilid.setTextColor(context.getResources().getColor(R.color.red));
            myHolder.txtHal.setTextColor(context.getResources().getColor(R.color.red));
            myHolder.imgReadIcon.setImageResource(R.drawable.ic_book_red_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return listLastRead.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtNoSurat, txtHal, txtNmSurat, txtJilid, txtFirstPage;
        ImageView imgSurat, imgReadIcon;

        Context context;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        public myHolder (View itemView, Context context) {
            super(itemView);

            txtNoSurat = (TextView) itemView.findViewById(R.id.no_surat_text);
            txtHal = (TextView) itemView.findViewById(R.id.hal_text);
            txtNmSurat = (TextView) itemView.findViewById(R.id.nm_surat_text);
            txtJilid = (TextView) itemView.findViewById(R.id.jilid_text);
            imgSurat = (ImageView) itemView.findViewById(R.id.lastread_img);
            txtFirstPage = (TextView) itemView.findViewById(R.id.first_page_text);
            imgReadIcon = (ImageView) itemView.findViewById(R.id.read_icon);

            this.context = context;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int mPos = Integer.parseInt(txtHal.getText().toString()) - Integer.parseInt(txtFirstPage.getText().toString());

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
            editor.putString("NoSurat", txtNoSurat.getText().toString()).apply();
            editor.putInt("Pos", mPos).apply();

            Intent screenActivity = new Intent(context, ViewTafsir.class);
            context.startActivity(screenActivity);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
