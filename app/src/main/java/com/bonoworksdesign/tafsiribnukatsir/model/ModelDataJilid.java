package com.bonoworksdesign.tafsiribnukatsir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDataJilid {
    @SerializedName("no_buku")
    @Expose
    private String no_buku;

    @SerializedName("jilid")
    @Expose
    private String jilidBuku;

    @SerializedName("uri_img")
    @Expose
    private String uriImg;

    public ModelDataJilid(String no_buku, String jilidBuku, String uriImg) {
        this.no_buku = no_buku;
        this.jilidBuku = jilidBuku;
        this.uriImg = uriImg;
    }

    public String getNo_buku() {
        return no_buku;
    }

    public void setNo_buku(String no_buku) {
        this.no_buku = no_buku;
    }

    public String getJilidBuku() {
        return jilidBuku;
    }

    public void setJilidBuku(String jilidBuku) {
        this.jilidBuku = jilidBuku;
    }

    public String getUriImg() {
        return uriImg;
    }

    public void setUriImg(String uriImg) {
        this.uriImg = uriImg;
    }
}
