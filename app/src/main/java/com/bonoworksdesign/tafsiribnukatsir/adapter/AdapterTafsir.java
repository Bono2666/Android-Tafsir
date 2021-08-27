package com.bonoworksdesign.tafsiribnukatsir.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.LeadingMarginSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonoworksdesign.tafsiribnukatsir.R;
import com.bonoworksdesign.tafsiribnukatsir.database.DataBookmark;
import com.bonoworksdesign.tafsiribnukatsir.database.DataLastRead;
import com.bonoworksdesign.tafsiribnukatsir.database.DataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataTafsir;

import java.util.ArrayList;

import static android.support.design.widget.Snackbar.make;

public class AdapterTafsir extends RecyclerView.Adapter<AdapterTafsir.myHolder> {

    ArrayList<ModelDataTafsir> listTafsir = new ArrayList<>();
    Context context;
    DataBookmark dbBookmark;
    DataTafsir dbLastread;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public AdapterTafsir(ArrayList<ModelDataTafsir> listTafsir, Context context) {
        this.listTafsir = listTafsir;
        this.context = context;
        dbBookmark = new DataBookmark(context);
        dbLastread = new DataTafsir(context);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tafsir_card, viewGroup, false);
        return new myHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final myHolder myHolder, int i) {
        myHolder.txtNo_Surat.setText(listTafsir.get(i).getNo_surat());
        myHolder.suratWeb.loadDataWithBaseURL("file:///android_res/","<html> <style type=\"text/css\"> " +
                "@font-face { font-family: 'Mcs-Quran';src: url('font/mcs_quran.ttf'); } " +
                "@font-face { font-family: 'Mcs-Quran-1';src: url('font/mcs_quran_1.ttf'); } " +
                "@font-face { font-family: 'Mcs-Quran-2';src: url('font/mcs_quran_2.ttf'); } " +
                "surah { font-family: 'Mcs-Quran';font-size: 24px;color: #4b7155; } " +
                "nm1 { font-family: 'Mcs-Quran-1';font-size: 24px;color: #4b7155; } " +
                "nm2 { font-family: 'Mcs-Quran-2';font-size: 24px;color: #4b7155; } " +
                "surahred { font-family: 'Mcs-Quran';font-size: 24px;color: #7c2522; } " +
                "nmred { font-family: 'Mcs-Quran-1';font-size: 24px;color: #7c2522; } " +
                "</style>" + listTafsir.get(i).getArabic() + "</html>", "text/html", "utf-8", null);
        myHolder.txtSuratArabic.setText(listTafsir.get(i).getArabic());
        myHolder.txtNm_Surat.setText(listTafsir.get(i).getNm_surat());
        myHolder.txtNo_Buku.setText(listTafsir.get(i).getNo_buku());
        myHolder.txtPeriode.setText(listTafsir.get(i).getPeriode());
        myHolder.txtSuratImg.setText(listTafsir.get(i).getSurat_img());
        myHolder.juz.setText(listTafsir.get(i).getJuz());
        myHolder.txtTafsir.setText(listTafsir.get(i).getTafsir());
        myHolder.tafsirWeb.loadDataWithBaseURL("file:///android_res/","<html> <style type=\"text/css\"> " +
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
                "p.py { direction: rtl;text-align: right;padding-left: 20px;padding-right: 48px; } " +
                "ol { list-style-type : none;padding-left: 17px;text-indent: -17px;font-family: 'Ubuntu';font-size: 16px;" +
                "line-height: 24px;margin-top: 16px;color: #484848; } " +
                "hr { margin-top: 28px;height: 0.25px;background-color: #484848; } " +
                "ul { font-family: 'Ubuntu';list-style-type : none;padding-left: 15px;text-indent: -15px;font-size: 14px;" +
                "line-height: 18px;color: #484848; } " +
                "</style>" + listTafsir.get(i).getTafsir() + "</html>", "text/html", "utf-8", null);
        myHolder.txtJuz.setText(listTafsir.get(i).getJuz_arabic());
        myHolder.txtJuzNo.setText(listTafsir.get(i).getUri_img());
        myHolder.txtHal.setText(listTafsir.get(i).getHal());
        final int fp = listTafsir.get(i).getHal_1();
        myHolder.txtNoSubTopik.setText(listTafsir.get(i).getNo_sub_topik());
        myHolder.txtJudul.setText(listTafsir.get(i).getJudul());

        if (myHolder.txtPeriode.getText().equals("Makkiyyah")) {
            myHolder.txtSuratArabic.setTextColor(ContextCompat.getColor(context, R.color.red));
            myHolder.txtHal.setTextColor(ContextCompat.getColor(context, R.color.red));
            myHolder.txtJuz.setTextColor(ContextCompat.getColor(context, R.color.red));
            myHolder.txtJuzNo.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

        if (dbBookmark.isBookmark(Integer.parseInt(myHolder.txtNo_Surat.getText().toString()), myHolder.txtHal.getText().toString())) {
            if (myHolder.txtPeriode.getText().equals("Makkiyyah")) {
                myHolder.bookMark.setImageResource(R.drawable.ic_bookmark_red_24dp);
            } else {
                myHolder.bookMark.setImageResource(R.drawable.ic_bookmark_green_24dp);
            }
        }

        myHolder.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });

        myHolder.bookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dbBookmark.isBookmark(Integer.parseInt(myHolder.txtNo_Surat.getText().toString()), myHolder.txtHal.getText().toString())) {
                    dbBookmark.addBookmark(Integer.parseInt(myHolder.txtNo_Surat.getText().toString()),
                            Integer.parseInt(myHolder.txtHal.getText().toString()), myHolder.txtNm_Surat.getText().toString(),
                            myHolder.txtNo_Buku.getText().toString(), myHolder.txtPeriode.getText().toString(), myHolder.txtSuratImg.getText().toString(), fp);
                    String msg = "Halaman " + myHolder.txtHal.getText() + " berhasil di bookmark.";
                    Snackbar snackBar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
                    if (myHolder.txtPeriode.getText().equals("Makkiyyah")) {
                        View view = snackBar.getView();
                        view.setBackgroundColor(ContextCompat.getColor(context,R.color.red));
                        myHolder.bookMark.setImageResource(R.drawable.ic_bookmark_red_24dp);
                    } else {
                        myHolder.bookMark.setImageResource(R.drawable.ic_bookmark_green_24dp);
                    }
                    snackBar.show();
                } else {
                    dbBookmark.removeBookmark(Integer.parseInt(myHolder.txtNo_Surat.getText().toString()), Integer.parseInt(myHolder.txtHal.getText().toString()));
                    myHolder.bookMark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    String msg = "Hapus bookmark untuk halaman " + myHolder.txtHal.getText() + ".";
                    Snackbar snackBar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
                    if (myHolder.txtPeriode.getText().equals("Makkiyyah")) {
                        View view = snackBar.getView();
                        view.setBackgroundColor(ContextCompat.getColor(context,R.color.red));
                    }
                    snackBar.show();
                }

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myHolder.scrollTafsir.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    v = myHolder.scrollTafsir.getChildAt(myHolder.scrollTafsir.getChildCount() - 1);
                    int diff = (v.getBottom() - (myHolder.scrollTafsir.getHeight() + myHolder.scrollTafsir.getScrollY()));

                    editor.putInt("Pos", Integer.parseInt(myHolder.txtHal.getText().toString()) - fp).apply();

                    if (diff == 0) {
                        if (!dbLastread.isExist(Integer.parseInt(myHolder.txtNo_Surat.getText().toString()), Integer.parseInt(myHolder.txtHal.getText().toString()))) {
                            dbLastread.addLastread(Integer.parseInt(myHolder.txtNo_Surat.getText().toString()),
                                    Integer.parseInt(myHolder.txtHal.getText().toString()), myHolder.txtNm_Surat.getText().toString(),
                                    myHolder.txtNo_Buku.getText().toString(), myHolder.txtPeriode.getText().toString(),
                                    myHolder.txtSuratImg.getText().toString(), fp, myHolder.juz.getText().toString(),
                                    myHolder.txtTafsir.getText().toString().replace("'",""),
                                    myHolder.txtSuratArabic.getText().toString(),
                                    Integer.parseInt(myHolder.txtNoSubTopik.getText().toString()),
                                    myHolder.txtJudul.getText().toString().replace("'",""));
                        } else {
                            dbLastread.removeLastread(Integer.parseInt(myHolder.txtNo_Surat.getText().toString()), Integer.parseInt(myHolder.txtHal.getText().toString()));
                            dbLastread.addLastread(Integer.parseInt(myHolder.txtNo_Surat.getText().toString()),
                                    Integer.parseInt(myHolder.txtHal.getText().toString()), myHolder.txtNm_Surat.getText().toString(),
                                    myHolder.txtNo_Buku.getText().toString(), myHolder.txtPeriode.getText().toString(),
                                    myHolder.txtSuratImg.getText().toString(), fp, myHolder.juz.getText().toString(),
                                    myHolder.txtTafsir.getText().toString().replace("'",""),
                                    myHolder.txtSuratArabic.getText().toString(),
                                    Integer.parseInt(myHolder.txtNoSubTopik.getText().toString()),
                                    myHolder.txtJudul.getText().toString().replace("'",""));
                        }

                        if (dbLastread.isThirteen()) {
                            DataTafsir lastRead = new DataTafsir(context);
                            Cursor data = lastRead.getData("");

                            data.moveToFirst();

                            int surat = data.getInt(0);
                            int hal = data.getInt(1);

                            dbLastread.removeLastread(surat, hal);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listTafsir.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder {
        TextView txtNo_Surat, txtSuratArabic, txtNm_Surat, txtNo_Buku, txtPeriode, txtSuratImg, juz, txtTafsir, txtJuz, txtJuzNo, txtHal,
                txtNoSubTopik, txtJudul;
        ImageView bookMark, backButton;
        ScrollView scrollTafsir;
        WebView tafsirWeb, suratWeb;

        Context context;

        public myHolder (View itemView, Context context) {
            super(itemView);

            txtNo_Surat = (TextView) itemView.findViewById(R.id.no_surat_text);
            suratWeb = (WebView) itemView.findViewById(R.id.surat_web);
            txtSuratArabic = (TextView) itemView.findViewById(R.id.surat_arabic_text);
            txtNm_Surat = (TextView) itemView.findViewById(R.id.nm_surat_text);
            txtNo_Buku = (TextView) itemView.findViewById(R.id.no_buku_text);
            txtPeriode = (TextView) itemView.findViewById(R.id.periode_text);
            txtSuratImg = (TextView) itemView.findViewById(R.id.surat_img);
            juz = (TextView) itemView.findViewById(R.id.juz);
            tafsirWeb = (WebView) itemView.findViewById(R.id.tafsir_web);
            txtTafsir = (TextView) itemView.findViewById(R.id.tafsir_text);
            txtJuz = (TextView) itemView.findViewById(R.id.juz_text);
            txtJuzNo = (TextView) itemView.findViewById(R.id.juz_no_text);
            txtHal = (TextView) itemView.findViewById(R.id.hal_text);
            bookMark = (ImageView) itemView.findViewById(R.id.bookmark_ic);
            backButton = (ImageView) itemView.findViewById(R.id.back_button);
            scrollTafsir = (ScrollView) itemView.findViewById(R.id.tafsir_scroll);
            txtNoSubTopik = (TextView) itemView.findViewById(R.id.no_sub_topik_text);
            txtJudul = (TextView) itemView.findViewById(R.id.judul_text);

            this.context = context;

        }
    }
}
