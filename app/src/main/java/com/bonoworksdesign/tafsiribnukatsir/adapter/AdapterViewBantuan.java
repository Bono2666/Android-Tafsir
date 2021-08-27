package com.bonoworksdesign.tafsiribnukatsir.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonoworksdesign.tafsiribnukatsir.R;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataBantuan;

import java.util.ArrayList;

public class AdapterViewBantuan extends RecyclerView.Adapter<AdapterViewBantuan.myHolder> {

    ArrayList<ModelDataBantuan> listBantuan = new ArrayList<>();
    Context context;

    public AdapterViewBantuan(ArrayList<ModelDataBantuan> listBantuan, Context context) {
        this.listBantuan = listBantuan;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.help_view_card, viewGroup, false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {
        myHolder.pertanyaan.setText(listBantuan.get(i).getPertanyaan());
        myHolder.jawaban.loadDataWithBaseURL("file:///android_res/","<html> <style type=\"text/css\"> " +
                "@font-face { font-family: 'Ubuntu';src: url('font/ubuntu.ttf'); } " +
                "@font-face { font-family: 'Ubuntu-bold';src: url('font/ubuntu_bold.ttf'); } " +
                "@font-face { font-family: 'Ubuntu-medium';src: url('font/ubuntu_medium.ttf'); } " +
                "h1 { margin-top: 12px;margin-bottom: 16px;font-family: 'Ubuntu-bold';font-size: 24px;color: #484848; } " +
                "body { margin-left: 0px;margin-right: 0px;font-family: 'Ubuntu';font-size: 16px;line-height: 24px;color: #484848;} " +
                "ol { list-style-type : none;padding-left: 17px;text-indent: -17px;font-family: 'Ubuntu';font-size: 16px;" +
                "line-height: 24px;margin-top: 16px;margin-bottom: 8px;color: #484848; } " +
                "hr { margin-top: 28px;height: 0.25px;background-color: #484848; } " +
                "ul { font-family: 'Ubuntu';list-style-type : none;padding-left: 14px;text-indent: -14px;font-size: 14px;" +
                "line-height: 20px;color: #484848; } " +
                "</style>" + listBantuan.get(i).getJawaban() + "</html>", "text/html", "utf-8", null);

        myHolder.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBantuan.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder {
        TextView pertanyaan;
        ImageView backBtn;
        WebView jawaban;

        public myHolder (View itemView) {
            super(itemView);

            pertanyaan = (TextView) itemView.findViewById(R.id.pertanyaan);
            jawaban = (WebView) itemView.findViewById(R.id.jawaban_Web);
            backBtn = (ImageView) itemView.findViewById(R.id.back_button);

        }
    }
}
