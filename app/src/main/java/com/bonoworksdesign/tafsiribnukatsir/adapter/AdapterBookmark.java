package com.bonoworksdesign.tafsiribnukatsir.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonoworksdesign.tafsiribnukatsir.BookmarkFragment;
import com.bonoworksdesign.tafsiribnukatsir.GlobalVar;
import com.bonoworksdesign.tafsiribnukatsir.R;
import com.bonoworksdesign.tafsiribnukatsir.ViewTafsir;
import com.bonoworksdesign.tafsiribnukatsir.database.DataBookmark;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataBookmark;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataJilid;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataTafsir;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterSubTopics.firstPage;

public class AdapterBookmark extends RecyclerView.Adapter<AdapterBookmark.myHolder> {

    ArrayList<ModelDataBookmark> listBookmark = new ArrayList<>();
    SharedPreferences sharedPreferences;
    Context context;
    BookmarkFragment bookmarkFragment;
    String path, st;

    public AdapterBookmark(ArrayList<ModelDataBookmark> listBookmark, Context context, BookmarkFragment bookmarkFragment) {
        this.listBookmark = listBookmark;
        this.context = context;
        this.bookmarkFragment = bookmarkFragment;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookmark_card, viewGroup, false);
        return new myHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final myHolder myHolder, int i) {
        myHolder.txtNoSurat.setText(listBookmark.get(i).getNoSurat());
        myHolder.txtHal.setText(String.valueOf(listBookmark.get(i).getHal()));
        myHolder.txtNmSurat.setText(listBookmark.get(i).getNmSurat());
        myHolder.txtJilid.setText("Jilid " + listBookmark.get(i).getJilid() + " hal ");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (sharedPreferences.getString("Downloaded", "").equals("True")) {
            path = Environment.getExternalStoragePublicDirectory(listBookmark.get(i).getSuratImg()).toString();
        } else {
            path = listBookmark.get(i).getSuratImg();
        }

        Glide.with(context).load(path).into(myHolder.imgSurat);
        myHolder.txtPath.setText(path);
        myHolder.txtFirstPage.setText(String.valueOf(listBookmark.get(i).getFirstPage()));

        if (listBookmark.get(i).getPeriode().equals("Makkiyyah")) {
            myHolder.txtJilid.setTextColor(context.getResources().getColor(R.color.red));
            myHolder.txtHal.setTextColor(context.getResources().getColor(R.color.red));
            myHolder.imgBookmark.setImageResource(R.drawable.ic_bookmark_red_24dp);
        }

        myHolder.imgBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog;
                TextView btnYa, btnTidak, txtNmSurat, txtJilidHal;
                ImageView imgBookmark;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                v = ((Activity) context).getLayoutInflater().inflate(R.layout.bookmark_dialog, null);
                builder.setView(v);
                dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                btnYa = (TextView) v.findViewById(R.id.ya_btn);
                btnTidak = (TextView) v.findViewById(R.id.tidak_btn);
                txtNmSurat = (TextView) v.findViewById(R.id.nm_surat_text);
                txtJilidHal = (TextView) v.findViewById(R.id.jilid_hal_text);
                imgBookmark = (ImageView) v.findViewById(R.id.bookmark_img);

                Glide.with(context).load(myHolder.txtPath.getText()).into(imgBookmark);
                txtNmSurat.setText(myHolder.txtNmSurat.getText());

                String mJilidHal;
                mJilidHal = myHolder.txtJilid.getText() + "" + myHolder.txtHal.getText();
                txtJilidHal.setText(mJilidHal);

                if (myHolder.txtPath.getText().toString().contains("red")) {
                    txtJilidHal.setTextColor(ContextCompat.getColor(context, R.color.red));
                    btnTidak.setTextColor(ContextCompat.getColor(context, R.color.red));
                    imgBookmark.setBackground(ContextCompat.getDrawable(context, R.color.red));
                }

                btnYa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataBookmark dataBookmark = new DataBookmark(context);
                        int no = Integer.parseInt(myHolder.txtNoSurat.getText().toString());
                        int hal = Integer.parseInt(myHolder.txtHal.getText().toString());
                        dataBookmark.removeBookmark(no, hal);

                        dialog.dismiss();
                        bookmarkFragment.onResume();
                    }
                });

                btnTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBookmark.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtNoSurat, txtHal, txtNmSurat, txtJilid, txtFirstPage, txtPath;
        ImageView imgSurat, imgBookmark;

        Context context;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        public myHolder (View itemView, Context context) {
            super(itemView);

            txtNoSurat = (TextView) itemView.findViewById(R.id.no_surat_text);
            txtHal = (TextView) itemView.findViewById(R.id.hal_text);
            txtNmSurat = (TextView) itemView.findViewById(R.id.nm_surat_text);
            txtJilid = (TextView) itemView.findViewById(R.id.jilid_text);
            imgSurat = (ImageView) itemView.findViewById(R.id.bookmark_img);
            txtFirstPage = (TextView) itemView.findViewById(R.id.first_page_text);
            imgBookmark = (ImageView) itemView.findViewById(R.id.bookmark_button);
            txtPath = (TextView) itemView.findViewById(R.id.path_txt);

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
