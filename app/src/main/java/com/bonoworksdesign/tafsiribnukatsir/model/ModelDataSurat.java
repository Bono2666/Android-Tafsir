package com.bonoworksdesign.tafsiribnukatsir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDataSurat {
    @SerializedName("no_surat")
    @Expose
    private String noSurat;

    @SerializedName("arabic")
    @Expose
    private String arabic;

    @SerializedName("nm_surat")
    @Expose
    private String nmSurat;

    @SerializedName("juz")
    @Expose
    private String juzSurat;

    @SerializedName("no_buku")
    @Expose
    private String noBuku;

    @SerializedName("jml_ayat")
    @Expose
    private String jmlAyat;

    @SerializedName("periode")
    @Expose
    private String perTurun;

    @SerializedName("hal_1")
    @Expose
    private String hal_1;

    @SerializedName("uri_img")
    @Expose
    private String uriImg;

    public ModelDataSurat(String noSurat, String arabic, String nmSurat, String juzSurat, String noBuku, String jmlAyat, String perTurun, String hal_1, String uriImg) {
        this.noSurat = noSurat;
        this.arabic = arabic;
        this.nmSurat = nmSurat;
        this.juzSurat = juzSurat;
        this.noBuku = noBuku;
        this.jmlAyat = jmlAyat;
        this.perTurun = perTurun;
        this.hal_1 = hal_1;
        this.uriImg = uriImg;
    }

    public String getArabic() {
        return arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    public String getHal_1() {
        return hal_1;
    }

    public void setHal_1(String hal_1) {
        this.hal_1 = hal_1;
    }

    public String getNoSurat() {
        return noSurat;
    }

    public void setNoSurat(String noSurat) {
        this.noSurat = noSurat;
    }

    public String getNmSurat() {
        return nmSurat;
    }

    public void setNmSurat(String nmSurat) {
        this.nmSurat = nmSurat;
    }

    public String getJuzSurat() {
        return juzSurat;
    }

    public void setJuzSurat(String juzSurat) {
        this.juzSurat = juzSurat;
    }

    public String getNoBuku() {
        return noBuku;
    }

    public void setNoBuku(String noBuku) {
        this.noBuku = noBuku;
    }

    public String getJmlAyat() {
        return jmlAyat;
    }

    public void setJmlAyat(String jmlAyat) {
        this.jmlAyat = jmlAyat;
    }

    public String getPerTurun() {
        return perTurun;
    }

    public void setPerTurun(String perTurun) {
        this.perTurun = perTurun;
    }

    public String getUriImg() {
        return uriImg;
    }

    public void setUriImg(String uriImg) {
        this.uriImg = uriImg;
    }
}
