package com.bonoworksdesign.tafsiribnukatsir.api;

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
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("browse_jilid.php")
    Call<List<ModelDataJilid>> getDataJilid(@Query("kriteria") String kriteria);

    @GET("filter_cari.php")
    Call<List<ModelDataSubTopics>> getDataCari(@Query("cari") String cari, @Query("kriteria") String kriteria);

    @GET("cari_pengantar.php")
    Call<List<ModelDataSubTpcPengantar>> getDataCariPengantar(@Query("cari") String cari, @Query("no") String no_pengantar);

    @GET("browse_surat.php")
    Call<List<ModelDataSurat>> getDataSurat(@Query("kriteria") String kriteria);

    @GET("browse_pengantar.php")
    Call<List<ModelDataPengantar>> getDataPengantar();

    @GET("browse_sub_topics.php")
    Call<List<ModelDataSubTopics>> getDataSubTopics(@Query("kriteria") String kriteria);

    @GET("br_pengantar_sub_topics.php")
    Call<List<ModelDataSubTpcPengantar>> getDataSubTpcPengantar(@Query("kriteria") String kriteria);

    @GET("browse_tafsir.php")
    Call<List<ModelDataTafsir>> getDataTafsir(@Query("kriteria") String kriteria);

    @GET("get_isi_pengantar.php")
    Call<List<ModelIsiPengantar>> getIsiPengantar(@Query("kriteria") String kriteria);

    @GET("get_bantuan.php")
    Call<List<ModelDataBantuan>> getDataBantuan(@Query("kriteria") String kriteria);

    @GET("get_juz.php")
    Call<List<ModelDataJuz>> getDataJuz();

}
