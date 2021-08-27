package com.bonoworksdesign.tafsiribnukatsir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterBantuan;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterSearch;
import com.bonoworksdesign.tafsiribnukatsir.api.APIService;
import com.bonoworksdesign.tafsiribnukatsir.database.DataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataBantuan;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSubTopics;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchHelp extends AppCompatActivity {

    ArrayList<ModelDataBantuan> listBantuan = new ArrayList<ModelDataBantuan>();
    LinearLayoutManager layoutManager;
    AdapterBantuan adapterBantuan;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String kriteria;

    ImageView closeBtn;
    EditText edtSearch;
    RecyclerView searchRecycler;
    ShimmerFrameLayout searchShimmer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.search_help);

        closeBtn = (ImageView) findViewById(R.id.close_btn);
        edtSearch = (EditText) findViewById(R.id.edt_search);
        searchRecycler = (RecyclerView) findViewById(R.id.search_recyclerview);
        searchShimmer = (ShimmerFrameLayout) findViewById(R.id.search_shimmer);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SearchHelp.this);
        editor = sharedPreferences.edit();

        edtSearch.setText(sharedPreferences.getString("CariBantuan", ""));

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(edtSearch.getText().toString())) {
                    searchRecycler.setVisibility(View.INVISIBLE);
                    searchShimmer.stopShimmer();
                    searchShimmer.setVisibility(View.GONE);
                    editor.putString("CariBantuan", "").apply();
                } else {
                    searchRecycler.setVisibility(View.VISIBLE);

                    kriteria = "WHERE pertanyaan LIKE '%" + edtSearch.getText().toString() + "%' OR jawaban LIKE '%" +
                            edtSearch.getText().toString() + "%'";

                    if (sharedPreferences.getString("Downloaded","").equals("True")) {
                        loadCari();
                    } else {
                        searchShimmer.startShimmer();
                        searchShimmer.setVisibility(View.VISIBLE);
                        searchRecycler.setVisibility(View.GONE);
                        loadData();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    editor.putString("CariBantuan", edtSearch.getText().toString()).apply();
                    finish();
                    return true;
                }

                return false;
            }
        });

        searchRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v = SearchHelp.this.getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private void loadCari() {
        DataTafsir bantuan = new DataTafsir(SearchHelp.this);
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

        searchShimmer.stopShimmer();
        searchShimmer.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(SearchHelp.this);
        searchRecycler.setLayoutManager(layoutManager);
        adapterBantuan = new AdapterBantuan(listBantuan, SearchHelp.this);
        searchRecycler.setAdapter(adapterBantuan);
        searchRecycler.setVisibility(View.VISIBLE);
    }

    private void loadData() {
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

                    searchShimmer.stopShimmer();
                    searchShimmer.setVisibility(View.GONE);

                    layoutManager = new LinearLayoutManager(SearchHelp.this);
                    searchRecycler.setLayoutManager(layoutManager);
                    adapterBantuan = new AdapterBantuan(listBantuan, SearchHelp.this);
                    searchRecycler.setAdapter(adapterBantuan);
                    searchRecycler.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<ModelDataBantuan>> callSubTopics, Throwable t) {
                startActivity(new Intent(getApplicationContext(), FailureActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        kriteria = "WHERE pertanyaan LIKE '%" + edtSearch.getText().toString() + "%' OR jawaban LIKE '%" +
                edtSearch.getText().toString() + "%'";

        if (!edtSearch.getText().toString().equals("")) {
            if (sharedPreferences.getString("Downloaded","").equals("True")) {
                loadCari();
            } else {
                searchShimmer.startShimmer();
                searchShimmer.setVisibility(View.VISIBLE);
                searchRecycler.setVisibility(View.GONE);
                loadData();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (edtSearch.getText().toString().equals("")) {
            editor.putString("CariBantuan", "").apply();
        }
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
