package com.bonoworksdesign.tafsiribnukatsir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDataJuz {
    @SerializedName("juz")
    @Expose
    private String juz;

    @SerializedName("juz_arabic")
    @Expose
    private String juz_arabic;

    @SerializedName("uri_img")
    @Expose
    private String uriImg;

    public ModelDataJuz(String juz, String juz_arabic, String uriImg) {
        this.juz = juz;
        this.juz_arabic = juz_arabic;
        this.uriImg = uriImg;
    }

    public String getJuz() {
        return juz;
    }

    public void setJuz(String juz) {
        this.juz = juz;
    }

    public String getJuz_arabic() {
        return juz_arabic;
    }

    public void setJuz_arabic(String juz_arabic) {
        this.juz_arabic = juz_arabic;
    }

    public String getUriImg() {
        return uriImg;
    }

    public void setUriImg(String uriImg) {
        this.uriImg = uriImg;
    }
}
