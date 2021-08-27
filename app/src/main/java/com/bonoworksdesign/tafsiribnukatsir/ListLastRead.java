package com.bonoworksdesign.tafsiribnukatsir;

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

import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterListLastRead;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterSuratDialog;
import com.bonoworksdesign.tafsiribnukatsir.api.APIService;
import com.bonoworksdesign.tafsiribnukatsir.database.DataLastRead;
import com.bonoworksdesign.tafsiribnukatsir.database.DataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataLastRead;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSurat;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListLastRead extends AppCompatActivity {

    RelativeLayout search_btn, juzFilter, suratFilter;
    TextView juzTxtFilter, suratTxtFilter, cariTxt;
    RecyclerView lastreadRecycler, suratDialogRecycler;
    ImageView backBtn;
    ShimmerFrameLayout suratDialogShimmer;

    String kriteriaJuz, kriteriaSurat, kriteriaSuratDialog;
    String kriteriaLastRead = "";

    AdapterListLastRead adapterListLastRead;
    AdapterSuratDialog adapterSuratDialog;
    ArrayList<ModelDataLastRead> listLastRead = new ArrayList<>();
    ArrayList<ModelDataSurat> listSuratDialog = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lastread_list);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        search_btn = (RelativeLayout) findViewById(R.id.search_button);
        cariTxt = (TextView) findViewById(R.id.cari_text);
        juzFilter = (RelativeLayout) findViewById(R.id.juz_filter);
        juzTxtFilter = (TextView) findViewById(R.id.juz_text_filter);
        suratFilter = (RelativeLayout) findViewById(R.id.surat_filter);
        suratTxtFilter = (TextView) findViewById(R.id.surat_text_filter);
        lastreadRecycler = (RecyclerView) findViewById(R.id.lastread_recycler);
        backBtn = (ImageView) findViewById(R.id.back_button);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ListLastRead.this);
        editor = sharedPreferences.edit();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("Cari", cariTxt.getText().toString()).apply();

                startActivity(new Intent(ListLastRead.this, Search.class));
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

                AlertDialog.Builder juzBuilder = new AlertDialog.Builder(ListLastRead.this);
                v = getLayoutInflater().inflate(R.layout.juz_dialog, null);
                juzBuilder.setView(v);
                final AlertDialog juzDialog = juzBuilder.create();
                juzDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                juzDialog.show();

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
                        juzDialog.dismiss();
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
                        if (Integer.parseInt(juzEdt.getText().toString()) == 0) {
                            if (TextUtils.isEmpty(kriteriaSurat)) {
                                kriteriaLastRead = "";
                            } else {
                                kriteriaLastRead = kriteriaSurat;
                            }
                            editor.putString("Juz", "").apply();
                            juzFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                            juzTxtFilter.setText("Juz");

                        } else {
                            kriteriaJuz = "WHERE juz = " + juzEdt.getText().toString();
                            editor.putString("Juz", juzEdt.getText().toString()).apply();
                            juzFilter.setBackgroundResource(R.drawable.filter_background);
                            juzTxtFilter.setText("Juz " + juzEdt.getText().toString());
                            if (TextUtils.isEmpty(kriteriaSurat)) {
                                kriteriaLastRead = kriteriaJuz;
                            } else {
                                kriteriaLastRead = kriteriaJuz + kriteriaSurat;
                            }
                        }
                        juzDialog.dismiss();
                        loadLastRead();
                    }
                });
            }
        });

        suratFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder suratBuilder = new AlertDialog.Builder(ListLastRead.this);
                v = getLayoutInflater().inflate(R.layout.surat_dialog, null);
                suratBuilder.setView(v);
                final AlertDialog suratDialog = suratBuilder.create();
                suratDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                suratDialog.show();

                suratDialogRecycler = v.findViewById(R.id.surat_recyclerview);
                suratDialogShimmer = v.findViewById(R.id.surat_dialog_shimmer);

                ImageView closeBtn = v.findViewById(R.id.close_btn);
                TextView hapusBtn = v.findViewById(R.id.hapus_btn);
                TextView selectedTxt = v.findViewById(R.id.selected_text);
                RelativeLayout simpanBtn = v.findViewById(R.id.simpan_btn);
                final RelativeLayout selectedSurat = v.findViewById(R.id.selected_surat);

                if (!TextUtils.isEmpty(sharedPreferences.getString("NoSuratFilter", ""))) {
                    selectedSurat.setVisibility(View.VISIBLE);
                    selectedTxt.setText(sharedPreferences.getString("NmSuratFilter", ""));
                }

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        suratDialog.dismiss();
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
                        loadSurat();
                    }
                });

                simpanBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String no = sharedPreferences.getString("TempNoSurat", "");
                        String nm = sharedPreferences.getString("TempNmSurat", "");
                        editor.putString("NoSuratFilter", no).apply();
                        editor.putString("NmSuratFilter", nm).apply();

                        suratDialog.dismiss();
                        onResume();
                    }
                });

                loadSurat();
            }
        });

        onResume();
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

                    linearLayoutManager = new LinearLayoutManager(ListLastRead.this);
                    suratDialogRecycler.setLayoutManager(linearLayoutManager);
                    adapterSuratDialog = new AdapterSuratDialog(listSuratDialog, ListLastRead.this);
                    suratDialogRecycler.setAdapter(adapterSuratDialog);
                    suratDialogRecycler.setVisibility(View.VISIBLE);

                    suratDialogRecycler.addItemDecoration(
                            new DividerItemDecoration(ListLastRead.this, linearLayoutManager.getOrientation()) {
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

            }
        });
    }

    public void loadLastRead() {
        DataTafsir lastRead = new DataTafsir(ListLastRead.this);
        Cursor data = lastRead.getData(kriteriaLastRead);

        listLastRead.clear();

        if (data.getCount() > 0) {
            data.moveToLast();

            do {
                ModelDataLastRead dataLastRead = new ModelDataLastRead(
                        data.getInt(0),
                        data.getInt(1),
                        data.getString(2),
                        data.getString(3),
                        data.getString(4),
                        data.getString(5),
                        data.getInt(6),
                        data.getString(7),
                        data.getString(8)
                );

                listLastRead.add(dataLastRead);

            } while (data.moveToPrevious());
        }

        layoutManager = new GridLayoutManager(ListLastRead.this,1);
        lastreadRecycler.setLayoutManager(layoutManager);
        adapterListLastRead = new AdapterListLastRead(listLastRead, ListLastRead.this);
        lastreadRecycler.setAdapter(adapterListLastRead);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        editor.putString("CariLastRead", "False").apply();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String nm = sharedPreferences.getString("TempNmSurat", "");
        String cari, kriteriaCari;

        editor.putString("CariLastRead", "True").apply();

        if (sharedPreferences.getString("Cari", "").equals("")) {
            editor.putString("Cari", "Terakhir dibaca").apply();
        }

        cariTxt.setText(sharedPreferences.getString("Cari", ""));
        cari = sharedPreferences.getString("Cari", "").replace("'","");

        if (cari.equals("Terakhir dibaca")) {
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

                kriteriaLastRead = kriteriaJuz;
                suratFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                suratTxtFilter.setText("Surat");

            } else {
                if (!TextUtils.isEmpty(sharedPreferences.getString("Juz", ""))) {
                    kriteriaJuz = "WHERE juz = " + sharedPreferences.getString("Juz", "");

                    kriteriaSurat = " AND no_surat = " + sharedPreferences.getString("NoSuratFilter", "");

                    kriteriaLastRead = kriteriaJuz + kriteriaSurat;
                    juzFilter.setBackgroundResource(R.drawable.filter_background);
                    juzTxtFilter.setText("Juz " + sharedPreferences.getString("Juz", ""));
                } else {
                    kriteriaJuz = "";

                    kriteriaSurat = "WHERE no_surat = " + sharedPreferences.getString("NoSuratFilter", "");

                    kriteriaLastRead = kriteriaSurat;
                    juzFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                    juzTxtFilter.setText("Juz");
                }

                kriteriaSuratDialog = "WHERE surat.no_surat <> " + sharedPreferences.getString("NoSuratFilter", "");

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

                kriteriaLastRead = kriteriaJuz + kriteriaCari;
                suratFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                suratTxtFilter.setText("Surat");

            } else {
                kriteriaCari = " AND REPLACE(tafsir,'''','') LIKE '%" + cari + "%'";

                if (!TextUtils.isEmpty(sharedPreferences.getString("Juz", ""))) {
                    kriteriaJuz = "WHERE juz = " + sharedPreferences.getString("Juz", "");

                    kriteriaSurat = " AND no_surat = " + sharedPreferences.getString("NoSuratFilter", "");

                    kriteriaLastRead = kriteriaJuz + kriteriaSurat + kriteriaCari;
                    juzFilter.setBackgroundResource(R.drawable.filter_background);
                    juzTxtFilter.setText("Juz " + sharedPreferences.getString("Juz", ""));
                } else {
                    kriteriaJuz = "";

                    kriteriaSurat = "WHERE no_surat = " + sharedPreferences.getString("NoSuratFilter", "");

                    kriteriaLastRead = kriteriaSurat + kriteriaCari;
                    juzFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                    juzTxtFilter.setText("Juz");
                }

                kriteriaSuratDialog = "WHERE surat.no_surat <> " + sharedPreferences.getString("NoSuratFilter", "");

                suratFilter.setBackgroundResource(R.drawable.filter_background);
                suratTxtFilter.setText(nm);
            }
        }

        loadLastRead();
    }
}
