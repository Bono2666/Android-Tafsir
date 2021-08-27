package com.bonoworksdesign.tafsiribnukatsir.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataLastRead extends SQLiteOpenHelper {

    public DataLastRead(Context context) {
        super(context, "lastread", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE lastread (no_surat INTEGER, hal INTEGER, nm_surat TEXT, jilid TEXT, " +
                "periode TEXT, surat_img TEXT, first_page INTEGER, juz TEXT, tafsir TEXT, arabic TEXT, no_sub_topik INTEGER, judul TEXT, " +
                "PRIMARY KEY(no_surat, hal))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
        String query = "SELECT no_surat, arabic, nm_surat, first_page, no_sub_topik, judul, hal, surat_img FROM lastread " + kriteria + ";";
        Cursor data = db.rawQuery(query, null);

        return data;
    }
}
