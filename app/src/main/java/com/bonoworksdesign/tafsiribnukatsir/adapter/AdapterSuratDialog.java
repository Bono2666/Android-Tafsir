package com.bonoworksdesign.tafsiribnukatsir.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonoworksdesign.tafsiribnukatsir.GlobalVar;
import com.bonoworksdesign.tafsiribnukatsir.R;
import com.bonoworksdesign.tafsiribnukatsir.SubTopics;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSurat;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterSuratDialog extends RecyclerView.Adapter<AdapterSuratDialog.myHolder> {

    ArrayList<ModelDataSurat> listSurat = new ArrayList<>();

    int selectedPos = RecyclerView.NO_POSITION;

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public AdapterSuratDialog(ArrayList<ModelDataSurat> listSurat, Context context) {
        this.listSurat = listSurat;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.surat_card_dialog, viewGroup, false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myHolder myHolder, int i) {
        myHolder.noSurat.setText(listSurat.get(i).getNoSurat());
        myHolder.nmSurat.setText(listSurat.get(i).getNoSurat() + " | " + listSurat.get(i).getNmSurat().substring(6));

        myHolder.itemView.setSelected(selectedPos == i);

        myHolder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPos);
                selectedPos = myHolder.getAdapterPosition();
                notifyItemChanged(selectedPos);

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                editor = sharedPreferences.edit();
                editor.putString("TempNoSurat", myHolder.noSurat.getText().toString()).apply();
                editor.putString("TempNmSurat", myHolder.nmSurat.getText().toString()).apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSurat.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder {
        TextView noSurat, nmSurat;
        ImageView optImg, line;
        RelativeLayout itemCard;

        public myHolder (View itemView) {
            super(itemView);

            noSurat = (TextView) itemView.findViewById(R.id.no_surat_text);
            nmSurat = (TextView) itemView.findViewById(R.id.surat_text);
            optImg = (ImageView) itemView.findViewById(R.id.option_img);
            itemCard = (RelativeLayout) itemView.findViewById(R.id.item_card);
            line = (ImageView) itemView.findViewById(R.id.line);
        }
    }
}
