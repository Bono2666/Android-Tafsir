package com.bonoworksdesign.tafsiribnukatsir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterSuratDialog;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterTafsir;
import com.bonoworksdesign.tafsiribnukatsir.api.APIService;
import com.bonoworksdesign.tafsiribnukatsir.database.DataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSurat;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataTafsir;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewTafsir extends AppCompatActivity {

    RecyclerView tafsirRecycler;
    RelativeLayout tafsirLayout;
    ShimmerFrameLayout tafsirShimmer;

    ArrayList<ModelDataTafsir> listTafsir = new ArrayList<ModelDataTafsir>();
    AdapterTafsir adapterTafsir;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    SnapHelper snapHelper = new LinearSnapHelper();

    String kriteria;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tafsir_view);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        tafsirRecycler = (RecyclerView) findViewById(R.id.tafsir_recycler);
        tafsirLayout = (RelativeLayout) findViewById(R.id.tafsir_layout);
        tafsirShimmer = (ShimmerFrameLayout) findViewById(R.id.tafsir_shimmer);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getString("ScreenOn", "").equals("True")) {
            tafsirLayout.setKeepScreenOn(true);
        } else {
            tafsirLayout.setKeepScreenOn(false);
        }

        onResume();
    }

    private void loadTafsir() {
        DataTafsir tafsir = new DataTafsir(ViewTafsir.this);
        Cursor data = tafsir.getDataTafsir(kriteria);
        listTafsir.clear();

        int jmlRec = data.getCount();

        data.moveToFirst();
        for (int i = 0; i < jmlRec; i++) {
            ModelDataTafsir dataTafsir = new ModelDataTafsir(
                    data.getString(0),
                    data.getString(1),
                    data.getString(2),
                    data.getString(3),
                    data.getString(4),
                    data.getInt(5),
                    data.getString(6),
                    data.getString(7),
                    data.getString(8),
                    data.getString(9),
                    data.getString(10),
                    data.getString(11),
                    data.getString(12),
                    data.getString(13)
            );

            listTafsir.add(dataTafsir);
            data.moveToNext();
        }

        tafsirShimmer.stopShimmer();
        tafsirShimmer.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(ViewTafsir.this, LinearLayoutManager.HORIZONTAL, false);
        tafsirRecycler.setLayoutManager(layoutManager);
        adapterTafsir = new AdapterTafsir(listTafsir,ViewTafsir.this);
        tafsirRecycler.setAdapter(adapterTafsir);
        tafsirRecycler.scrollToPosition(sharedPreferences.getInt("Pos", 0));
        tafsirRecycler.setVisibility(View.VISIBLE);

        snapHelper.attachToRecyclerView(tafsirRecycler);
    }

    public void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataTafsir>> call = api.getDataTafsir(kriteria);
        call.enqueue(new Callback<List<ModelDataTafsir>>() {
            @Override
            public void onResponse(Call<List<ModelDataTafsir>> call, Response<List<ModelDataTafsir>> response) {

                listTafsir.clear();

                if (response.isSuccessful()) {
                    int jmlRec = response.body().size();

                    for (int i = 0; i < jmlRec; i++) {
                        ModelDataTafsir dataTafsir = new ModelDataTafsir(
                                response.body().get(i).getNo_surat(),
                                response.body().get(i).getArabic(),
                                response.body().get(i).getNm_surat(),
                                response.body().get(i).getNo_buku(),
                                response.body().get(i).getPeriode(),
                                response.body().get(i).getHal_1(),
                                response.body().get(i).getSurat_img(),
                                response.body().get(i).getJuz(),
                                response.body().get(i).getNo_sub_topik(),
                                response.body().get(i).getHal(),
                                response.body().get(i).getTafsir(),
                                response.body().get(i).getJuz_arabic(),
                                response.body().get(i).getUri_img(),
                                response.body().get(i).getJudul()
                        );

                        listTafsir.add(dataTafsir);
                    }

                    tafsirShimmer.stopShimmer();
                    tafsirShimmer.setVisibility(View.GONE);

                    layoutManager = new LinearLayoutManager(ViewTafsir.this, LinearLayoutManager.HORIZONTAL, false);
                    tafsirRecycler.setLayoutManager(layoutManager);
                    adapterTafsir = new AdapterTafsir(listTafsir,ViewTafsir.this);
                    tafsirRecycler.setAdapter(adapterTafsir);
                    tafsirRecycler.scrollToPosition(sharedPreferences.getInt("Pos", 0));
                    tafsirRecycler.setVisibility(View.VISIBLE);

                    snapHelper.attachToRecyclerView(tafsirRecycler);
                }
            }

            @Override
            public void onFailure(Call<List<ModelDataTafsir>> call, Throwable t) {
                startActivity(new Intent(getApplicationContext(), FailureActivity.class));
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();

        kriteria = "WHERE tafsir.no_surat = " + sharedPreferences.getString("NoSurat","");

        if (sharedPreferences.getString("Downloaded","").equals("True")) {
            loadTafsir();
        } else {
            loadData();
        }
    }
}
