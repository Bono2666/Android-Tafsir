package com.bonoworksdesign.tafsiribnukatsir;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterSurat;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterSuratDialog;
import com.bonoworksdesign.tafsiribnukatsir.api.APIService;
import com.bonoworksdesign.tafsiribnukatsir.database.DataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSurat;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListSurat extends AppCompatActivity {

    RelativeLayout search_btn, juzFilter, suratFilter;
    RecyclerView suratRecyclerView, suratDialogRecycler;
    ImageView backBtn;
    TextView judulTxt, cariTxt, juzTxtFilter, suratTxtFilter;
    ShimmerFrameLayout suratShimmer, suratDialogShimmer;

    AdapterSurat adapterSurat;
    AdapterSuratDialog adapterSuratDialog;
    ArrayList<ModelDataSurat> listSurat = new ArrayList<>();
    ArrayList<ModelDataSurat> listSuratDialog = new ArrayList<>();
    RecyclerView.LayoutManager suratLayoutManager;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AlertDialog dialog;

    String kriteria, kriteriaSurat, kriteriaJuz, kriteriaSuratDialog, kriteriaBuku;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.surat_list);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ListSurat.this);
        editor = sharedPreferences.edit();

        search_btn = (RelativeLayout) findViewById(R.id.search_button);
        juzFilter = (RelativeLayout) findViewById(R.id.juz_filter);
        juzTxtFilter = (TextView) findViewById(R.id.juz_text_filter);
        suratFilter = (RelativeLayout) findViewById(R.id.surat_filter);
        suratTxtFilter = (TextView) findViewById(R.id.surat_text_filter);
        suratRecyclerView = (RecyclerView) findViewById(R.id.surat_recyclerview);
        backBtn = (ImageView) findViewById(R.id.back_button);
        judulTxt = (TextView) findViewById(R.id.judul_text);
        cariTxt = (TextView) findViewById(R.id.cari_text);
        suratShimmer = (ShimmerFrameLayout) findViewById(R.id.surat_shimmer);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListSurat.this, Search.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        juzFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder juzBuilder = new AlertDialog.Builder(ListSurat.this);
                v = getLayoutInflater().inflate(R.layout.juz_dialog, null);
                juzBuilder.setView(v);
                dialog = juzBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                final ImageView minBtn = v.findViewById(R.id.min_btn);
                final ImageView plusBtn = v.findViewById(R.id.plus_btn);
                final EditText juzEdt = v.findViewById(R.id.juz_edit);

                ImageView closeBtn = v.findViewById(R.id.close_btn);
                TextView hapusBtn = v.findViewById(R.id.hapus_btn);
                RelativeLayout simpanBtn = v.findViewById(R.id.simpan_btn);

                if (!TextUtils.isEmpty(sharedPreferences.getString("Juz", ""))) {
                    juzEdt.setText(sharedPreferences.getString("Juz", ""));
                }

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        onResume();
                    }
                });

                if (juzEdt.getText().toString().equals("0")) {
                    minBtn.setEnabled(false);
                    minBtn.setAlpha((float) 0.5);
                }

                minBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        juzEdt.setText(String.valueOf(Integer.parseInt(juzEdt.getText().toString()) - 1));
                        if (juzEdt.getText().toString().equals("0")) {
                            minBtn.setEnabled(false);
                            minBtn.setAlpha((float) 0.5);
                        }
                        if (Integer.parseInt(juzEdt.getText().toString()) < 30) {
                            plusBtn.setEnabled(true);
                            plusBtn.setAlpha((float) 1);
                        }
                    }
                });

                plusBtn.requestFocus();
                plusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        juzEdt.setText(String.valueOf(Integer.parseInt(juzEdt.getText().toString()) + 1));
                        if (juzEdt.getText().toString().equals("30")) {
                            plusBtn.setEnabled(false);
                            plusBtn.setAlpha((float) 0.5);
                        }
                        if (Integer.parseInt(juzEdt.getText().toString()) > 0) {
                            minBtn.setEnabled(true);
                            minBtn.setAlpha((float) 1);
                        }
                    }
                });

                hapusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        minBtn.setEnabled(false);
                        minBtn.setAlpha((float) 0.5);
                        juzEdt.setText("0");
                        editor.putString("Juz", "").apply();
                    }
                });

                simpanBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Integer.parseInt(juzEdt.getText().toString()) > 0) {
                            editor.putString("Juz", juzEdt.getText().toString()).apply();
                        } else {
                            editor.putString("Juz", "").apply();
                        }
                        onResume();
                        dialog.dismiss();
                    }
                });
            }
        });

        suratFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder suratBuilder = new AlertDialog.Builder(ListSurat.this);
                v = getLayoutInflater().inflate(R.layout.surat_dialog, null);
                suratBuilder.setView(v);
                dialog = suratBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                suratDialogRecycler = v.findViewById(R.id.surat_recyclerview);

                ImageView closeBtn = v.findViewById(R.id.close_btn);
                TextView hapusBtn = v.findViewById(R.id.hapus_btn);
                TextView selectedTxt = v.findViewById(R.id.selected_text);
                RelativeLayout simpanBtn = v.findViewById(R.id.simpan_btn);
                suratDialogShimmer = v.findViewById(R.id.surat_dialog_shimmer);
                final RelativeLayout selectedSurat = v.findViewById(R.id.selected_surat);

                if (!TextUtils.isEmpty(sharedPreferences.getString("NoSuratFilter", ""))) {
                    selectedSurat.setVisibility(View.VISIBLE);
                    selectedTxt.setText(sharedPreferences.getString("NmSuratFilter", ""));
                }

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        onResume();
                    }
                });

                hapusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor.putString("TempNoSurat", "").apply();
                        editor.putString("TempNmSurat", "").apply();
                        editor.putString("NoSuratFilter", "").apply();
                        editor.putString("NmSuratFilter", "").apply();

                        selectedSurat.setVisibility(View.GONE);
                        kriteriaSuratDialog = "";

                        if (sharedPreferences.getString("Downloaded","").equals("True")) {
                            loadSuratDialogLocal();
                        } else {
                            suratDialogShimmer.startShimmer();
                            suratDialogShimmer.setVisibility(View.VISIBLE);
                            suratDialogRecycler.setVisibility(View.GONE);
                            loadSurat();
                        }
                    }
                });

                simpanBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String no = sharedPreferences.getString("TempNoSurat", "");
                        String nm = sharedPreferences.getString("TempNmSurat", "");
                        editor.putString("NoSuratFilter", no).apply();
                        editor.putString("NmSuratFilter", nm).apply();

                        onResume();
                        dialog.dismiss();
                    }
                });

                if (sharedPreferences.getString("Downloaded","").equals("True")) {
                    loadSuratDialogLocal();
                } else {
                    loadSurat();
                }
            }
        });

        onResume();
    }

    private void loadSuratDialogLocal() {
        DataTafsir surat = new DataTafsir(ListSurat.this);
        Cursor data = surat.getDataSurat(kriteriaSuratDialog);
        listSuratDialog.clear();

        int jmlRec = data.getCount();

        data.moveToFirst();
        for (int i = 0; i < jmlRec; i++) {
            ModelDataSurat dataSurat = new ModelDataSurat(
                    data.getString(0),
                    data.getString(1),
                    data.getString(2),
                    data.getString(3),
                    data.getString(4),
                    data.getString(5),
                    data.getString(6),
                    data.getString(7),
                    data.getString(8)
            );

            listSuratDialog.add(dataSurat);
            data.moveToNext();
        }

        suratDialogShimmer.stopShimmer();
        suratDialogShimmer.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(ListSurat.this);
        suratDialogRecycler.setLayoutManager(linearLayoutManager);
        adapterSuratDialog = new AdapterSuratDialog(listSuratDialog, ListSurat.this);
        suratDialogRecycler.setAdapter(adapterSuratDialog);
        suratDialogRecycler.setVisibility(View.VISIBLE);

        suratDialogRecycler.addItemDecoration(
                new DividerItemDecoration(ListSurat.this, linearLayoutManager.getOrientation()) {
                    @Override
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                        int position = parent.getChildAdapterPosition(view);

                        if (position == state.getItemCount() - 1) {
                            outRect.setEmpty();
                        } else {
                            super.getItemOffsets(outRect, view, parent, state);
                        }
                    }
                }
        );
    }

    private void loadSurat() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataSurat>> callSurat = api.getDataSurat(kriteriaSuratDialog);
        callSurat.enqueue(new Callback<List<ModelDataSurat>>() {
            @Override
            public void onResponse(Call<List<ModelDataSurat>> callSurat, Response<List<ModelDataSurat>> responseSurat) {

                listSuratDialog.clear();

                if (responseSurat.isSuccessful()) {
                    int jmlRecSurat = responseSurat.body().size();

                    for (int iSurat = 0; iSurat < jmlRecSurat; iSurat++) {
                        ModelDataSurat dataSurat = new ModelDataSurat(
                                responseSurat.body().get(iSurat).getNoSurat(),
                                responseSurat.body().get(iSurat).getArabic(),
                                responseSurat.body().get(iSurat).getNmSurat(),
                                responseSurat.body().get(iSurat).getJuzSurat(),
                                responseSurat.body().get(iSurat).getNoBuku(),
                                responseSurat.body().get(iSurat).getJmlAyat(),
                                responseSurat.body().get(iSurat).getPerTurun(),
                                responseSurat.body().get(iSurat).getHal_1(),
                                responseSurat.body().get(iSurat).getUriImg()
                        );

                        listSuratDialog.add(dataSurat);
                    }

                    suratDialogShimmer.stopShimmer();
                    suratDialogShimmer.setVisibility(View.GONE);

                    linearLayoutManager = new LinearLayoutManager(ListSurat.this);
                    suratDialogRecycler.setLayoutManager(linearLayoutManager);
                    adapterSuratDialog = new AdapterSuratDialog(listSuratDialog, ListSurat.this);
                    suratDialogRecycler.setAdapter(adapterSuratDialog);
                    suratDialogRecycler.setVisibility(View.VISIBLE);

                    suratDialogRecycler.addItemDecoration(
                            new DividerItemDecoration(ListSurat.this, linearLayoutManager.getOrientation()) {
                                @Override
                                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                    int position = parent.getChildAdapterPosition(view);

                                    if (position == state.getItemCount() - 1) {
                                        outRect.setEmpty();
                                    } else {
                                        super.getItemOffsets(outRect, view, parent, state);
                                    }
                                }
                            }
                    );

                }
            }

            @Override
            public void onFailure(Call<List<ModelDataSurat>> call, Throwable t) {
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), FailureActivity.class));
            }
        });
    }

    private void loadSuratLocal() {
        DataTafsir surat = new DataTafsir(ListSurat.this);
        Cursor data = surat.getDataSurat(kriteria);
        listSurat.clear();

        int jmlRec;

        jmlRec = data.getCount();

        data.moveToFirst();
        for (int i = 0; i < jmlRec; i++) {
            ModelDataSurat dataSurat = new ModelDataSurat(
                    data.getString(0),
                    data.getString(1),
                    data.getString(2),
                    data.getString(3),
                    data.getString(4),
                    data.getString(5),
                    data.getString(6),
                    data.getString(7),
                    data.getString(8)
            );

            listSurat.add(dataSurat);
            data.moveToNext();
        }

        suratShimmer.stopShimmer();
        suratShimmer.setVisibility(View.GONE);

        suratLayoutManager = new GridLayoutManager(ListSurat.this,2);
        suratRecyclerView.setLayoutManager(suratLayoutManager);
        adapterSurat = new AdapterSurat(listSurat, ListSurat.this);
        suratRecyclerView.setAdapter(adapterSurat);
        suratRecyclerView.setVisibility(View.VISIBLE);
    }

    public void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataSurat>> callSurat = api.getDataSurat(kriteria);
        callSurat.enqueue(new Callback<List<ModelDataSurat>>() {
            @Override
            public void onResponse(Call<List<ModelDataSurat>> callSurat, Response<List<ModelDataSurat>> responseSurat) {

                listSurat.clear();

                if (responseSurat.isSuccessful()) {
                    int jmlRecSurat = responseSurat.body().size();

                    for (int iSurat = 0; iSurat < jmlRecSurat; iSurat++) {
                        ModelDataSurat dataSurat = new ModelDataSurat(
                                responseSurat.body().get(iSurat).getNoSurat(),
                                responseSurat.body().get(iSurat).getArabic(),
                                responseSurat.body().get(iSurat).getNmSurat(),
                                responseSurat.body().get(iSurat).getJuzSurat(),
                                responseSurat.body().get(iSurat).getNoBuku(),
                                responseSurat.body().get(iSurat).getJmlAyat(),
                                responseSurat.body().get(iSurat).getPerTurun(),
                                responseSurat.body().get(iSurat).getHal_1(),
                                responseSurat.body().get(iSurat).getUriImg()
                        );

                        listSurat.add(dataSurat);
                    }

                    suratShimmer.stopShimmer();
                    suratShimmer.setVisibility(View.GONE);

                    suratLayoutManager = new GridLayoutManager(ListSurat.this,2);
                    suratRecyclerView.setLayoutManager(suratLayoutManager);
                    adapterSurat = new AdapterSurat(listSurat, ListSurat.this);
                    suratRecyclerView.setAdapter(adapterSurat);
                    suratRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ModelDataSurat>> call, Throwable t) {
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

        String nmsurat = "";
        String nm = sharedPreferences.getString("TempNmSurat", "");
        String cari, kriteriaCari;

        if (!TextUtils.isEmpty(sharedPreferences.getString("NmSurat", ""))) {
            nmsurat = sharedPreferences.getString("NmSurat", "").substring(6);
        }

        if (TextUtils.isEmpty(sharedPreferences.getString("NoBuku", ""))) {
            editor.putString("Screen", "Surat").apply();

            if (sharedPreferences.getString("Cari", "").equals("") ||
                    sharedPreferences.getString("Cari", "").equals(nmsurat)) {
                editor.putString("Cari", "Surat").apply();
            }

            cariTxt.setText(sharedPreferences.getString("Cari", ""));
            cari = sharedPreferences.getString("Cari", "").replace("'", "");

            if (cari.equals("Surat")) {
                if (TextUtils.isEmpty(sharedPreferences.getString("NoSuratFilter", ""))) {
                    if (TextUtils.isEmpty(sharedPreferences.getString("Juz", ""))) {
                        kriteriaJuz = "";
                        juzFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                        juzTxtFilter.setText("Juz");
                    } else {
                        kriteriaJuz = "WHERE juz = " + sharedPreferences.getString("Juz", "");
                        juzFilter.setBackgroundResource(R.drawable.filter_background);
                        juzTxtFilter.setText("Juz " + sharedPreferences.getString("Juz", ""));
                    }

                    kriteriaSuratDialog = "";
                    kriteriaSurat = "";

                    kriteria = kriteriaJuz;
                    suratFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                    suratTxtFilter.setText("Surat");

                } else {
                    if (!TextUtils.isEmpty(sharedPreferences.getString("Juz", ""))) {
                        kriteriaJuz = "WHERE juz = " + sharedPreferences.getString("Juz", "");

                        kriteriaSurat = " AND tafsir.no_surat = " +
                                sharedPreferences.getString("NoSuratFilter", "");

                        kriteria = kriteriaJuz + kriteriaSurat;
                        juzFilter.setBackgroundResource(R.drawable.filter_background);
                        juzTxtFilter.setText("Juz " + sharedPreferences.getString("Juz", ""));
                    } else {
                        kriteriaJuz = "";

                        kriteriaSurat = "WHERE tafsir.no_surat = " +
                                sharedPreferences.getString("NoSuratFilter", "");

                        kriteria = kriteriaSurat;
                        juzFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                        juzTxtFilter.setText("Juz");
                    }

                    kriteriaSuratDialog = "WHERE surat.no_surat <> " +
                            sharedPreferences.getString("NoSuratFilter", "");

                    suratFilter.setBackgroundResource(R.drawable.filter_background);
                    suratTxtFilter.setText(nm);
                }
            } else {
                if (TextUtils.isEmpty(sharedPreferences.getString("NoSuratFilter", ""))) {
                    if (TextUtils.isEmpty(sharedPreferences.getString("Juz", ""))) {
                        kriteriaJuz = "";
                        kriteriaCari = "WHERE REPLACE(tafsir,'''','') LIKE '%" + cari + "%'";
                        juzFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                        juzTxtFilter.setText("Juz");
                    } else {
                        kriteriaJuz = "WHERE juz = " + sharedPreferences.getString("Juz", "");
                        kriteriaCari = " AND REPLACE(tafsir,'''','') LIKE '%" + cari + "%'";
                        juzFilter.setBackgroundResource(R.drawable.filter_background);
                        juzTxtFilter.setText("Juz " + sharedPreferences.getString("Juz", ""));
                    }

                    kriteriaSuratDialog = "";
                    kriteriaSurat = "";

                    kriteria = kriteriaJuz + kriteriaCari;
                    suratFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                    suratTxtFilter.setText("Surat");

                } else {

                    kriteriaCari = " AND REPLACE(tafsir,'''','') LIKE '%" + cari + "%'";

                    if (!TextUtils.isEmpty(sharedPreferences.getString("Juz", ""))) {
                        kriteriaJuz = "WHERE juz = " + sharedPreferences.getString("Juz", "");

                        kriteriaSurat = " AND tafsir.no_surat = " +
                                sharedPreferences.getString("NoSuratFilter", "");

                        kriteria = kriteriaJuz + kriteriaSurat + kriteriaCari;
                        juzFilter.setBackgroundResource(R.drawable.filter_background);
                        juzTxtFilter.setText("Juz " + sharedPreferences.getString("Juz", ""));
                    } else {
                        kriteriaJuz = "";

                        kriteriaSurat = "WHERE tafsir.no_surat = " +
                                sharedPreferences.getString("NoSuratFilter", "");

                        kriteria = kriteriaSurat + kriteriaCari;
                        juzFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                        juzTxtFilter.setText("Juz");
                    }

                    kriteriaSuratDialog = "WHERE surat.no_surat <> " +
                            sharedPreferences.getString("NoSuratFilter", "");

                    suratFilter.setBackgroundResource(R.drawable.filter_background);
                    suratTxtFilter.setText(nm);
                }
            }
        } else {
            editor.putString("Screen", "Jilid").apply();

            if (sharedPreferences.getString("Cari", "").equals("") ||
                    sharedPreferences.getString("Cari", "").equals(nmsurat)) {
                cariTxt.setText("Jilid " + sharedPreferences.getString("NoBuku", ""));
                editor.putString("Cari", "Jilid " + sharedPreferences.getString("NoBuku", "")).apply();
            }

            cariTxt.setText(sharedPreferences.getString("Cari", ""));
            judulTxt.setText("Pilih tafsir di buku jilid " + sharedPreferences.getString("Jilid", "").toLowerCase());

            cari = sharedPreferences.getString("Cari", "").replace("'", "");
            kriteriaBuku = " AND no_buku = " + sharedPreferences.getString("NoBuku", "");

            if (cari.equals("Jilid " + sharedPreferences.getString("NoBuku", ""))) {
                if (TextUtils.isEmpty(sharedPreferences.getString("NoSuratFilter", ""))) {
                    if (TextUtils.isEmpty(sharedPreferences.getString("Juz", ""))) {
                        kriteriaJuz = "";
                        juzFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                        juzTxtFilter.setText("Juz");
                    } else {
                        kriteriaJuz = "WHERE juz = " + sharedPreferences.getString("Juz", "");
                        juzFilter.setBackgroundResource(R.drawable.filter_background);
                        juzTxtFilter.setText("Juz " + sharedPreferences.getString("Juz", ""));
                    }

                    kriteriaSuratDialog = "";
                    kriteriaSurat = "";

                    kriteria = kriteriaJuz + kriteriaBuku;
                    suratFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                    suratTxtFilter.setText("Surat");

                } else {
                    if (!TextUtils.isEmpty(sharedPreferences.getString("Juz", ""))) {
                        kriteriaJuz = "WHERE juz = " + sharedPreferences.getString("Juz", "");

                        kriteriaSurat = " AND tafsir.no_surat = " +
                                sharedPreferences.getString("NoSuratFilter", "");

                        kriteria = kriteriaJuz + kriteriaSurat + kriteriaBuku;
                        juzFilter.setBackgroundResource(R.drawable.filter_background);
                        juzTxtFilter.setText("Juz " + sharedPreferences.getString("Juz", ""));
                    } else {
                        kriteriaJuz = "";

                        kriteriaSurat = "WHERE tafsir.no_surat = " +
                                sharedPreferences.getString("NoSuratFilter", "");

                        kriteria = kriteriaSurat + kriteriaBuku;
                        juzFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                        juzTxtFilter.setText("Juz");
                    }

                    kriteriaSuratDialog = "WHERE surat.no_surat <> " +
                            sharedPreferences.getString("NoSuratFilter", "");

                    suratFilter.setBackgroundResource(R.drawable.filter_background);
                    suratTxtFilter.setText(nm);
                }
            } else {
                if (TextUtils.isEmpty(sharedPreferences.getString("NoSuratFilter", ""))) {
                    if (TextUtils.isEmpty(sharedPreferences.getString("Juz", ""))) {
                        kriteriaJuz = "";
                        kriteriaCari = "WHERE REPLACE(tafsir,'''','') LIKE '%" + cari + "%'";
                        juzFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                        juzTxtFilter.setText("Juz");
                    } else {
                        kriteriaJuz = "WHERE juz = " + sharedPreferences.getString("Juz", "");
                        kriteriaCari = " AND REPLACE(tafsir,'''','') LIKE '%" + cari + "%'";
                        juzFilter.setBackgroundResource(R.drawable.filter_background);
                        juzTxtFilter.setText("Juz " + sharedPreferences.getString("Juz", ""));
                    }

                    kriteriaSuratDialog = "";
                    kriteriaSurat = "";

                    kriteria = kriteriaJuz + kriteriaCari + kriteriaBuku;

                    suratFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                    suratTxtFilter.setText("Surat");

                } else {
                    kriteriaCari = " AND REPLACE(tafsir,'''','') LIKE '%" + cari + "%'";

                    if (!TextUtils.isEmpty(sharedPreferences.getString("Juz", ""))) {
                        kriteriaJuz = "WHERE juz = " + sharedPreferences.getString("Juz", "");

                        kriteriaSurat = " AND tafsir.no_surat = " +
                                sharedPreferences.getString("NoSuratFilter", "");

                        kriteria = kriteriaJuz + kriteriaSurat + kriteriaBuku + kriteriaCari;
                        juzFilter.setBackgroundResource(R.drawable.filter_background);
                        juzTxtFilter.setText("Juz " + sharedPreferences.getString("Juz", ""));
                    } else {
                        kriteriaJuz = "";

                        kriteriaSurat = "WHERE tafsir.no_surat = " +
                                sharedPreferences.getString("NoSuratFilter", "");

                        kriteria = kriteriaSurat + kriteriaBuku + kriteriaCari;
                        juzFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                        juzTxtFilter.setText("Juz");
                    }

                    kriteriaSuratDialog = "WHERE surat.no_surat <> " +
                            sharedPreferences.getString("NoSuratFilter", "");

                    suratFilter.setBackgroundResource(R.drawable.filter_background);
                    suratTxtFilter.setText(nm);
                }
            }
        }

        if (sharedPreferences.getString("Downloaded","").equals("True")) {
            loadSuratLocal();
        } else {
            loadData();
        }
    }
}
