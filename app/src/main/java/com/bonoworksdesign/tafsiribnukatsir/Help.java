package com.bonoworksdesign.tafsiribnukatsir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterBantuan;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterBookmark;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterSubTopics;
import com.bonoworksdesign.tafsiribnukatsir.api.APIService;
import com.bonoworksdesign.tafsiribnukatsir.database.DataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataBantuan;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataBookmark;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSubTopics;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Help extends AppCompatActivity {

    RecyclerView questionsRecycler;
    TextView cariTxt;
    ImageView backBtn;
    RelativeLayout searchLayout;
    ShimmerFrameLayout questionShimmer;

    String cariBantuan = "";
    String kriteria = "";

    AdapterBantuan adapterBantuan;
    ArrayList<ModelDataBantuan> listBantuan = new ArrayList<>();
    LinearLayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        questionsRecycler = (RecyclerView) findViewById(R.id.question_recyclerview);
        backBtn = (ImageView) findViewById(R.id.back_button);
        searchLayout = (RelativeLayout) findViewById(R.id.search_button);
        cariTxt = (TextView) findViewById(R.id.cari_text);
        questionShimmer = (ShimmerFrameLayout) findViewById(R.id.question_shimmer);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Help.this);
        editor = sharedPreferences.edit();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Help.this, SearchHelp.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        onResume();
    }

    private void loadBantuan() {
        DataTafsir bantuan = new DataTafsir(Help.this);
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

        questionShimmer.stopShimmer();
        questionShimmer.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(Help.this);
        questionsRecycler.setLayoutManager(layoutManager);
        adapterBantuan = new AdapterBantuan(listBantuan, Help.this);
        questionsRecycler.setAdapter(adapterBantuan);
        questionsRecycler.setVisibility(View.VISIBLE);
    }

    public void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataBantuan>> call = api.getDataBantuan(kriteria);;
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

                    questionShimmer.stopShimmer();
                    questionShimmer.setVisibility(View.GONE);

                    layoutManager = new LinearLayoutManager(Help.this);
                    questionsRecycler.setLayoutManager(layoutManager);
                    adapterBantuan = new AdapterBantuan(listBantuan, Help.this);
                    questionsRecycler.setAdapter(adapterBantuan);
                    questionsRecycler.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<ModelDataBantuan>> call, Throwable t) {
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

        if (TextUtils.isEmpty(sharedPreferences.getString("CariBantuan", ""))) {
            editor.putString("CariBantuan", "").apply();
            cariTxt.setText("Cari bantuan");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cariTxt.setTextAppearance(R.style.AppTheme);
            }
        } else {
            cariTxt.setText(sharedPreferences.getString("CariBantuan", ""));
            cariTxt.setTypeface(cariTxt.getTypeface(), Typeface.BOLD);
        }

        cariBantuan = sharedPreferences.getString("CariBantuan", "").replace("'", "");

        if (!cariBantuan.equals("")) {
            kriteria = "WHERE pertanyaan LIKE '%" + cariBantuan + "%' OR jawaban LIKE '%" + cariBantuan + "%'";
        } else {
            kriteria = "";
        }

        if (sharedPreferences.getString("Downloaded","").equals("True")) {
            loadBantuan();
        } else {
            loadData();
        }
    }
}
