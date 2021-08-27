package com.bonoworksdesign.tafsiribnukatsir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelIsiPengantar {
    @SerializedName("no_pengantar")
    @Expose
    private String no_pengantar;

    @SerializedName("hal_1")
    @Expose
    private int hal_1;

    @SerializedName("no_sub_topik")
    @Expose
    private String no_sub_topik;

    @SerializedName("hal")
    @Expose
    private String hal;

    @SerializedName("isi")
    @Expose
    private String isi;

    @SerializedName("footer")
    @Expose
    private String footer;

    @SerializedName("uri_img")
    @Expose
    private String uri_img;

    public ModelIsiPengantar(String no_pengantar, int hal_1, String no_sub_topik, String hal, String isi, String footer, String uri_img) {
        this.no_pengantar = no_pengantar;
        this.hal_1 = hal_1;
        this.no_sub_topik = no_sub_topik;
        this.hal = hal;
        this.isi = isi;
        this.footer = footer;
        this.uri_img = uri_img;
    }

    public String getNo_pengantar() {
        return no_pengantar;
    }

    public void setNo_pengantar(String no_pengantar) {
        this.no_pengantar = no_pengantar;
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

    public String getHal() {
        return hal;
    }

    public void setHal(String hal) {
        this.hal = hal;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getUri_img() {
        return uri_img;
    }

    public void setUri_img(String uri_img) {
        this.uri_img = uri_img;
    }
}
