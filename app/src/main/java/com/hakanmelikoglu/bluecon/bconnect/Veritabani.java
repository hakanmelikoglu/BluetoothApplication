package com.hakanmelikoglu.bluecon.bconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hakan on 26.04.2016.
 */
public class Veritabani extends SQLiteOpenHelper {

    private static final String VERITABANI = "tustakimi";
    private static final String TABLO = "tablo";
    private static final String TABLOUUID = "uuid";
    private static final String GIDEN = "giden";
    private static final String GELEN = "gelen";
    private static final int SURUM = 7;
    private final String IDSECURE = "fa87c0d0-afac-11de-8a39-0800200c9a66";
    private final String IDINSECURE  = "00001101-0000-1000-8000-00805f9b34fb";

    public Veritabani(Context context){
        super(context,VERITABANI, null, SURUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // TABLO tablosu
        db.execSQL("CREATE TABLE "+ TABLO +
                "(anahtar TEXT, deger TEXT," + "anahtar2 TEXT, deger2 TEXT," + "anahtar3 TEXT, deger3 TEXT," +
                "anahtar4 TEXT, deger4 TEXT," + "anahtar5 TEXT, deger5 TEXT," + "anahtar6 TEXT, deger6 TEXT," +
                "anahtar7 TEXT, deger7 TEXT," + "anahtar8 TEXT, deger8 TEXT," + "anahtar9 TEXT, deger9 TEXT," +
                "anahtarA TEXT, " + "anahtarB TEXT," + "anahtarC TEXT)");

        ContentValues values = new ContentValues();
        values.put("anahtar","1");
        values.put("deger","Bir");
        values.put("anahtar2","2");
        values.put("deger2","İki");
        values.put("anahtar3","3");
        values.put("deger3","Üç");
        values.put("anahtar4","4");
        values.put("deger4","Dort");
        values.put("anahtar5","5");
        values.put("deger5","Beş");
        values.put("anahtar6","6");
        values.put("deger6","Altı");
        values.put("anahtar7","7");
        values.put("deger7","Yedi");
        values.put("anahtar8","8");
        values.put("deger8","Sekiz");
        values.put("anahtar9","9");
        values.put("deger9","Dokuz");
        values.put("anahtarA","A");
        values.put("anahtarB","B");
        values.put("anahtarC","C");
        db.insert(TABLO,null,values);

        // UUID Tablosu
        db.execSQL("CREATE TABLE "+ TABLOUUID + "(secure TEXT, insecure TEXT)");
        ContentValues values1 = new ContentValues();
        values1.put("secure",IDSECURE);
        values1.put("insecure",IDINSECURE);
        db.insert(TABLOUUID, null, values1);

        // DATA Tablosu. Gönderilen veriler bunun üzerinden gönderilir.
        // VT 3
        db.execSQL("CREATE TABLE "+ GIDEN + "(databitset TEXT)");
        // VT 4
        db.execSQL("CREATE TABLE "+ GELEN + "(databitget TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLOUUID);
        db.execSQL("DROP TABLE IF EXISTS " + GIDEN);
        db.execSQL("DROP TABLE IF EXISTS " + GELEN);

        onCreate(db);
    }
}
