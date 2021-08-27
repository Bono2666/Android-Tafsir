package com.bonoworksdesign.tafsiribnukatsir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDataLastRead {
    @SerializedName("no_surat")
    @Expose
    private int noSurat;

    @SerializedName("hal")
    @Expose
    private int hal;

    @SerializedName("nm_surat")
    @Expose
    private String nmSurat;

    @SerializedName("jilid")
    @Expose
    private String jilid;

    @SerializedName("periode")
    @Expose
    private String periode;

    @SerializedName("surat_img")
    @Expose
    private String suratImg;

    @SerializedName("first_page")
    @Expose
    private int firstPage;

    @SerializedName("juz")
    @Expose
    private String juz;

    @SerializedName("tafsir")
    @Expose
    private String tafsir;

    public ModelDataLastRead(int noSurat, int hal, String nmSurat, String jilid, String periode, String suratImg, int firstPage, String juz, String tafsir) {
        this.noSurat = noSurat;
        this.hal = hal;
        this.nmSurat = nmSurat;
        this.jilid = jilid;
        this.periode = periode;
        this.suratImg = suratImg;
        this.firstPage = firstPage;
        this.juz = juz;
        this.tafsir = tafsir;
    }

    public int getNoSurat() {
        return noSurat;
    }

    public void setNoSurat(int noSurat) {
        this.noSurat = noSurat;
    }

    public int getHal() {
        return hal;
    }

    public void setHal(int hal) {
        this.hal = hal;
    }

    public String getNmSurat() {
        return nmSurat;
    }

    public void setNmSurat(String nmSurat) {
        this.nmSurat = nmSurat;
    }

    public String getJilid() {
        return jilid;
    }

    public void setJilid(String jilid) {
        this.jilid = jilid;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getSuratImg() {
        return suratImg;
    }

    public void setSuratImg(String suratImg) {
        this.suratImg = suratImg;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public String getJuz() {
        return juz;
    }

    public void setJuz(String juz) {
        this.juz = juz;
    }

    public String getTafsir() {
        return tafsir;
    }

    public void setTafsir(String tafsir) {
        this.tafsir = tafsir;
    }
}
