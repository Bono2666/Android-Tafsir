package com.bonoworksdesign.tafsiribnukatsir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterBantuan;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterTafsir;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterViewBantuan;
import com.bonoworksdesign.tafsiribnukatsir.api.APIService;
import com.bonoworksdesign.tafsiribnukatsir.database.DataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataBantuan;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewHelp extends AppCompatActivity {

    ArrayList<ModelDataBantuan> listBantuan = new ArrayList<>();
    AdapterViewBantuan adapterViewBantuan;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    String kriteria = "";

    RecyclerView helpRecycler;
    ShimmerFrameLayout helpShimmer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help_view);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        helpRecycler = (RecyclerView) findViewById(R.id.help_recycler);
        helpShimmer = (ShimmerFrameLayout) findViewById(R.id.help_shimmer);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ViewHelp.this);

        onResume();
    }

    private void loadBantuan() {
        DataTafsir bantuan = new DataTafsir(ViewHelp.this);
        Cursor data = bantuan.getDataBantuan(kriteria);
        listBantuan.clear();

        while (data.moveToNext()) {
            ModelDataBantuan dataBantuan = new ModelDataBantuan(
                    data.getString(0),
                    data.getString(1),
                    data.getString(2)
            );

            listBantuan.add(dataBantuan);
        }

        helpShimmer.stopShimmer();
        helpShimmer.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(ViewHelp.this);
        helpRecycler.setLayoutManager(layoutManager);
        adapterViewBantuan = new AdapterViewBantuan(listBantuan, ViewHelp.this);
        helpRecycler.setAdapter(adapterViewBantuan);
        helpRecycler.setVisibility(View.VISIBLE);
    }

    public void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataBantuan>> call = api.getDataBantuan(kriteria);
        call.enqueue(new Callback<List<ModelDataBantuan>>() {
            @Override
            public void onResponse(Call<List<ModelDataBantuan>> call, Response<List<ModelDataBantuan>> response) {

                listBantuan.clear();

                if (response.isSuccessful()) {
                    int jmlRec = response.body().size();

                    for (int i = 0; i < jmlRec; i++) {
                        ModelDataBantuan data = new ModelDataBantuan(
                                response.body().get(i).getId_bantuan(),
                                response.body().get(i).getPertanyaan(),
                                response.body().get(i).getJawaban()
                        );

                        listBantuan.add(data);
                    }

                    helpShimmer.stopShimmer();
                    helpShimmer.setVisibility(View.GONE);

                    layoutManager = new LinearLayoutManager(ViewHelp.this, LinearLayoutManager.HORIZONTAL, false);
                    helpRecycler.setLayoutManager(layoutManager);
                    adapterViewBantuan = new AdapterViewBantuan(listBantuan, ViewHelp.this);
                    helpRecycler.setAdapter(adapterViewBantuan);
                    helpRecycler.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ModelDataBantuan>> call, Throwable t) {
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

        kriteria = "WHERE id_bantuan = " + sharedPreferences.getString("IdBantuan", "");

        if (sharedPreferences.getString("Downloaded","").equals("True")) {
            loadBantuan();
        } else {
            loadData();
        }
    }
}
