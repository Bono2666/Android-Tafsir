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

import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterSearch;
import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterSearchPengantar;
import com.bonoworksdesign.tafsiribnukatsir.api.APIService;
import com.bonoworksdesign.tafsiribnukatsir.database.DataTafsir;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSubTopics;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataSubTpcPengantar;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchPengantar extends AppCompatActivity {

    ArrayList<ModelDataSubTpcPengantar> listCari = new ArrayList<ModelDataSubTpcPengantar>();
    RecyclerView.LayoutManager layoutManager;
    AdapterSearchPengantar adapterSearch;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String no = "";

    TextView cancelBtn;
    ImageView cancelIcon;
    EditText edtSearch;
    RecyclerView searchRecycler;
    ShimmerFrameLayout searchShimmer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.search);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SearchPengantar.this);
        editor = sharedPreferences.edit();

        cancelBtn = (TextView) findViewById(R.id.batal_btn);
        cancelIcon = (ImageView) findViewById(R.id.cancel_icon);
        edtSearch = (EditText) findViewById(R.id.edt_search);
        searchRecycler = (RecyclerView) findViewById(R.id.search_recyclerview);
        searchShimmer = (ShimmerFrameLayout) findViewById(R.id.search_shimmer);

        no = sharedPreferences.getString("NoPengantar","");

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(edtSearch.getText().toString())) {
                    cancelIcon.setVisibility(View.INVISIBLE);
                    searchRecycler.setVisibility(View.INVISIBLE);
                    searchShimmer.stopShimmer();
                    searchShimmer.setVisibility(View.GONE);
                } else {
                    cancelIcon.setVisibility(View.VISIBLE);
                    searchRecycler.setVisibility(View.VISIBLE);

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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    cancelBtn.setFocusableInTouchMode(true);
                    cancelBtn.requestFocus();
                    return true;
                }
                return false;
            }
        });

        cancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                cancelIcon.setVisibility(View.INVISIBLE);
                editor.putString("CariPengantar", "").apply();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v = SearchPengantar.this.getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private void loadCari() {
        DataTafsir subTopik = new DataTafsir(SearchPengantar.this);
        Cursor data = subTopik.getDataCariPengantar(edtSearch.getText().toString(), no);
        listCari.clear();

        while (data.moveToNext()) {
            ModelDataSubTpcPengantar dataSubTopik = new ModelDataSubTpcPengantar(
                    data.getString(0),
                    data.getString(1),
                    data.getInt(3),
                    data.getString(4),
                    data.getString(5),
                    data.getString(6),
                    data.getString(7)
            );

            listCari.add(dataSubTopik);
        }

        searchShimmer.stopShimmer();
        searchShimmer.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(SearchPengantar.this);
        searchRecycler.setLayoutManager(layoutManager);
        adapterSearch = new AdapterSearchPengantar(listCari, SearchPengantar.this);
        searchRecycler.setAdapter(adapterSearch);
        searchRecycler.setVisibility(View.VISIBLE);
    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.rootUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);

        Call<List<ModelDataSubTpcPengantar>> call = api.getDataCariPengantar(edtSearch.getText().toString(), no);
        call.enqueue(new Callback<List<ModelDataSubTpcPengantar>>() {
            @Override
            public void onResponse(Call<List<ModelDataSubTpcPengantar>> call, Response<List<ModelDataSubTpcPengantar>> response) {

                listCari.clear();

                if (response.isSuccessful()) {
                    int jmlRecSubTopics = response.body().size();

                    for (int iSubTopics = 0; iSubTopics < jmlRecSubTopics; iSubTopics++) {
                        ModelDataSubTpcPengantar dataSubTopics = new ModelDataSubTpcPengantar(
                                response.body().get(iSubTopics).getNo_pengantar(),
                                response.body().get(iSubTopics).getJudul(),
                                response.body().get(iSubTopics).getHal_1(),
                                response.body().get(iSubTopics).getNo_sub_topik(),
                                response.body().get(iSubTopics).getJudul_sub_topik(),
                                response.body().get(iSubTopics).getHal(),
                                response.body().get(iSubTopics).getUri_img()
                        );

                        listCari.add(dataSubTopics);
                    }

                    searchShimmer.stopShimmer();
                    searchShimmer.setVisibility(View.GONE);

                    layoutManager = new LinearLayoutManager(SearchPengantar.this);
                    searchRecycler.setLayoutManager(layoutManager);
                    adapterSearch = new AdapterSearchPengantar(listCari, SearchPengantar.this);
                    searchRecycler.setAdapter(adapterSearch);
                    searchRecycler.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<ModelDataSubTpcPengantar>> call, Throwable t) {
                startActivity(new Intent(getApplicationContext(), FailureActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!edtSearch.getText().toString().equals("")) {
            cancelIcon.setVisibility(View.VISIBLE);

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
        finish();
    }
}
