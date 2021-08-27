package com.bonoworksdesign.tafsiribnukatsir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterSubTopics;
import com.bonoworksdesign.tafsiribnukatsir.adapter.PengantarSubTopik;
import com.bonoworksdesign.tafsiribnukatsir.api.APIService;
import com.bonoworksdesign.tafsiribnukatsir.database.DataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSubTopics;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSubTpcPengantar;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PengantarSubTopics extends AppCompatActivity {

    RecyclerView subTopicsRecycler;
    TextView mJudul, cariTxt;
    ImageView backBtn;
    RelativeLayout searchLayout;
    ShimmerFrameLayout subtopikShimmer;
    WebView webJudul;

    String kriteria;

    PengantarSubTopik adapterSubTopics;
    ArrayList<ModelDataSubTpcPengantar> listSubTopics = new ArrayList<ModelDataSubTpcPengantar>();
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pengantar_subtopics_list);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PengantarSubTopics.this);
        editor = sharedPreferences.edit();

        mJudul = (TextView) findViewById(R.id.judul_pengantar);
        webJudul = (WebView) findViewById(R.id.judul_web);
        subTopicsRecycler = (RecyclerView) findViewById(R.id.subtopics_recyclerview);
        backBtn = (ImageView) findViewById(R.id.back_button);
        searchLayout = (RelativeLayout) findViewById(R.id.search_button);
        cariTxt = (TextView) findViewById(R.id.cari_text);
        subtopikShimmer = (ShimmerFrameLayout) findViewById(R.id.subtopik_shimmer);

        webJudul.loadDataWithBaseURL("file:///android_res/","<html> <style type=\"text/css\"> " +
                "@font-face { font-family: 'Uthmanic';src: url('font/kfgqpc_arabic_symbols.otf'); } " +
                "@font-face { font-family: 'Ubuntu';src: url('font/ubuntu_bold.ttf'); } " +
                "body { margin-left: 0px;margin-right: 0px;font-family: 'Ubuntu';font-size: 28px;line-height: 32px;color: #484848; } " +
                "red { color: #484848; } " +
                "green { color: #484848; } " +
                "ar { font-family: 'Uthmanic';font-size: 40px; } " +
                "</style>" + sharedPreferences.getString("JudulPengantar", "") + "</html>", "text/html", "utf-8", null);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PengantarSubTopics.this, SearchPengantar.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        onResume();
    }

    private void loadSubTopik() {
        DataTafsir subtopik = new DataTafsir(PengantarSubTopics.this);
        Cursor data;
        listSubTopics.clear();

        data = subtopik.getPengantarSubTopik(kriteria);

        while (data.moveToNext()) {
            ModelDataSubTpcPengantar dataSubTopik = new ModelDataSubTpcPengantar(
                    data.getString(0),
                    data.getString(1),
                    data.getInt(2),
                    data.getString(3),
                    data.getString(4),
                    data.getString(5),
                    data.getString(6)
            );

            listSubTopics.add(dataSubTopik);
        }

        subtopikShimmer.stopShimmer();
        subtopikShimmer.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(PengantarSubTopics.this, LinearLayoutManager.VERTICAL, false);
        subTopicsRecycler.setLayoutManager(layoutManager);
        adapterSubTopics = new PengantarSubTopik(listSubTopics, PengantarSubTopics.this);
        subTopicsRecycler.setAdapter(adapterSubTopics);
        subTopicsRecycler.setVisibility(View.VISIBLE);
    }

    public void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);
        Call<List<ModelDataSubTpcPengantar>> callSubTopics;
        callSubTopics = api.getDataSubTpcPengantar(kriteria);

        callSubTopics.enqueue(new Callback<List<ModelDataSubTpcPengantar>>() {
            @Override
            public void onResponse(Call<List<ModelDataSubTpcPengantar>> callSubTopics, Response<List<ModelDataSubTpcPengantar>> responseSubTopics) {

                listSubTopics.clear();

                if (responseSubTopics.isSuccessful()) {
                    int jmlRecSubTopics = responseSubTopics.body().size();

                    for (int iSubTopics = 0; iSubTopics < jmlRecSubTopics; iSubTopics++) {
                        ModelDataSubTpcPengantar dataSubTopics = new ModelDataSubTpcPengantar(
                                responseSubTopics.body().get(iSubTopics).getNo_pengantar(),
                                responseSubTopics.body().get(iSubTopics).getJudul(),
                                responseSubTopics.body().get(iSubTopics).getHal_1(),
                                responseSubTopics.body().get(iSubTopics).getNo_sub_topik(),
                                responseSubTopics.body().get(iSubTopics).getJudul_sub_topik(),
                                responseSubTopics.body().get(iSubTopics).getHal(),
                                responseSubTopics.body().get(iSubTopics).getUri_img()
                        );

                        listSubTopics.add(dataSubTopics);
                    }

                    subtopikShimmer.stopShimmer();
                    subtopikShimmer.setVisibility(View.GONE);

                    layoutManager = new LinearLayoutManager(PengantarSubTopics.this, LinearLayoutManager.VERTICAL, false);
                    subTopicsRecycler.setLayoutManager(layoutManager);
                    adapterSubTopics = new PengantarSubTopik(listSubTopics, PengantarSubTopics.this);
                    subTopicsRecycler.setAdapter(adapterSubTopics);
                    subTopicsRecycler.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ModelDataSubTpcPengantar>> callSubTopics, Throwable t) {
                startActivity(new Intent(getApplicationContext(), FailureActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();

        kriteria = "WHERE pengantar_sub_topik.no_pengantar = " + sharedPreferences.getString("NoPengantar","");

        if (sharedPreferences.getString("Downloaded","").equals("True")) {
            loadSubTopik();
        } else {
            loadData();
        }
    }
}
