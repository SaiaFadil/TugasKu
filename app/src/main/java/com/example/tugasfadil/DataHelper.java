package com.example.tugasfadil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tugasfadil.db";
    private static final int DATABASE_VERSION = 1;
    public DataHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    String sql = "create table tugas(no integer primary key,matkul text null,tgl text null,deskripsi text null);";
        Log.d("Data","onCreate"+sql);
        db.execSQL(sql);
        sql = "INSERT INTO tugas(no ,matkul,tgl,deskripsi) VALUES ('1','WKPL','02-12-2023','Kerjakan BKPM acara 40 sampai 42 ,Screenshot kode beserta outputnya untuk di dokumentasikan pada laporan')";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
