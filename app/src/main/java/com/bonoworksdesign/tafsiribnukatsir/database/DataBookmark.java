package com.bonoworksdesign.tafsiribnukatsir.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DataBookmark extends SQLiteOpenHelper {

    public DataBookmark(Context context) {
        super(context, "bookmark", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE bookmark (no_surat INTEGER, hal INTEGER, nm_surat TEXT, jilid TEXT, " +
                "periode TEXT, surat_img TEXT, first_page INTEGER, PRIMARY KEY(no_surat, hal))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS bookmark");
//        onCreate(db);
    }

    public void addBookmark(int no_surat, int hal, String nm_surat, String jilid, String periode, String surat_img, int first_page) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO bookmark(no_surat,hal,nm_surat,jilid,periode,surat_img,first_page) VALUES(" + no_surat + "," + hal +
                ",'" + nm_surat + "','" + jilid + "','" + periode + "','" + surat_img + "'," + first_page + ");");
    }

    public void updateBookmark(String surat_img, int no_surat, int hal) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE bookmark SET surat_img = '" + surat_img + "' WHERE no_surat = " + no_surat + " AND hal = " + hal + ";");
    }

    public void removeBookmark(int no_surat, int hal) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM bookmark WHERE no_surat = " + no_surat + " AND hal = " + hal + ";");
    }

    public boolean isBookmark(int no_surat, String hal) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM bookmark WHERE no_surat = " + no_surat + " AND hal = " + hal + ";";
        Cursor data = db.rawQuery(query,null);

        if (data.getCount() <= 0) {
            data.close();
            return false;
        }

        data.close();
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM bookmark ORDER BY no_surat, hal;";
        Cursor data = db.rawQuery(query, null);

        return data;
    }
}
