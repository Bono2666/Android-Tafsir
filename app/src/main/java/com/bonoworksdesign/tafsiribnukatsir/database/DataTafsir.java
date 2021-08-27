package com.bonoworksdesign.tafsiribnukatsir.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DataTafsir extends SQLiteOpenHelper {

    public DataTafsir(Context context) {
        super(context, "tafsir", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE surat (no_surat INTEGER, arabic TEXT, nm_surat TEXT, no_buku TEXT, jml_ayat TEXT, periode TEXT," +
                " hal_1 INTEGER, uri_img TEXT, PRIMARY KEY(no_surat))");
        db.execSQL("CREATE TABLE tafsir (no_surat INTEGER, hal INTEGER, juz TEXT, no_sub_topik INTEGER, tafsir TEXT, " +
                "PRIMARY KEY(no_surat, hal))");
        db.execSQL("CREATE TABLE sub_topik (no_surat INTEGER, no_sub_topik INTEGER, judul TEXT, hal INTEGER, " +
                "PRIMARY KEY(no_surat, no_sub_topik))");
        db.execSQL("CREATE TABLE juz (juz TEXT, juz_arabic TEXT, uri_img TEXT, PRIMARY KEY(juz))");
        db.execSQL("CREATE TABLE jilid (no_buku TEXT, jilid TEXT, uri_img TEXT, PRIMARY KEY(no_buku))");
        db.execSQL("CREATE TABLE lastread (no_surat INTEGER, hal INTEGER, nm_surat TEXT, jilid TEXT, " +
                "periode TEXT, surat_img TEXT, first_page INTEGER, juz TEXT, tafsir TEXT, arabic TEXT, no_sub_topik INTEGER, judul TEXT, " +
                "PRIMARY KEY(no_surat, hal))");
        db.execSQL("CREATE TABLE pengantar (no_pengantar INTEGER, judul TEXT, hal_1 INTEGER, footer, uri_img TEXT, " +
                "PRIMARY KEY(no_pengantar))");
        db.execSQL("CREATE TABLE pengantar_sub_topik (no_pengantar INTEGER, no_sub_topik INTEGER, judul TEXT, hal INTEGER, " +
                "PRIMARY KEY(no_pengantar, no_sub_topik))");
        db.execSQL("CREATE TABLE isi_pengantar (no_pengantar INTEGER, hal INTEGER, no_sub_topik INTEGER, isi TEXT, " +
                "PRIMARY KEY(no_pengantar, hal))");
        db.execSQL("CREATE TABLE bantuan (id_bantuan INTEGER, pertanyaan TEXT, jawaban TEXT, " +
                "PRIMARY KEY(id_bantuan))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addsurat(int no_surat, String arabic, String nm_surat, String no_buku, String jml_ayat, String periode, int hal_1,
                         String uri_img) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO surat(no_surat,arabic,nm_surat,no_buku,jml_ayat,periode,hal_1,uri_img) VALUES(" + no_surat + ",'" +
                arabic + "','" + nm_surat + "','" + no_buku + "','" + jml_ayat + "','" + periode + "'," + hal_1 + ",'" + uri_img + "');");
    }

    public void addtafsir(int no_surat, int hal, String juz, int no_sub_topik, String tafsir) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO tafsir(no_surat,hal,juz,no_sub_topik,tafsir) " +
                "VALUES(" + no_surat + "," + hal + ",'" + juz + "'," + no_sub_topik + ",'" + tafsir + "');");
    }

    public void addsub_topik(int no_surat, int no_sub_topik, String judul, int hal) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO sub_topik(no_surat,no_sub_topik,judul,hal) VALUES(" + no_surat + "," + no_sub_topik + ",'" + judul + "'," +
                hal + ");");
    }

    public void addjuz(String juz, String juz_arabic, String uri_img) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO juz(juz,juz_arabic,uri_img) VALUES('" + juz + "','" + juz_arabic + "','" + uri_img + "');");
    }

    public void addjilid(String no_buku, String jilid, String uri_img) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO jilid(no_buku,jilid,uri_img) VALUES('" + no_buku + "','" + jilid + "','" + uri_img + "');");
    }

    public void addpengantar(int no_pengantar, String judul, int hal_1, String footer, String uri_img) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO pengantar(no_pengantar,judul,hal_1,footer,uri_img) VALUES(" + no_pengantar + ",'" + judul + "'," +
                hal_1 + ",'" + footer + "','" + uri_img + "');");
    }

    public void addpengantarst(int no_pengantar, int no_sub_topik, String judul, int hal) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO pengantar_sub_topik(no_pengantar,no_sub_topik,judul,hal) VALUES(" + no_pengantar + "," +
                no_sub_topik + ",'" + judul + "'," + hal + ");");
    }

    public void addisipengantar(int no_pengantar, int hal, int no_sub_topik, String isi_pengantar) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO isi_pengantar(no_pengantar,hal,no_sub_topik,isi) VALUES(" + no_pengantar + "," + hal + "," +
                no_sub_topik + ",'" + isi_pengantar + "');");
    }

    public void addbantuan(int id_bantuan, String pertanyaan, String jawaban) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO bantuan(id_bantuan,pertanyaan,jawaban) VALUES(" + id_bantuan + ",'" + pertanyaan + "','" +
                jawaban + "');");
    }

    public void removesurat() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM surat;");
    }

    public void removetafsir() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM tafsir;");
    }

    public void removesub_topik() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM sub_topik;");
    }

    public void removejuz() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM juz;");
    }

    public void removejilid() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM jilid;");
    }

    public void removepengantar() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM pengantar;");
    }

    public void removepengantarst() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM pengantar_sub_topik;");
    }

    public void removeisipengantar() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM isi_pengantar;");
    }

    public void removebantuan() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM bantuan;");
    }

    public Cursor getDataJilid(String kriteria) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT jilid.* FROM jilid INNER JOIN surat ON jilid.no_buku = surat.no_buku " +
                "INNER JOIN tafsir ON surat.no_surat = tafsir.no_surat " + kriteria + " GROUP BY jilid.no_buku " +
                "ORDER BY jilid.no_buku;";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getDataPengantar() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT pengantar.* FROM pengantar ORDER BY pengantar.no_pengantar;";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getDataSubTopik(String kriteria) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT sub_topik.no_surat, surat.arabic, surat.nm_surat, surat.hal_1, sub_topik.no_sub_topik, " +
                "judul, hal, surat.uri_img FROM surat INNER JOIN sub_topik ON surat.no_surat = sub_topik.no_surat " +
                kriteria + " GROUP BY sub_topik.no_surat, sub_topik.no_sub_topik ORDER BY sub_topik.no_surat, sub_topik.no_sub_topik;";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getPengantarSubTopik(String kriteria) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT pengantar_sub_topik.no_pengantar, pengantar.judul, pengantar.hal_1, pengantar_sub_topik.no_sub_topik, " +
                "pengantar_sub_topik.judul AS judul_sub_topik, hal, uri_img FROM pengantar INNER JOIN pengantar_sub_topik " +
                "ON pengantar.no_pengantar = pengantar_sub_topik.no_pengantar " + kriteria + " ORDER BY pengantar_sub_topik.no_sub_topik;";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getDataSurat(String kriteria) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT surat.no_surat, arabic, nm_surat, juz, no_buku, jml_ayat, periode, hal_1, uri_img" +
                " FROM surat INNER JOIN tafsir ON surat.no_surat = tafsir.no_surat " + kriteria +
                " GROUP BY surat.no_surat ORDER BY surat.no_surat;";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getDataTafsir(String kriteria) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT tafsir.no_surat, surat.arabic, surat.nm_surat, surat.no_buku, surat.periode, surat.hal_1, " +
                "surat.uri_img AS surat_img, tafsir.juz, tafsir.no_sub_topik, tafsir.hal, tafsir, juz.juz_arabic, juz.uri_img, judul " +
                "FROM juz INNER JOIN ((tafsir INNER JOIN surat ON tafsir.no_surat = surat.no_surat) INNER JOIN sub_topik " +
                "ON sub_topik.no_surat = tafsir.no_surat AND sub_topik.no_sub_topik = tafsir.no_sub_topik) ON juz.juz = tafsir.juz " +
                kriteria + ";";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getIsiPengantar(String kriteria) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT isi_pengantar.no_pengantar, pengantar.hal_1, no_sub_topik, hal, isi, footer, uri_img " +
                "FROM isi_pengantar INNER JOIN pengantar ON isi_pengantar.no_pengantar = pengantar.no_pengantar " + kriteria + ";";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getDataCari(String cari, String kriteria) {
        SQLiteDatabase db = getReadableDatabase();
        cari = cari.replace("'","");

        String query = "SELECT sub_topik.no_surat, surat.arabic, surat.nm_surat, surat.hal_1, sub_topik.no_sub_topik, " +
                "sub_topik.judul, q_cari.hal, surat.uri_img FROM surat " +
                "INNER JOIN sub_topik ON surat.no_surat = sub_topik.no_surat INNER JOIN (SELECT no_surat, no_sub_topik, hal " +
                "FROM tafsir WHERE REPLACE(tafsir,'''','') LIKE '%" + cari + "%') AS q_cari ON q_cari.no_surat = sub_topik.no_surat AND " +
                "q_cari.no_sub_topik = sub_topik.no_sub_topik " + kriteria + ";";

        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getDataBantuan(String kriteria) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT bantuan.* FROM bantuan " + kriteria + " ORDER BY id_bantuan;";

        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getDataCariPengantar(String cari, String no) {
        SQLiteDatabase db = getReadableDatabase();
        cari = cari.replace("'","");

        String query = "SELECT pengantar_sub_topik.no_pengantar, pengantar.judul, pengantar.hal_1, " +
                "pengantar_sub_topik.no_sub_topik, pengantar_sub_topik.judul AS judul_sub_topik, q_cari.hal, pengantar.uri_img " +
                "FROM pengantar INNER JOIN pengantar_sub_topik ON pengantar.no_pengantar = pengantar_sub_topik.no_pengantar " +
                "INNER JOIN (SELECT no_pengantar, no_sub_topik, hal FROM isi_pengantar WHERE REPLACE(tafsir,'''','') LIKE '%" +
                cari + "%') AS q_cari ON q_cari.no_pengantar = pengantar_sub_topik.no_pengantar " +
                "AND q_cari.no_sub_topik = pengantar_sub_topik.no_sub_topik WHERE pengantar_sub_topik.no_pengantar = " + no + ";";

        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public void addLastread(int no_surat, int hal, String nm_surat, String jilid, String periode, String surat_img, int first_page,
                            String juz, String tafsir, String arabic, int no_sub_topik, String judul) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO lastread(no_surat,hal,nm_surat,jilid,periode,surat_img,first_page,juz,tafsir,arabic,no_sub_topik,judul) " +
                "VALUES(" + no_surat + "," + hal + ",'" + nm_surat + "','" + jilid + "','" + periode + "','" + surat_img + "'," +
                first_page + ",'" + juz + "','" + tafsir + "','" + arabic + "'," + no_sub_topik + ",'" + judul + "');");
    }

    public void updateLastread(String surat_img, int no_surat, int hal) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE lastread SET surat_img = '" + surat_img + "' WHERE no_surat = " + no_surat + " AND hal = " + hal + ";");
    }

    public void removeLastread(int no_surat, int hal) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM lastread WHERE no_surat = " + no_surat + " AND hal = " + hal + ";");
    }

    public boolean isThirteen() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM lastread;";
        Cursor data = db.rawQuery(query,null);

        if (data.getCount() <= 12) {
            data.close();
            return false;
        }

        data.close();
        return true;
    }

    public boolean isExist(int no_surat, int hal) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM lastread WHERE no_surat = " + no_surat + " AND hal = " + hal + ";";
        Cursor data = db.rawQuery(query,null);

        if (data.getCount() <= 0) {
            data.close();
            return false;
        }

        data.close();
        return true;
    }

    public Cursor getData(String kriteria) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM lastread " + kriteria + ";";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getCariLastRead(String kriteria) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT sub_topik.no_surat, lastread.arabic, lastread.nm_surat, lastread.first_page, sub_topik.no_sub_topik, " +
                "sub_topik.judul, q_cari.hal, lastread.surat_img FROM lastread " +
                "INNER JOIN sub_topik ON lastread.no_surat = sub_topik.no_surat INNER JOIN (SELECT no_surat, no_sub_topik, hal " +
                "FROM tafsir " + kriteria + ") AS q_cari ON q_cari.no_surat = sub_topik.no_surat AND " +
                "q_cari.no_sub_topik = sub_topik.no_sub_topik;";

        Cursor data = db.rawQuery(query, null);

        return data;
    }
}
