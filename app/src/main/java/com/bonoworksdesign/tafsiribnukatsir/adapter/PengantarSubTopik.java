package com.bonoworksdesign.tafsiribnukatsir.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSubTopics;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSubTpcPengantar;

import java.util.ArrayList;

public class PengantarSubTopik extends RecyclerView.Adapter<PengantarSubTopik.myHolder> {

    ArrayList<ModelDataSubTpcPengantar> listSubTopics = new ArrayList<>();

    Context context;
    public static int firstPage;

    public PengantarSubTopik(ArrayList<ModelDataSubTpcPengantar> listSubTopics, Context context) {
        this.listSubTopics = listSubTopics;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subtopics_card, viewGroup, false);
        return new myHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {
        myHolder.txtNoSubTopik.setText(listSubTopics.get(i).getNo_sub_topik());
        myHolder.txtJudul.setText(Html.fromHtml(listSubTopics.get(i).getJudul_sub_topik()));
        myHolder.txtHal.setText(listSubTopics.get(i).getHal());

        firstPage = listSubTopics.get(i).getHal_1();

        if (i == listSubTopics.size() - 1) {
            myHolder.line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listSubTopics.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtNoSubTopik, txtHal, txtJudul;
        ImageView line;

        Context context;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        public myHolder (View itemView, Context context) {
            super(itemView);

            txtNoSubTopik = (TextView) itemView.findViewById(R.id.no_sub_topik_text);
            txtJudul = (TextView) itemView.findViewById(R.id.judul_text);
            txtHal = (TextView) itemView.findViewById(R.id.hal_text);
            line = (ImageView) itemView.findViewById(R.id.line);

            this.context = context;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
            editor.putString("Hal", txtHal.getText().toString()).apply();
            editor.putInt("Pos", Integer.parseInt(txtHal.getText().toString()) - firstPage).apply();

            Intent screenActivity = new Intent(context, ViewPengantar.class);
            context.startActivity(screenActivity);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
