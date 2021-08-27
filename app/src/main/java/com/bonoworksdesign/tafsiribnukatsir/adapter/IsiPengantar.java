package com.bonoworksdesign.tafsiribnukatsir.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bonoworksdesign.tafsiribnukatsir.R;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelIsiPengantar;

import java.util.ArrayList;

public class IsiPengantar extends RecyclerView.Adapter<IsiPengantar.myHolder> {

    ArrayList<ModelIsiPengantar> listPengantar = new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public IsiPengantar(ArrayList<ModelIsiPengantar> listPengantar, Context context) {
        this.listPengantar = listPengantar;
        this.context = context;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.isipengantar_card, viewGroup, false);
        return new myHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final myHolder myHolder, int i) {
        myHolder.txtNo_Pengantar.setText(listPengantar.get(i).getNo_pengantar());
        myHolder.isiWeb.loadDataWithBaseURL("file:///android_res/","<html> <style type=\"text/css\"> " +
                "@font-face { font-family: 'Uthman';src: url('font/uthman_taha.ttf'); } " +
                "@font-face { font-family: 'Uthmanic';src: url('font/uthmanichafs1ver13.otf'); } " +
                "@font-face { font-family: 'Arabic-5';src: url('font/mcs_ii50.ttf'); } " +
                "@font-face { font-family: 'Arabic-1';src: url('font/kfgqpc_arabic_symbols.otf'); } " +
                "@font-face { font-family: 'Aga_arab';src: url('font/aga_arabesque.ttf'); } " +
                "@font-face { font-family: 'Ubuntu';src: url('font/ubuntu.ttf'); } " +
                "@font-face { font-family: 'Ubuntu-bold';src: url('font/ubuntu_bold.ttf'); } " +
                "@font-face { font-family: 'Ubuntu-medium';src: url('font/ubuntu_medium.ttf'); } " +
                "h1 { margin-top: 0px;margin-bottom: 0px;font-family: 'Ubuntu-bold';font-size: 32px;line-height: 40px;color: #484848; } " +
                "h2 { margin-top: 0px;margin-bottom: 0px;font-family: 'Ubuntu-medium';font-size: 16px;line-height: 36px;color: #484848; } " +
                "h3red { margin-top: 0px;margin-bottom: 0px;font-family: 'Ubuntu';font-size: 14px;line-height: 16px;color: #7c2522; } " +
                "h4red { margin-top: 0px;margin-bottom: 36px;font-family: 'Ubuntu';font-size: 14px;color: #7c2522; } " +
                "h3green { margin-top: 0px;margin-bottom: 0px;font-family: 'Ubuntu';font-size: 14px;line-height: 16px;color: #4b7155; } " +
                "h4green { margin-top: 0px;margin-bottom: 36px;font-family: 'Ubuntu';font-size: 14px;color: #4b7155; } " +
                "h5 { margin-top: 32px;margin-bottom: 0px;font-family: 'Ubuntu-bold';font-size: 24px;line-height: 28px;color: #484848; } " +
                "h6 { margin-top: 0px;margin-bottom: 0px;font-family: 'Ubuntu-bold';font-size: 24px;line-height: 28px;color: #484848; } " +
                "body { margin-left: 0px;margin-right: 0px;font-family: 'Ubuntu';font-size: 16px;line-height: 24px;color: #484848; } " +
                "arabic { font-family: 'Uthmanic';font-size: 20px;line-height: 20px;color: #484848; } " +
                "arabic0 { font-family: 'Arabic-1';font-size: 20px;color: #484848; } " +
                "arabic7 { font-family: 'Aga_arab';font-size: 20px;color: #484848; } " +
                "arabic2 { font-family: 'Arabic-5';font-size: 12px;color: #7c2522; } " +
                "arabic4 { font-family: 'Arabic-5';font-size: 12px;color: #484848; } " +
                "arabic1 { font-family: 'Arabic-1';font-size: 24px;color: #484848; } " +
                "arabic6 { font-family: 'Aga_arab';font-size: 24px;color: #484848; } " +
                "as { margin-left: 0px;margin-right: 0px;font-family: 'Uthman';font-size: 24px;line-height: 40px;color: #484848; } " +
                "arabic3 { margin-left: 0px;margin-right: 0px;font-family: 'Uthmanic';font-size: 24px;line-height: 40px;color: #484848; } " +
                "arabic5 { margin-left: 0px;margin-right: 0px;font-family: 'Arabic-5';font-size: 32px;line-height: 24px;color: #484848; } " +
                "bigarabic { margin-left: 0px;margin-right: 0px;font-family: 'Uthmanic';font-size: 32px;line-height: 52px;color: #484848; } " +
                "bigphrase { font-family: 'Arabic-1';font-size: 36px;line-height: 48px;color: #484848; } " +
                "hs { margin-left: 0px;margin-right: 0px;font-family: 'Uthman';font-size: 28px;line-height: 48px;color: #484848; } " +
                "hadith { margin-left: 0px;margin-right: 0px;font-family: 'Uthmanic';font-size: 28px;line-height: 48px;color: #484848; } " +
                "py { font-family: 'Uthmanic';font-size: 28px;line-height: 48px;color: #484848; } " +
                "p.arabic { direction: rtl;text-align: right; } " +
                "p.py { direction: rtl;text-align: right;padding-left: 16px;padding-right: 36px; } " +
                "ol { list-style-type : none;padding-left: 26px;text-indent: -26px;font-family: 'Ubuntu';font-size: 16px;" +
                "line-height: 24px;margin-top: 16px;color: #484848; } " +
                "hr { margin-top: 28px;height: 0.25px;background-color: #484848; } " +
                "ul { font-family: 'Ubuntu';list-style-type : none;padding-left: 15px;text-indent: -15px;font-size: 14px;" +
                "line-height: 18px;color: #484848; } " +
                "</style>" + listPengantar.get(i).getIsi() + "</html>", "text/html", "utf-8", null);
        myHolder.txtHal.setText(listPengantar.get(i).getHal());
        final int fp = listPengantar.get(i).getHal_1();
        myHolder.txtNoSubTopik.setText(listPengantar.get(i).getNo_sub_topik());
//        myHolder.txtFooter.setText(listPengantar.get(i).getFooter());
        myHolder.footerWeb.loadDataWithBaseURL("file:///android_res/","<html> <style type=\"text/css\"> " +
                "@font-face { font-family: 'Uthmanic';src: url('font/kfgqpc_arabic_symbols.otf'); } " +
                "@font-face { font-family: 'Ubuntu';src: url('font/ubuntu_medium.ttf'); } " +
                "body { margin-left: 0px;margin-right: 0px;font-family: 'Ubuntu';font-style: italic;font-size: 12px; } " +
                "red { color: #7c2522; } " +
                "green { color: #4b7155; } " +
                "ar { font-family: 'Uthmanic';font-size: 20px;font-style: normal; } " +
                "</style>" + sharedPreferences.getString("JudulPengantar", "") + "</html>", "text/html", "utf-8", null);

        if (listPengantar.get(i).getUri_img().contains("red")) {
            myHolder.txtHal.setTextColor(ContextCompat.getColor(context, R.color.red));
            myHolder.txtFooter.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

        myHolder.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myHolder.scrollIsi.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    editor.putInt("Pos", Integer.parseInt(myHolder.txtHal.getText().toString()) - fp).apply();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listPengantar.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder {
        TextView txtNo_Pengantar, txtHal, txtNoSubTopik, txtFooter;
        ImageView backButton;
        ScrollView scrollIsi;
        WebView isiWeb, footerWeb;

        Context context;

        public myHolder (View itemView, Context context) {
            super(itemView);

            txtNo_Pengantar = (TextView) itemView.findViewById(R.id.no_pengantar_text);
            isiWeb = (WebView) itemView.findViewById(R.id.isi_web);
            txtHal = (TextView) itemView.findViewById(R.id.hal_text);
            backButton = (ImageView) itemView.findViewById(R.id.back_button);
            scrollIsi = (ScrollView) itemView.findViewById(R.id.isi_scroll);
            txtNoSubTopik = (TextView) itemView.findViewById(R.id.no_sub_topik_text);
            txtFooter = (TextView) itemView.findViewById(R.id.judul_isi_text);
            footerWeb = (WebView) itemView.findViewById(R.id.judul_isi_web);

            this.context = context;

        }
    }
}
