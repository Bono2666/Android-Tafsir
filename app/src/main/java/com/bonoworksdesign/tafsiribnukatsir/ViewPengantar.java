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
import android.widget.RelativeLayout;

import com.bonoworksdesign.tafsiribnukatsir.adapter.IsiPengantar;
import com.bonoworksdesign.tafsiribnukatsir.api.APIService;
import com.bonoworksdesign.tafsiribnukatsir.database.DataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelIsiPengantar;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewPengantar extends AppCompatActivity {

    RecyclerView pengantarRecycler;
    RelativeLayout pengantarLayout;
    ShimmerFrameLayout pengantarShimmer;

    ArrayList<ModelIsiPengantar> listPengantar = new ArrayList<ModelIsiPengantar>();
    IsiPengantar adapterIsi;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    SnapHelper snapHelper = new LinearSnapHelper();

    String kriteria;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pengantar_view);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        pengantarRecycler = (RecyclerView) findViewById(R.id.pengantar_recycler);
        pengantarLayout = (RelativeLayout) findViewById(R.id.pengantar_layout);
        pengantarShimmer = (ShimmerFrameLayout) findViewById(R.id.pengantar_shimmer);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getString("ScreenOn", "").equals("True")) {
            pengantarLayout.setKeepScreenOn(true);
        } else {
            pengantarLayout.setKeepScreenOn(false);
        }

        onResume();
    }

    private void loadPengantar() {
        DataTafsir isipengantar = new DataTafsir(ViewPengantar.this);
        Cursor data = isipengantar.getIsiPengantar(kriteria);
        listPengantar.clear();

        int jmlRec = data.getCount();

        data.moveToFirst();
        for (int i = 0; i < jmlRec; i++) {
            ModelIsiPengantar dataIsi = new ModelIsiPengantar(
                    data.getString(0),
                    data.getInt(1),
                    data.getString(2),
                    data.getString(3),
                    data.getString(4),
                    data.getString(5),
                    data.getString(6)
            );

            listPengantar.add(dataIsi);
            data.moveToNext();
        }

        pengantarShimmer.stopShimmer();
        pengantarShimmer.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(ViewPengantar.this, LinearLayoutManager.HORIZONTAL, false);
        pengantarRecycler.setLayoutManager(layoutManager);
        adapterIsi = new IsiPengantar(listPengantar, ViewPengantar.this);
        pengantarRecycler.setAdapter(adapterIsi);
        pengantarRecycler.scrollToPosition(sharedPreferences.getInt("Pos", 0));
        pengantarRecycler.setVisibility(View.VISIBLE);

        snapHelper.attachToRecyclerView(pengantarRecycler);
    }

    public void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelIsiPengantar>> call = api.getIsiPengantar(kriteria);
        call.enqueue(new Callback<List<ModelIsiPengantar>>() {
            @Override
            public void onResponse(Call<List<ModelIsiPengantar>> call, Response<List<ModelIsiPengantar>> response) {

                listPengantar.clear();

                if (response.isSuccessful()) {
                    int jmlRec = response.body().size();

                    for (int i = 0; i < jmlRec; i++) {
                        ModelIsiPengantar dataTafsir = new ModelIsiPengantar(
                                response.body().get(i).getNo_pengantar(),
                                response.body().get(i).getHal_1(),
                                response.body().get(i).getNo_sub_topik(),
                                response.body().get(i).getHal(),
                                response.body().get(i).getIsi(),
                                response.body().get(i).getFooter(),
                                response.body().get(i).getUri_img()
                        );

                        listPengantar.add(dataTafsir);
                    }

                    pengantarShimmer.stopShimmer();
                    pengantarShimmer.setVisibility(View.GONE);

                    layoutManager = new LinearLayoutManager(ViewPengantar.this, LinearLayoutManager.HORIZONTAL, false);
                    pengantarRecycler.setLayoutManager(layoutManager);
                    adapterIsi = new IsiPengantar(listPengantar, ViewPengantar.this);
                    pengantarRecycler.setAdapter(adapterIsi);
                    pengantarRecycler.scrollToPosition(sharedPreferences.getInt("Pos", 0));
                    pengantarRecycler.setVisibility(View.VISIBLE);

                    snapHelper.attachToRecyclerView(pengantarRecycler);
                }
            }

            @Override
            public void onFailure(Call<List<ModelIsiPengantar>> call, Throwable t) {
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

        kriteria = "WHERE isi_pengantar.no_pengantar = " + sharedPreferences.getString("NoPengantar","");

        if (sharedPreferences.getString("Downloaded","").equals("True")) {
            loadPengantar();
        } else {
            loadData();
        }
    }
}
