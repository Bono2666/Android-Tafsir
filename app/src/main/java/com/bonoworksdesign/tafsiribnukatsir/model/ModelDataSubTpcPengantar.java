package com.bonoworksdesign.tafsiribnukatsir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDataSubTpcPengantar {
    @SerializedName("no_pengantar")
    @Expose
    private String no_pengantar;

    @SerializedName("judul")
    @Expose
    private String judul;

    @SerializedName("hal_1")
    @Expose
    private int hal_1;

    @SerializedName("no_sub_topik")
    @Expose
    private String no_sub_topik;

    @SerializedName("judul_sub_topik")
    @Expose
    private String judul_sub_topik;

    @SerializedName("hal")
    @Expose
    private String hal;

    @SerializedName("uri_img")
    @Expose
    private String uri_img;

    public ModelDataSubTpcPengantar(String no_pengantar, String judul, int hal_1, String no_sub_topik, String judul_sub_topik, String hal, String uri_img) {
        this.no_pengantar = no_pengantar;
        this.judul = judul;
        this.hal_1 = hal_1;
        this.no_sub_topik = no_sub_topik;
        this.judul_sub_topik = judul_sub_topik;
        this.hal = hal;
        this.uri_img = uri_img;
    }

    public String getNo_pengantar() {
        return no_pengantar;
    }

    public void setNo_pengantar(String no_pengantar) {
        this.no_pengantar = no_pengantar;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public int getHal_1() {
        return hal_1;
    }

    public void setHal_1(int hal_1) {
        this.hal_1 = hal_1;
    }

    public String getNo_sub_topik() {
        return no_sub_topik;
    }

    public void setNo_sub_topik(String no_sub_topik) {
        this.no_sub_topik = no_sub_topik;
    }

    public String getJudul_sub_topik() {
        return judul_sub_topik;
    }

    public void setJudul_sub_topik(String judul_sub_topik) {
        this.judul_sub_topik = judul_sub_topik;
    }

    public String getHal() {
        return hal;
    }

    public void setHal(String hal) {
        this.hal = hal;
    }

    public String getUri_img() {
        return uri_img;
    }

    public void setUri_img(String uri_img) {
        this.uri_img = uri_img;
    }
}
