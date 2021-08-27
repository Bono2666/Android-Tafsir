package com.bonoworksdesign.tafsiribnukatsir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDataBantuan {
    @SerializedName("id_bantuan")
    @Expose
    private String id_bantuan;

    @SerializedName("pertanyaan")
    @Expose
    private String pertanyaan;

    @SerializedName("jawaban")
    @Expose
    private String jawaban;

    public ModelDataBantuan(String id_bantuan, String pertanyaan, String jawaban) {
        this.id_bantuan = id_bantuan;
        this.pertanyaan = pertanyaan;
        this.jawaban = jawaban;
    }

    public String getId_bantuan() {
        return id_bantuan;
    }

    public void setId_bantuan(String id_bantuan) {
        this.id_bantuan = id_bantuan;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getJawaban() {
        return jawaban;
    }

    public void setJawaban(String jawaban) {
        this.jawaban = jawaban;
    }
}
