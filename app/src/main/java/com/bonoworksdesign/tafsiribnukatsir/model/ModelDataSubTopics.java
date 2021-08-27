package com.bonoworksdesign.tafsiribnukatsir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDataSubTopics {
    @SerializedName("no_surat")
    @Expose
    private String no_surat;

    @SerializedName("arabic")
    @Expose
    private String arabic;

    @SerializedName("nm_surat")
    @Expose
    private String nm_surat;

    @SerializedName("hal_1")
    @Expose
    private int hal_1;

    @SerializedName("no_sub_topik")
    @Expose
    private String no_sub_topik;

    @SerializedName("judul")
    @Expose
    private String judul;

    @SerializedName("hal")
    @Expose
    private String hal;

    @SerializedName("uri_img")
    @Expose
    private String uri_img;

    public ModelDataSubTopics(String no_surat, String arabic, String nm_surat, int hal_1, String no_sub_topik, String judul, String hal, String uri_img) {
        this.no_surat = no_surat;
        this.arabic = arabic;
        this.nm_surat = nm_surat;
        this.hal_1 = hal_1;
        this.no_sub_topik = no_sub_topik;
        this.judul = judul;
        this.hal = hal;
        this.uri_img = uri_img;
    }

    public int getHal_1() {
        return hal_1;
    }

    public void setHal_1(int hal_1) {
        this.hal_1 = hal_1;
    }

    public String getNo_surat() {
        return no_surat;
    }

    public void setNo_surat(String no_surat) {
        this.no_surat = no_surat;
    }

    public String getNm_surat() {
        return nm_surat;
    }

    public void setNm_surat(String nm_surat) {
        this.nm_surat = nm_surat;
    }

    public String getUri_img() {
        return uri_img;
    }

    public void setUri_img(String uri_img) {
        this.uri_img = uri_img;
    }

    public String getArabic() {
        return arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    public String getNo_sub_topik() {
        return no_sub_topik;
    }

    public void setNo_sub_topik(String no_sub_topik) {
        this.no_sub_topik = no_sub_topik;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getHal() {
        return hal;
    }

    public void setHal(String hal) {
        this.hal = hal;
    }
}
