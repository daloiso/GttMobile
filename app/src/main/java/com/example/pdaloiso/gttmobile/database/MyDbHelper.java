package com.example.pdaloiso.gttmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pdaloiso.gttmobile.model.Fermata;

import java.sql.SQLException;

/**
 * Created by pdaloiso on 13/05/2015.
 */
public class MyDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "gttMobile";
    private static final int DB_VERSION = 1;

    public static final String PERCORSO_TABLE = "PERCORSO";
    public static final String PERCORSO_FIELD1 = "ID_FERMATA";
    public static final String PERCORSO_FIELD2 = "ID_FERMATA";
    public static final String[] PERCORSO_ALL_COLUMN = {PERCORSO_FIELD1, PERCORSO_FIELD2};

    public static final String FERMATA_TABLE = "FERMATA";
    public static final String FERMATA_FIELD1 = "ID_FERMATA";
    public static final String FERMATA_FIELD2 = "INDICAZIONE_STRADALE";
    public static final String FERMATA_FIELD3 = "ALTRE_INDICAZIONI";
    public static final String FERMATA_FIELD4 = "BANCHINA";
    public static final String FERMATA_FIELD5 = "X";
    public static final String FERMATA_FIELD6 = "Y";
    public static final String[] FERMATA_ALL_COLUMN = {FERMATA_FIELD1, FERMATA_FIELD2,
            FERMATA_FIELD3, FERMATA_FIELD4, FERMATA_FIELD5, FERMATA_FIELD6};

    private static final String PERCORSO_CREATE_TABLE = "create table " + PERCORSO_TABLE + " (" +
            PERCORSO_FIELD1 + " INTEGER, " + PERCORSO_FIELD2 + " INTEGER )";
    private static final String FERMATA_CREATE_TABLE = "create table " + FERMATA_TABLE + " (" +
            FERMATA_FIELD1 + " INTEGER PRIMARY KEY, " + FERMATA_FIELD2 + " TEXT, " + FERMATA_FIELD3 +
            " TEXT, " + FERMATA_FIELD4 + " INTEGER, " + FERMATA_FIELD5 + " REAL, " + FERMATA_FIELD6 + " REAL)";


    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + PERCORSO_TABLE + " IF EXISTS " + PERCORSO_TABLE);
        db.execSQL("DROP TABLE " + FERMATA_TABLE + " IF EXISTS " + FERMATA_TABLE);
        this.onCreate(db);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PERCORSO_CREATE_TABLE);
        db.execSQL(FERMATA_CREATE_TABLE);
    }


}
