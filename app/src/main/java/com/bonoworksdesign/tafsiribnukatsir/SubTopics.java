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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterJilid;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterSubTopics;
import com.bonoworksdesign.tafsiribnukatsir.api.APIService;
import com.bonoworksdesign.tafsiribnukatsir.database.DataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataJilid;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSubTopics;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubTopics extends AppCompatActivity {

    RecyclerView subTopicsRecycler;
    TextView mNmSurat, cariTxt;
    ImageView backBtn;
    RelativeLayout searchLayout;
    ShimmerFrameLayout subtopikShimmer;

    String cari = "";
    String kriteria;

    AdapterSubTopics adapterSubTopics;
    ArrayList<ModelDataSubTopics> listSubTopics = new ArrayList<ModelDataSubTopics>();
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.subtopics_list);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SubTopics.this);
        editor = sharedPreferences.edit();

        mNmSurat = (TextView) findViewById(R.id.judul_surat);
        subTopicsRecycler = (RecyclerView) findViewById(R.id.subtopics_recyclerview);
        backBtn = (ImageView) findViewById(R.id.back_button);
        searchLayout = (RelativeLayout) findViewById(R.id.search_button);
        cariTxt = (TextView) findViewById(R.id.cari_text);
        subtopikShimmer = (ShimmerFrameLayout) findViewById(R.id.subtopik_shimmer);

        mNmSurat.setText(sharedPreferences.getString("NmSurat", ""));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("Screen", "Sub Topik").apply();
                startActivity(new Intent(SubTopics.this, Search.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        onResume();
    }

    private void loadSubTopik() {
        DataTafsir subtopik = new DataTafsir(SubTopics.this);
        Cursor data;
        listSubTopics.clear();

        if (!cari.equals(sharedPreferences.getString("NmSurat", "").substring(6))) {
            data = subtopik.getDataCari(cari, kriteria);
        } else {
            data = subtopik.getDataSubTopik(kriteria);
        }

        while (data.moveToNext()) {
            ModelDataSubTopics dataSubTopik = new ModelDataSubTopics(
                    data.getString(0),
                    data.getString(1),
                    data.getString(2),
                    data.getInt(3),
                    data.getString(4),
                    data.getString(5),
                    data.getString(6),
                    data.getString(7)
            );

            listSubTopics.add(dataSubTopik);
        }

        subtopikShimmer.stopShimmer();
        subtopikShimmer.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(SubTopics.this, LinearLayoutManager.VERTICAL, false);
        subTopicsRecycler.setLayoutManager(layoutManager);
        adapterSubTopics = new AdapterSubTopics(listSubTopics, SubTopics.this);
        subTopicsRecycler.setAdapter(adapterSubTopics);
        subTopicsRecycler.setVisibility(View.VISIBLE);
    }

    public void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);
        Call<List<ModelDataSubTopics>> callSubTopics;

        if (!cari.equals(sharedPreferences.getString("NmSurat", "").substring(6))) {
            callSubTopics = api.getDataCari(cari, kriteria);
        } else {
            callSubTopics = api.getDataSubTopics(kriteria);
        }

        callSubTopics.enqueue(new Callback<List<ModelDataSubTopics>>() {
            @Override
            public void onResponse(Call<List<ModelDataSubTopics>> callSubTopics, Response<List<ModelDataSubTopics>> responseSubTopics) {

                listSubTopics.clear();

                if (responseSubTopics.isSuccessful()) {
                    int jmlRecSubTopics = responseSubTopics.body().size();

                    for (int iSubTopics = 0; iSubTopics < jmlRecSubTopics; iSubTopics++) {
                        ModelDataSubTopics dataSubTopics = new ModelDataSubTopics(
                                responseSubTopics.body().get(iSubTopics).getNo_surat(),
                                responseSubTopics.body().get(iSubTopics).getArabic(),
                                responseSubTopics.body().get(iSubTopics).getNm_surat(),
                                responseSubTopics.body().get(iSubTopics).getHal_1(),
                                responseSubTopics.body().get(iSubTopics).getNo_sub_topik(),
                                responseSubTopics.body().get(iSubTopics).getJudul(),
                                responseSubTopics.body().get(iSubTopics).getHal(),
                                responseSubTopics.body().get(iSubTopics).getUri_img()
                        );

                        listSubTopics.add(dataSubTopics);
                    }

                    subtopikShimmer.stopShimmer();
                    subtopikShimmer.setVisibility(View.GONE);

                    layoutManager = new LinearLayoutManager(SubTopics.this, LinearLayoutManager.VERTICAL, false);
                    subTopicsRecycler.setLayoutManager(layoutManager);
                    adapterSubTopics = new AdapterSubTopics(listSubTopics, SubTopics.this);
                    subTopicsRecycler.setAdapter(adapterSubTopics);
                    subTopicsRecycler.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ModelDataSubTopics>> callSubTopics, Throwable t) {
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

        if (sharedPreferences.getString("Cari", "").equals("") ||
                sharedPreferences.getString("Cari", "").equals("Jilid " +
                sharedPreferences.getString("NoBuku", "")) ||
                sharedPreferences.getString("Cari", "").equals("Surat")) {
            editor.putString("Cari", sharedPreferences.getString("NmSurat", "").substring(6)).apply();
        }

        cariTxt.setText(sharedPreferences.getString("Cari", ""));
        cari = sharedPreferences.getString("Cari", "").replace("'","");
        kriteria = "WHERE sub_topik.no_surat = " + sharedPreferences.getString("NoSurat","");

        if (sharedPreferences.getString("Downloaded","").equals("True")) {
            loadSubTopik();
        } else {
            loadData();
        }
    }
}
