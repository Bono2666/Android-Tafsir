package com.bonoworksdesign.tafsiribnukatsir;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bonoworksdesign.tafsiribnukatsir.api.APIService;
import com.bonoworksdesign.tafsiribnukatsir.database.DataBookmark;
import com.bonoworksdesign.tafsiribnukatsir.database.DataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataBantuan;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataJilid;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataJuz;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataPengantar;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSubTopics;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSubTpcPengantar;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSurat;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelIsiPengantar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingFragment extends Fragment {

    RelativeLayout helpBtn, downloadBtn, okBtn, ligthSwitch;
    ProgressBar progressBar;
    TextView tableNm, currItem, totItem, downloadTxt, downloadDesc;
    LinearLayout downloadProgress, successLayout;
    ImageView disableTrack, disableIcon, enableTrack, enableIcon;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_setting, container, false);

        downloadBtn = (RelativeLayout) view.findViewById(R.id.download_btn);
        helpBtn = (RelativeLayout) view.findViewById(R.id.help_btn);
        downloadTxt = (TextView) view.findViewById(R.id.download_text);
        downloadDesc = (TextView) view.findViewById(R.id.download_desc);
        disableTrack = (ImageView) view.findViewById(R.id.disable_track);
        disableIcon = (ImageView) view.findViewById(R.id.disable_icon);
        enableTrack = (ImageView) view.findViewById(R.id.enable_track);
        enableIcon = (ImageView) view.findViewById(R.id.enable_icon);
        ligthSwitch = (RelativeLayout) view.findViewById(R.id.light_switch);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        if (sharedPreferences.getString("ScreenOn","").equals("True")) {
            enableTrack.setAlpha((float) 1.0);
            enableIcon.setAlpha((float) 1.0);
        } else {
            enableTrack.setAlpha((float) 0);
            enableIcon.setAlpha((float) 0);
        }

        ligthSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enableIcon.getAlpha() == 0.0) {
                    enableTrack.setAlpha((float) 1.0);
                    enableIcon.setAlpha((float) 1.0);
                    editor.putString("ScreenOn","True");
                    Snackbar.make(view, "Pengaturan cahaya diaktifkan", Snackbar.LENGTH_LONG).show();
                } else {
                    enableTrack.setAlpha((float) 0);
                    enableIcon.setAlpha((float) 0);
                    editor.putString("ScreenOn","False");
                    Snackbar.make(view, "Pengaturan cahaya dinonaktifkan", Snackbar.LENGTH_LONG).show();
                }
                editor.apply();
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                v = getLayoutInflater().inflate(R.layout.download_dialog, null);
                builder.setView(v);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                final ImageView closeBtn = v.findViewById(R.id.close_btn);
                final LinearLayout startLayout = v.findViewById(R.id.start_layout);
                final RelativeLayout mulaiBtn = v.findViewById(R.id.mulai_btn);
                final TextView successInfo = v.findViewById(R.id.success_info);
                downloadProgress = v.findViewById(R.id.download_progress);
                successLayout = v.findViewById(R.id.success_layout);
                okBtn = v.findViewById(R.id.ok_btn);
                tableNm = v.findViewById(R.id.table_nm);
                currItem = v.findViewById(R.id.current_item);
                totItem = v.findViewById(R.id.total_item);
                progressBar = v.findViewById(R.id.progress_bar);

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        onResume();
                    }
                });

                mulaiBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startLayout.setVisibility(View.GONE);
                        downloadProgress.setVisibility(View.VISIBLE);

                        downloadJuz();
                        downloadJilid();
                        downloadPengantar();
                        downloadPengantarST();
                        downloadIsiPengantar();
                        downloadSubTopik();
                        downloadSurat();
                        downloadBantuan();
                        downloadTafsir();
                        updateBookmark();
                        updateLastRead();
                    }
                });

                String s = "Sekarang anda dapat membuka tafsir langsung dari hp anda, tanpa menggunakan koneksi " +
                        "<font color=#4b7155>internet</font>.";
                successInfo.setText(Html.fromHtml(s));

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        onResume();
                    }
                });

            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), Help.class));
                ((Activity) getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        return view;
    }

    private void updateLastRead() {
        final DataTafsir lastRead = new DataTafsir(getActivity());
        final Cursor data = lastRead.getData("");
        final int jmlRec = data.getCount();

        tableNm.setText("Bacaan Terakhir ");
        totItem.setText(" dari " + jmlRec);
        progressBar.setMax(jmlRec);

        new Thread(new Runnable() {
            @Override
            public void run() {

                int i = 0;
                while (data.moveToNext()) {
                    SystemClock.sleep(50);

                    i++;

                    String nmFile = data.getString(5).replace("http:","Tafsir Ibnu Katsir");

                    lastRead.updateLastread(nmFile, data.getInt(0), data.getInt(1));

                    final int finalI = i;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            currItem.setText(String.valueOf(finalI + 1));
                            progressBar.setProgress(finalI + 1);
                        }
                    });
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        downloadProgress.setVisibility(View.GONE);
                        successLayout.setVisibility(View.VISIBLE);

                        editor.putString("Downloaded","True").apply();
                    }
                });
            }
        }).start();
    }

    private void updateBookmark() {
        final DataBookmark bookmark = new DataBookmark(getActivity());
        final Cursor data = bookmark.getData();
        final int jmlRec = data.getCount();

        tableNm.setText("Bookmark ");
        totItem.setText(" dari " + jmlRec);
        progressBar.setMax(jmlRec);

        new Thread(new Runnable() {
            @Override
            public void run() {

                int i = 0;
                while (data.moveToNext()) {
                    SystemClock.sleep(50);

                    i++;

                    String nmFile = data.getString(5).replace("http:","Tafsir Ibnu Katsir");

                    bookmark.updateBookmark(nmFile, data.getInt(0), data.getInt(1));

                    final int finalI = i;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            currItem.setText(String.valueOf(finalI + 1));
                            progressBar.setProgress(finalI + 1);
                        }
                    });
                }

            }
        }).start();
    }

    private void downloadTafsir() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataTafsir>> call = api.getDataTafsir("");
        call.enqueue(new Callback<List<ModelDataTafsir>>() {
            @Override
            public void onResponse(Call<List<ModelDataTafsir>> call, final Response<List<ModelDataTafsir>> response) {

                final DataTafsir dbTafsir = new DataTafsir(getContext());

                if (response.isSuccessful()) {
                    final int jmlRec = response.body().size();

                    tableNm.setText("Tafsir ");
                    totItem.setText(" dari " + jmlRec);
                    progressBar.setMax(jmlRec);
                    dbTafsir.removetafsir();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (int i = 0; i < jmlRec; i++) {
                                SystemClock.sleep(50);

                                dbTafsir.addtafsir(Integer.parseInt(response.body().get(i).getNo_surat()),
                                        Integer.parseInt(response.body().get(i).getHal()), response.body().get(i).getJuz(),
                                        Integer.parseInt(response.body().get(i).getNo_sub_topik()),
                                        response.body().get(i).getTafsir().replace("'","''"));

                                final int finalI = i;

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        currItem.setText(String.valueOf(finalI + 1));
                                        progressBar.setProgress(finalI + 1);
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<ModelDataTafsir>> call, Throwable t) {

            }
        });
    }

    private void downloadIsiPengantar() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelIsiPengantar>> call = api.getIsiPengantar("");
        call.enqueue(new Callback<List<ModelIsiPengantar>>() {
            @Override
            public void onResponse(Call<List<ModelIsiPengantar>> call, final Response<List<ModelIsiPengantar>> response) {

                final DataTafsir dbPengantar = new DataTafsir(getContext());

                if (response.isSuccessful()) {
                    final int jmlRec = response.body().size();

                    tableNm.setText("Isi Pengantar ");
                    totItem.setText(" dari " + jmlRec);
                    progressBar.setMax(jmlRec);
                    dbPengantar.removeisipengantar();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (int i = 0; i < jmlRec; i++) {
                                SystemClock.sleep(50);

                                dbPengantar.addisipengantar(Integer.parseInt(response.body().get(i).getNo_pengantar()),
                                        Integer.parseInt(response.body().get(i).getHal()),
                                        Integer.parseInt(response.body().get(i).getNo_sub_topik()),
                                        response.body().get(i).getIsi().replace("'","''"));

                                final int finalI = i;

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        currItem.setText(String.valueOf(finalI + 1));
                                        progressBar.setProgress(finalI + 1);
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<ModelIsiPengantar>> call, Throwable t) {

            }
        });
    }

    private void downloadBantuan() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataBantuan>> call = api.getDataBantuan("");
        call.enqueue(new Callback<List<ModelDataBantuan>>() {
            @Override
            public void onResponse(Call<List<ModelDataBantuan>> call, final Response<List<ModelDataBantuan>> response) {

                final DataTafsir dbTafsir = new DataTafsir(getContext());

                if (response.isSuccessful()) {
                    final int jmlRec = response.body().size();

                    tableNm.setText("Bantuan ");
                    totItem.setText(" dari " + jmlRec);
                    progressBar.setMax(jmlRec);
                    dbTafsir.removebantuan();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (int i = 0; i < jmlRec; i++) {
                                SystemClock.sleep(50);

                                dbTafsir.addbantuan(Integer.parseInt(response.body().get(i).getId_bantuan()),
                                        response.body().get(i).getPertanyaan().replace("'", "''"),
                                        response.body().get(i).getJawaban().replace("'","''"));

                                final int finalI = i;

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        currItem.setText(String.valueOf(finalI + 1));
                                        progressBar.setProgress(finalI + 1);
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<ModelDataBantuan>> call, Throwable t) {

            }
        });
    }

    private void downloadSurat() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataSurat>> call = api.getDataSurat("");
        call.enqueue(new Callback<List<ModelDataSurat>>() {
            @Override
            public void onResponse(Call<List<ModelDataSurat>> call, final Response<List<ModelDataSurat>> response) {

                final DataTafsir dbSurat = new DataTafsir(getContext());

                if (response.isSuccessful()) {
                    final int jmlRec = response.body().size();

                    tableNm.setText("Surat ");
                    totItem.setText(" dari " + jmlRec);
                    progressBar.setMax(jmlRec);
                    dbSurat.removesurat();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (int i = 0; i < jmlRec; i++) {
                                SystemClock.sleep(50);

                                int pos = 0;
                                String nmFile = response.body().get(i).getUriImg();

                                do {
                                    pos = nmFile.indexOf("/");
                                    if (pos > 0) {
                                        nmFile = nmFile.substring(pos);
                                    }
                                } while (pos > 0);

                                DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                Uri uri = Uri.parse(response.body().get(i).getUriImg());
                                DownloadManager.Request request = new DownloadManager.Request(uri);
//                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir("/Tafsir Ibnu Katsir", nmFile);
                                downloadManager.enqueue(request);

                                dbSurat.addsurat(Integer.parseInt(response.body().get(i).getNoSurat()), response.body().get(i).getArabic(),
                                        response.body().get(i).getNmSurat(), response.body().get(i).getNoBuku(),
                                        response.body().get(i).getJmlAyat(), response.body().get(i).getPerTurun(),
                                        Integer.parseInt(response.body().get(i).getHal_1()), "/Tafsir Ibnu Katsir" + nmFile);

                                final int finalI = i;

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        currItem.setText(String.valueOf(finalI + 1));
                                        progressBar.setProgress(finalI + 1);
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<ModelDataSurat>> call, Throwable t) {

            }
        });
    }

    private void downloadSubTopik() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataSubTopics>> call = api.getDataSubTopics("");
        call.enqueue(new Callback<List<ModelDataSubTopics>>() {
            @Override
            public void onResponse(Call<List<ModelDataSubTopics>> call, final Response<List<ModelDataSubTopics>> response) {

                final DataTafsir dbSubTopik = new DataTafsir(getContext());

                if (response.isSuccessful()) {
                    final int jmlRec = response.body().size();

                    tableNm.setText("Sub Topik ");
                    totItem.setText(" dari " + jmlRec);
                    progressBar.setMax(jmlRec);
                    dbSubTopik.removesub_topik();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (int i = 0; i < jmlRec; i++) {
                                SystemClock.sleep(50);

                                dbSubTopik.addsub_topik(Integer.parseInt(response.body().get(i).getNo_surat()),
                                        Integer.parseInt(response.body().get(i).getNo_sub_topik()),
                                        response.body().get(i).getJudul().replace("'","''"),
                                        Integer.parseInt(response.body().get(i).getHal()));

                                final int finalI = i;

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        currItem.setText(String.valueOf(finalI + 1));
                                        progressBar.setProgress(finalI + 1);
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<ModelDataSubTopics>> call, Throwable t) {

            }
        });
    }

    private void downloadPengantarST() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataSubTpcPengantar>> call = api.getDataSubTpcPengantar("");
        call.enqueue(new Callback<List<ModelDataSubTpcPengantar>>() {
            @Override
            public void onResponse(Call<List<ModelDataSubTpcPengantar>> call, final Response<List<ModelDataSubTpcPengantar>> response) {

                final DataTafsir dbSubTopik = new DataTafsir(getContext());

                if (response.isSuccessful()) {
                    final int jmlRec = response.body().size();

                    tableNm.setText("Sub Topik Pengantar ");
                    totItem.setText(" dari " + jmlRec);
                    progressBar.setMax(jmlRec);
                    dbSubTopik.removepengantarst();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (int i = 0; i < jmlRec; i++) {
                                SystemClock.sleep(50);

                                dbSubTopik.addpengantarst(Integer.parseInt(response.body().get(i).getNo_pengantar()),
                                        Integer.parseInt(response.body().get(i).getNo_sub_topik()),
                                        response.body().get(i).getJudul().replace("'","''"),
                                        Integer.parseInt(response.body().get(i).getHal()));

                                final int finalI = i;

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        currItem.setText(String.valueOf(finalI + 1));
                                        progressBar.setProgress(finalI + 1);
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<ModelDataSubTpcPengantar>> call, Throwable t) {

            }
        });
    }

    private void downloadJuz() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataJuz>> call = api.getDataJuz();
        call.enqueue(new Callback<List<ModelDataJuz>>() {
            @Override
            public void onResponse(Call<List<ModelDataJuz>> call, final Response<List<ModelDataJuz>> response) {

                final DataTafsir dbJuz = new DataTafsir(getContext());

                if (response.isSuccessful()) {
                    final int jmlRec = response.body().size();

                    tableNm.setText("Juz ");
                    totItem.setText(" dari " + jmlRec);
                    progressBar.setMax(jmlRec);
                    dbJuz.removejuz();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (int i = 0; i < jmlRec; i++) {
                                SystemClock.sleep(50);

                                dbJuz.addjuz(response.body().get(i).getJuz(),
                                        response.body().get(i).getJuz_arabic(), response.body().get(i).getUriImg());

                                final int finalI = i;

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        currItem.setText(String.valueOf(finalI + 1));
                                        progressBar.setProgress(finalI + 1);
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<ModelDataJuz>> call, Throwable t) {

            }
        });
    }

    private void downloadJilid() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataJilid>> call = api.getDataJilid("");
        call.enqueue(new Callback<List<ModelDataJilid>>() {
            @Override
            public void onResponse(Call<List<ModelDataJilid>> call, final Response<List<ModelDataJilid>> response) {

                final DataTafsir dbJilid = new DataTafsir(getContext());

                if (response.isSuccessful()) {
                    final int jmlRec = response.body().size();

                    tableNm.setText("Jilid ");
                    totItem.setText(" dari " + jmlRec);
                    progressBar.setMax(jmlRec);
                    dbJilid.removejilid();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (int i = 0; i < jmlRec; i++) {
                                SystemClock.sleep(50);

                                int pos = 0;
                                String nmFile = response.body().get(i).getUriImg();

                                do {
                                    pos = nmFile.indexOf("/");
                                    if (pos > 0) {
                                        nmFile = nmFile.substring(pos);
                                    }
                                } while (pos > 0);

                                DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                Uri uri = Uri.parse(response.body().get(i).getUriImg());
                                DownloadManager.Request request = new DownloadManager.Request(uri);
//                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir("/Tafsir Ibnu Katsir", nmFile);
                                downloadManager.enqueue(request);

                                dbJilid.addjilid(response.body().get(i).getNo_buku(),
                                        response.body().get(i).getJilidBuku(), "/Tafsir Ibnu Katsir" + nmFile.substring(1));

                                final int finalI = i;

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        currItem.setText(String.valueOf(finalI + 1));
                                        progressBar.setProgress(finalI + 1);
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<ModelDataJilid>> call, Throwable t) {

            }
        });
    }

    private void downloadPengantar() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataPengantar>> call = api.getDataPengantar();
        call.enqueue(new Callback<List<ModelDataPengantar>>() {
            @Override
            public void onResponse(Call<List<ModelDataPengantar>> call, final Response<List<ModelDataPengantar>> response) {

                final DataTafsir dbPengantar = new DataTafsir(getContext());

                if (response.isSuccessful()) {
                    final int jmlRec = response.body().size();

                    tableNm.setText("Pengantar ");
                    totItem.setText(" dari " + jmlRec);
                    progressBar.setMax(jmlRec);
                    dbPengantar.removejilid();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (int i = 0; i < jmlRec; i++) {
                                SystemClock.sleep(50);

                                int pos = 0;
                                String nmFile = response.body().get(i).getUriImg();

                                do {
                                    pos = nmFile.indexOf("/");
                                    if (pos > 0) {
                                        nmFile = nmFile.substring(pos);
                                    }
                                } while (pos > 0);

                                DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                Uri uri = Uri.parse(response.body().get(i).getUriImg());
                                DownloadManager.Request request = new DownloadManager.Request(uri);
//                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir("/Tafsir Ibnu Katsir", nmFile);
                                downloadManager.enqueue(request);

                                dbPengantar.addpengantar(Integer.parseInt(response.body().get(i).getNoPengantar()),
                                        response.body().get(i).getJudul(), Integer.parseInt(response.body().get(i).getHal_1()),
                                        response.body().get(i).getFooter(), "/Tafsir Ibnu Katsir" + nmFile.substring(1));

                                final int finalI = i;

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        currItem.setText(String.valueOf(finalI + 1));
                                        progressBar.setProgress(finalI + 1);
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<ModelDataPengantar>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sharedPreferences.getString("Downloaded","").equals("True")) {
            downloadTxt.setText(Html.fromHtml("Download <b><sup><small><small><font color=#4b7155>100% terdownload" +
                    "</font></small></small></sup></b>"));
            downloadDesc.setText("Seluruh bacaan tafsir telah berhasil tersimpan di hp anda, sekarang anda tidak membutuhkan koneksi" +
                    " internet untuk membukanya.");
        }
    }
}
