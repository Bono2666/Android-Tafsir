package com.bonoworksdesign.tafsiribnukatsir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDataTafsir {
    @SerializedName("no_surat")
    @Expose
    private String no_surat;

    @SerializedName("arabic")
    @Expose
    private String arabic;

    @SerializedName("nm_surat")
    @Expose
    private String nm_surat;

    @SerializedName("no_buku")
    @Expose
    private String no_buku;

    @SerializedName("periode")
    @Expose
    private String periode;

    @SerializedName("hal_1")
    @Expose
    private int hal_1;

    @SerializedName("surat_img")
    @Expose
    private String surat_img;

    @SerializedName("juz")
    @Expose
    private String juz;

    @SerializedName("no_sub_topik")
    @Expose
    private String no_sub_topik;

    @SerializedName("hal")
    @Expose
    private String hal;

    @SerializedName("tafsir")
    @Expose
    private String tafsir;

    @SerializedName("juz_arabic")
    @Expose
    private String juz_arabic;

    @SerializedName("uri_img")
    @Expose
    private String uri_img;

    @SerializedName("judul")
    @Expose
    private String judul;

    public ModelDataTafsir(String no_surat, String arabic, String nm_surat, String no_buku, String periode, int hal_1, String surat_img, String juz, String no_sub_topik, String hal, String tafsir, String juz_arabic, String uri_img, String judul) {
        this.no_surat = no_surat;
        this.arabic = arabic;
        this.nm_surat = nm_surat;
        this.no_buku = no_buku;
        this.periode = periode;
        this.hal_1 = hal_1;
        this.surat_img = surat_img;
        this.juz = juz;
        this.no_sub_topik = no_sub_topik;
        this.hal = hal;
        this.tafsir = tafsir;
        this.juz_arabic = juz_arabic;
        this.uri_img = uri_img;
        this.judul = judul;
    }

    public String getNo_surat() {
        return no_surat;
    }

    public void setNo_surat(String no_surat) {
        this.no_surat = no_surat;
    }

    public String getArabic() {
        return arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    public String getNm_surat() {
        return nm_surat;
    }

    public void setNm_surat(String nm_surat) {
        this.nm_surat = nm_surat;
    }

    public String getNo_buku() {
        return no_buku;
    }

    public void setNo_buku(String no_buku) {
        this.no_buku = no_buku;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public int getHal_1() {
        return hal_1;
    }

    public void setHal_1(int hal_1) {
        this.hal_1 = hal_1;
    }

    public String getSurat_img() {
        return surat_img;
    }

    public void setSurat_img(String surat_img) {
        this.surat_img = surat_img;
    }

    public String getJuz() {
        return juz;
    }

    public void setJuz(String juz) {
        this.juz = juz;
    }

    public String getNo_sub_topik() {
        return no_sub_topik;
    }

    public void setNo_sub_topik(String no_sub_topik) {
        this.no_sub_topik = no_sub_topik;
    }

    public String getHal() {
        return hal;
    }

    public void setHal(String hal) {
        this.hal = hal;
    }

    public String getTafsir() {
        return tafsir;
    }

    public void setTafsir(String tafsir) {
        this.tafsir = tafsir;
    }

    public String getJuz_arabic() {
        return juz_arabic;
    }

    public void setJuz_arabic(String juz_arabic) {
        this.juz_arabic = juz_arabic;
    }

    public String getUri_img() {
        return uri_img;
    }

    public void setUri_img(String uri_img) {
        this.uri_img = uri_img;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
}
