package com.bonoworksdesign.tafsiribnukatsir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDataPengantar {
    @SerializedName("no_pengantar")
    @Expose
    private String noPengantar;

    @SerializedName("judul")
    @Expose
    private String judul;

    @SerializedName("hal_1")
    @Expose
    private String hal_1;

    @SerializedName("footer")
    @Expose
    private String footer;

    @SerializedName("uri_img")
    @Expose
    private String uriImg;

    public ModelDataPengantar(String noPengantar, String judul, String hal_1, String footer, String uriImg) {
        this.noPengantar = noPengantar;
        this.judul = judul;
        this.hal_1 = hal_1;
        this.footer = footer;
        this.uriImg = uriImg;
    }

    public String getNoPengantar() {
        return noPengantar;
    }

    public void setNoPengantar(String noPengantar) {
        this.noPengantar = noPengantar;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getHal_1() {
        return hal_1;
    }

    public void setHal_1(String hal_1) {
        this.hal_1 = hal_1;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getUriImg() {
        return uriImg;
    }

    public void setUriImg(String uriImg) {
        this.uriImg = uriImg;
    }
}
