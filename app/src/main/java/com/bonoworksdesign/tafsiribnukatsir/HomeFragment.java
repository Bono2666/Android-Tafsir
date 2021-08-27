package com.bonoworksdesign.tafsiribnukatsir;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterJilid;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterLastRead;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterPengantar;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterSurat;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterSuratDialog;
import com.bonoworksdesign.tafsiribnukatsir.api.APIService;
import com.bonoworksdesign.tafsiribnukatsir.database.DataLastRead;
import com.bonoworksdesign.tafsiribnukatsir.database.DataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataJilid;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataLastRead;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataPengantar;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSurat;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    RelativeLayout search_btn, juzFilter, suratFilter, listSuratBtn, listLastReadBtn;
    RecyclerView jilidRecyclerView, pengantarRecyclerView, suratRecyclerView, lastReadRecycler, suratDialogRecycler;
    LinearLayout noLastReadLayout, lastReadLayout, failureLayout;
    TextView juzTxtFilter, suratTxtFilter, cariTxt, refreshPage;
    ShimmerFrameLayout jilidShimmer, pengantarShimmer, suratShimmer, suratDialogShimmer;
    ScrollView scrollView;
    WebView pengantarSubTitleWeb;

    AdapterJilid adapterJilid;
    AdapterPengantar adapterPengantar;
    AdapterSurat adapterSurat;
    AdapterSuratDialog adapterSuratDialog;
    AdapterLastRead adapterLastRead;
    ArrayList<ModelDataJilid> listJilid = new ArrayList<ModelDataJilid>();
    ArrayList<ModelDataPengantar> listPengantar = new ArrayList<ModelDataPengantar>();
    ArrayList<ModelDataSurat> listSurat = new ArrayList<ModelDataSurat>();
    ArrayList<ModelDataSurat> listSuratDialog = new ArrayList<>();
    ArrayList<ModelDataLastRead> listLastRead = new ArrayList<>();
    RecyclerView.LayoutManager jilidLayoutManager, pengantarLayoutManager, suratLayoutManager, lastReadLayoutManager;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AlertDialog dialog;

    String kriteriaJuz, kriteriaSurat, kriteriaSuratDialog, kriteria;
    String kriteriaLastRead = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        search_btn = view.findViewById(R.id.search_button);
        cariTxt = view.findViewById(R.id.cari_text);
        juzFilter = view.findViewById(R.id.juz_filter);
        juzTxtFilter = view.findViewById(R.id.juz_text_filter);
        suratFilter = view.findViewById(R.id.surat_filter);
        suratTxtFilter = view.findViewById(R.id.surat_text_filter);
        jilidRecyclerView = view.findViewById(R.id.jilid_recyclerview);
        pengantarRecyclerView = view.findViewById(R.id.pengantar_recyclerview);
        suratRecyclerView = view.findViewById(R.id.surat_recyclerview);
        lastReadRecycler = view.findViewById(R.id.lastread_recyclerview);
        noLastReadLayout = view.findViewById(R.id.no_lastread_layout);
        lastReadLayout = view.findViewById(R.id.lastread_Layout);
        listSuratBtn = view.findViewById(R.id.btn_surat_all);
        listLastReadBtn = view.findViewById(R.id.listLastread_btn);
        refreshPage = view.findViewById(R.id.refresh_page);
        jilidShimmer = view.findViewById(R.id.jilid_shimmer);
        pengantarShimmer = view.findViewById(R.id.pengantar_shimmer);
        suratShimmer = view.findViewById(R.id.surat_shimmer);
        scrollView = view.findViewById(R.id.scrollView);
        failureLayout = view.findViewById(R.id.failure_layout);
        pengantarSubTitleWeb = view.findViewById(R.id.pengantar_subtitle_web);

        pengantarSubTitleWeb.loadDataWithBaseURL("file:///android_res/","<html> <style type=\"text/css\"> " +
                "@font-face { font-family: 'Uthmanic';src: url('font/kfgqpc_arabic_symbols.otf'); } " +
                "@font-face { font-family: 'Ubuntu';src: url('font/ubuntu.ttf'); } " +
                "body { margin-left: 0px;margin-right: 0px;font-family: 'Ubuntu';font-size: 16px;line-height: 22px;color: #ffffff;" +
                "background-color: #222222 } " +
                "ar { font-family: 'Uthmanic';font-size: 24px;color: #ffffff;background-color: #222222 } " +
                "</style>" + "Biografi dan pengantar penulis (Ibnu Katsir <ar>\uF072</ar>)" + "</html>", "text/html", "utf-8", null);

        refreshPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failureLayout.setVisibility(View.GONE);
                search_btn.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                onResume();
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        editor.putString("NmSurat", "").apply();
        editor.putString("CariLastRead", "False").apply();
        editor.putString("Cari", "").apply();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("Screen", "Home").apply();
                getContext().startActivity(new Intent(getContext(), Search.class));
            }
        });

        juzFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder juzBuilder = new AlertDialog.Builder(getContext());
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
                        if (Integer.parseInt(juzEdt.getText().toString()) == 0) {
                            editor.putString("Juz", "").apply();
                        } else {
                            editor.putString("Juz", juzEdt.getText().toString()).apply();
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

                AlertDialog.Builder suratBuilder = new AlertDialog.Builder(getContext());
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
                            loadSuratDialog();
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
                    loadSuratDialog();
                }
            }
        });

        listSuratBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("NoBuku", "").apply();

                getContext().startActivity(new Intent(getContext(), ListSurat.class));
                ((Activity) getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        listLastReadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), ListLastRead.class));
                ((Activity) getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        onResume();
        return view;
    }

    private void loadSuratDialogLocal() {
        DataTafsir surat = new DataTafsir(getActivity());
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

        linearLayoutManager = new LinearLayoutManager(getContext());
        suratDialogRecycler.setLayoutManager(linearLayoutManager);
        adapterSuratDialog = new AdapterSuratDialog(listSuratDialog, getContext());
        suratDialogRecycler.setAdapter(adapterSuratDialog);
        suratDialogRecycler.setVisibility(View.VISIBLE);

        suratDialogRecycler.addItemDecoration(
                new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation()) {
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

    private void loadSuratDialog() {
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

                    linearLayoutManager = new LinearLayoutManager(getContext());
                    suratDialogRecycler.setLayoutManager(linearLayoutManager);
                    adapterSuratDialog = new AdapterSuratDialog(listSuratDialog, getContext());
                    suratDialogRecycler.setAdapter(adapterSuratDialog);
                    suratDialogRecycler.setVisibility(View.VISIBLE);

                    suratDialogRecycler.addItemDecoration(
                            new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation()) {
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
                search_btn.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                failureLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadLastRead() {
        DataTafsir lastRead = new DataTafsir(getActivity());
        Cursor data = lastRead.getData(kriteriaLastRead);

        if (data.getCount() > 0) {

            data.moveToLast();
            listLastRead.clear();
            int i = 1;
            boolean s = false;

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
                i++;

                if (i<=data.getCount() && i<=8) {
                    data.moveToPrevious();
                } else {
                    s = true;
                }

            } while (!s);

            lastReadLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            lastReadRecycler.setLayoutManager(lastReadLayoutManager);
            adapterLastRead = new AdapterLastRead(listLastRead,getContext());
            lastReadRecycler.setAdapter(adapterLastRead);

            noLastReadLayout.setVisibility(View.GONE);
            lastReadLayout.setVisibility(View.VISIBLE);

        } else {
            noLastReadLayout.setVisibility(View.VISIBLE);
            lastReadLayout.setVisibility(View.GONE);
        }
    }

    private void loadJilid() {
        DataTafsir jilid = new DataTafsir(getActivity());
        Cursor data = jilid.getDataJilid(kriteria);
        listJilid.clear();

        while (data.moveToNext()) {
            ModelDataJilid dataJilid = new ModelDataJilid(
                    data.getString(0),
                    data.getString(1),
                    data.getString(2)
            );

            listJilid.add(dataJilid);
        }

        jilidShimmer.stopShimmer();
        jilidShimmer.setVisibility(View.GONE);

        jilidLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        jilidRecyclerView.setLayoutManager(jilidLayoutManager);
        adapterJilid = new AdapterJilid(listJilid,getContext());
        jilidRecyclerView.setAdapter(adapterJilid);
        jilidRecyclerView.setVisibility(View.VISIBLE);
    }

    private void loadPengantar() {
        DataTafsir pengantar = new DataTafsir(getActivity());
        Cursor data = pengantar.getDataPengantar();
        listPengantar.clear();

        while (data.moveToNext()) {
            ModelDataPengantar dataPengantar = new ModelDataPengantar(
                    data.getString(0),
                    data.getString(1),
                    data.getString(2),
                    data.getString(3),
                    data.getString(4)
            );

            listPengantar.add(dataPengantar);
        }

        pengantarShimmer.stopShimmer();
        pengantarShimmer.setVisibility(View.GONE);

        pengantarLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        pengantarRecyclerView.setLayoutManager(pengantarLayoutManager);
        adapterPengantar = new AdapterPengantar(listPengantar,getContext());
        pengantarRecyclerView.setAdapter(adapterPengantar);
        pengantarRecyclerView.setVisibility(View.VISIBLE);
    }

    private void loadSurat() {
        DataTafsir surat = new DataTafsir(getActivity());
        Cursor data = surat.getDataSurat(kriteria);
        listSurat.clear();

        int jmlRec;

        if (data.getCount() > 4) {
            jmlRec = 4;
        } else {
            jmlRec = data.getCount();
        }

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

        suratLayoutManager = new GridLayoutManager(getContext(),2);
        suratRecyclerView.setLayoutManager(suratLayoutManager);
        adapterSurat = new AdapterSurat(listSurat, getContext());
        suratRecyclerView.setAdapter(adapterSurat);
        suratRecyclerView.setVisibility(View.VISIBLE);
    }

    public void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataJilid>> callJilid = api.getDataJilid(kriteria);
        callJilid.enqueue(new Callback<List<ModelDataJilid>>() {
            @Override
            public void onResponse(Call<List<ModelDataJilid>> callJilid, Response<List<ModelDataJilid>> responseJilid) {

                listJilid.clear();

                if (responseJilid.isSuccessful()) {
                    int jmlRecJilid = responseJilid.body().size();

                    for (int iJilid = 0; iJilid < jmlRecJilid; iJilid++) {
                        ModelDataJilid dataJilid = new ModelDataJilid(
                                responseJilid.body().get(iJilid).getNo_buku(),
                                responseJilid.body().get(iJilid).getJilidBuku(),
                                responseJilid.body().get(iJilid).getUriImg()
                        );

                        listJilid.add(dataJilid);
                    }

                    jilidShimmer.stopShimmer();
                    jilidShimmer.setVisibility(View.GONE);

                    jilidLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    jilidRecyclerView.setLayoutManager(jilidLayoutManager);
                    adapterJilid = new AdapterJilid(listJilid, getContext());
                    jilidRecyclerView.setAdapter(adapterJilid);
                    jilidRecyclerView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<ModelDataJilid>> callJilid, Throwable t) {
                search_btn.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                failureLayout.setVisibility(View.VISIBLE);
            }
        });

        Call<List<ModelDataPengantar>> callPengantar = api.getDataPengantar();
        callPengantar.enqueue(new Callback<List<ModelDataPengantar>>() {
            @Override
            public void onResponse(Call<List<ModelDataPengantar>> callPengantar, Response<List<ModelDataPengantar>> responsePengantar) {

                listPengantar.clear();

                if (responsePengantar.isSuccessful()) {
                    int jmlRecPengantar = responsePengantar.body().size();

                    for (int iPengantar = 0; iPengantar < jmlRecPengantar; iPengantar++) {
                        ModelDataPengantar dataPengantar = new ModelDataPengantar(
                                responsePengantar.body().get(iPengantar).getNoPengantar(),
                                responsePengantar.body().get(iPengantar).getJudul(),
                                responsePengantar.body().get(iPengantar).getHal_1(),
                                responsePengantar.body().get(iPengantar).getFooter(),
                                responsePengantar.body().get(iPengantar).getUriImg()
                        );

                        listPengantar.add(dataPengantar);
                    }

                    pengantarShimmer.stopShimmer();
                    pengantarShimmer.setVisibility(View.GONE);

                    pengantarLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    pengantarRecyclerView.setLayoutManager(pengantarLayoutManager);
                    adapterPengantar = new AdapterPengantar(listPengantar, getContext());
                    pengantarRecyclerView.setAdapter(adapterPengantar);
                    pengantarRecyclerView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<ModelDataPengantar>> callPengantar, Throwable t) {
                search_btn.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                failureLayout.setVisibility(View.VISIBLE);
            }
        });

        Call<List<ModelDataSurat>> callSurat = api.getDataSurat(kriteria);
        callSurat.enqueue(new Callback<List<ModelDataSurat>>() {
            @Override
            public void onResponse(Call<List<ModelDataSurat>> callSurat, Response<List<ModelDataSurat>> responseSurat) {

                listSurat.clear();

                if (responseSurat.isSuccessful()) {
                    int jmlRecSurat;

                    if (responseSurat.body().size() > 4) {
                        jmlRecSurat = 4;
                    } else {
                        jmlRecSurat = responseSurat.body().size();
                    }

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

                    suratLayoutManager = new GridLayoutManager(getContext(),2);
                    suratRecyclerView.setLayoutManager(suratLayoutManager);
                    adapterSurat = new AdapterSurat(listSurat, getContext());
                    suratRecyclerView.setAdapter(adapterSurat);
                    suratRecyclerView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<ModelDataSurat>> call, Throwable t) {
                search_btn.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                failureLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        String nm = sharedPreferences.getString("TempNmSurat", "");
        String cari, kriteriaCari;
        String nmSurat;

        if (TextUtils.isEmpty(sharedPreferences.getString("NmSurat", ""))) {
            nmSurat = "";
        } else {
            nmSurat = sharedPreferences.getString("NmSurat", "").substring(6);
        }

        if (TextUtils.isEmpty(sharedPreferences.getString("Cari", "")) ||
                sharedPreferences.getString("Cari", "").equals("Jilid " +
                        sharedPreferences.getString("NoBuku", "")) ||
                sharedPreferences.getString("Cari", "").equals("Surat") ||
                sharedPreferences.getString("Cari", "").equals("Terakhir dibaca") ||
                sharedPreferences.getString("Cari", "").equals(nmSurat)) {
            editor.putString("Cari", "").apply();
            cariTxt.setText("Cari");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cariTxt.setTextAppearance(R.style.AppTheme);
            }
        } else {
            cariTxt.setText(sharedPreferences.getString("Cari", ""));
            cariTxt.setTypeface(cariTxt.getTypeface(), Typeface.BOLD);
        }

        cari = sharedPreferences.getString("Cari", "").replace("'", "");

        if (cari.equals("")) {
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
                kriteriaLastRead = kriteriaJuz;
                suratFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                suratTxtFilter.setText("Surat");

            } else {
                if (!TextUtils.isEmpty(sharedPreferences.getString("Juz", ""))) {
                    kriteriaJuz = "WHERE juz = " + sharedPreferences.getString("Juz", "");

                    kriteriaSurat = " AND tafsir.no_surat = " +
                            sharedPreferences.getString("NoSuratFilter", "");

                    kriteria = kriteriaJuz + kriteriaSurat;
                    kriteriaLastRead = kriteriaJuz + " AND no_surat = " + sharedPreferences.getString("NoSuratFilter", "");
                    juzFilter.setBackgroundResource(R.drawable.filter_background);
                    juzTxtFilter.setText("Juz " + sharedPreferences.getString("Juz", ""));
                } else {
                    kriteriaJuz = "";

                    kriteriaSurat = "WHERE tafsir.no_surat = " +
                            sharedPreferences.getString("NoSuratFilter", "");

                    kriteria = kriteriaSurat;
                    kriteriaLastRead = "WHERE no_surat = " + sharedPreferences.getString("NoSuratFilter", "");
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
                kriteriaLastRead = kriteriaJuz + kriteriaCari;
                suratFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                suratTxtFilter.setText("Surat");

            } else {
                kriteriaCari = " AND REPLACE(tafsir,'''','') LIKE '%" + cari + "%'";

                if (!TextUtils.isEmpty(sharedPreferences.getString("Juz", ""))) {
                    kriteriaJuz = "WHERE juz = " + sharedPreferences.getString("Juz", "");

                    kriteriaSurat = " AND tafsir.no_surat = " +
                            sharedPreferences.getString("NoSuratFilter", "");

                    kriteria = kriteriaJuz + kriteriaSurat + kriteriaCari;
                    kriteriaLastRead = kriteriaJuz + kriteriaCari + " AND no_surat = " +
                            sharedPreferences.getString("NoSuratFilter", "");
                    juzFilter.setBackgroundResource(R.drawable.filter_background);
                    juzTxtFilter.setText("Juz " + sharedPreferences.getString("Juz", ""));
                } else {
                    kriteriaJuz = "";

                    kriteriaSurat = "WHERE tafsir.no_surat = " +
                            sharedPreferences.getString("NoSuratFilter", "");

                    kriteria = kriteriaSurat + kriteriaCari;
                    kriteriaLastRead = "WHERE no_surat = " + sharedPreferences.getString("NoSuratFilter", "") +
                            kriteriaCari;
                    juzFilter.setBackgroundResource(R.drawable.stroke_radius_24);
                    juzTxtFilter.setText("Juz");
                }

                kriteriaSuratDialog = "WHERE surat.no_surat <> " +
                        sharedPreferences.getString("NoSuratFilter", "");

                suratFilter.setBackgroundResource(R.drawable.filter_background);
                suratTxtFilter.setText(nm);
            }
        }

        if (sharedPreferences.getString("Downloaded","").equals("True")) {
            loadJilid();
            loadPengantar();
            loadSurat();
        } else {
            loadData();
        }
        loadLastRead();
    }
}
