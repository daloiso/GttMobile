package com.example.pdaloiso.gttmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pdaloiso.gttmobile.model.Fermata;
import com.example.pdaloiso.gttmobile.model.Percorso;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by pdaloiso on 13/05/2015.
 */
public class SqlController {

    private MyDbHelper dbHelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public SqlController(Context c) {
        ourcontext = c;
        insertDataExample();
    }

    public SqlController open() throws SQLException {
        dbHelper = new MyDbHelper(ourcontext);
        database = dbHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        dbHelper.close();
    }


    private void insertData(Fermata inizio, Fermata fine) {

        ContentValues cv = new ContentValues();
        cv.put(MyDbHelper.FERMATA_FIELD1, inizio.getIdFermata());
        cv.put(MyDbHelper.FERMATA_FIELD2, inizio.getIndicazioneStradale());
        cv.put(MyDbHelper.FERMATA_FIELD3, inizio.getAltreIndicazioni());
        cv.put(MyDbHelper.FERMATA_FIELD4, inizio.isBanchina());
        cv.put(MyDbHelper.FERMATA_FIELD5, inizio.getX());
        cv.put(MyDbHelper.FERMATA_FIELD6, inizio.getY());

        database.insert(MyDbHelper.FERMATA_TABLE, null, cv);

        ContentValues cv2 = new ContentValues();
        cv2.put(MyDbHelper.FERMATA_FIELD1, fine.getIdFermata());
        cv2.put(MyDbHelper.FERMATA_FIELD2, fine.getIndicazioneStradale());
        cv2.put(MyDbHelper.FERMATA_FIELD3, fine.getAltreIndicazioni());
        cv2.put(MyDbHelper.FERMATA_FIELD4, fine.isBanchina());
        cv2.put(MyDbHelper.FERMATA_FIELD5, fine.getX());
        cv2.put(MyDbHelper.FERMATA_FIELD6, fine.getY());

        database.insert(MyDbHelper.FERMATA_TABLE, null, cv2);

        ContentValues cv3 = new ContentValues();
        cv3.put(MyDbHelper.PERCORSO_FIELD1, inizio.getIdFermata());
        cv3.put(MyDbHelper.PERCORSO_FIELD2, fine.getIdFermata());

        database.insert(MyDbHelper.PERCORSO_TABLE, null, cv3);

    }

    public void insertDataExample() {
        try {
            open();
            database.execSQL("DROP TABLE IF EXISTS " + MyDbHelper.PERCORSO_TABLE);
            database.execSQL("DROP TABLE  IF EXISTS " + MyDbHelper.FERMATA_TABLE);
            dbHelper.onCreate(database);
            Fermata inizio = new Fermata();
            inizio.setAltreIndicazioni("altre indicazioni");
            inizio.setBanchina(true);
            inizio.setIdFermata(1);
            inizio.setIndicazioneStradale("indicazione stradale");
            inizio.setX(45.08078);
            inizio.setY(7.69289);
            Fermata fine = new Fermata();
            fine.setAltreIndicazioni("AAA");
            fine.setBanchina(true);
            fine.setIdFermata(2);
            fine.setIndicazioneStradale("BBB");
            fine.setX(45.06987);
            fine.setY(7.66477);
            insertData(inizio, fine);
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Percorso getPercorso(){
        try {
            open();
            Percorso percorso = new Percorso();
            percorso.setFermate(new ArrayList<Fermata>());
            Cursor cursor = database.query(MyDbHelper.PERCORSO_TABLE,
                    MyDbHelper.PERCORSO_ALL_COLUMN, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Integer idFermata1 = cursor.getInt(0);
                Fermata fermata1 = new Fermata();
                fermata1.setIdFermata(idFermata1);
                percorso.getFermate().add(fermata1);

                Integer idFermata2 = cursor.getInt(1);
                Fermata fermata2 = new Fermata();
                fermata2.setIdFermata(idFermata2);
                percorso.getFermate().add(fermata2);

                cursor.moveToNext();
            }
            cursor.close();


            String whereClause = MyDbHelper.FERMATA_FIELD1+" = ? ";
            Fermata fermata = percorso.getFermate().get(0);
            String[] whereArgs = new String[] {
                    fermata.getIdFermata().toString()
            };

            Cursor cursor2 = database.query(MyDbHelper.FERMATA_TABLE,
                    MyDbHelper.FERMATA_ALL_COLUMN, whereClause, whereArgs, null, null, null);
            cursor2.moveToFirst();
            while (!cursor2.isAfterLast()) {
                percorso.getFermate().set(0,cursorToFermata(cursor2));
                cursor2.moveToNext();
            }
            cursor2.close();

            Fermata fermata2 = percorso.getFermate().get(1);
            String[] whereArgs2 = new String[] {
                    fermata2.getIdFermata().toString()
            };
            Cursor cursor3 = database.query(MyDbHelper.FERMATA_TABLE,
                    MyDbHelper.FERMATA_ALL_COLUMN, whereClause, whereArgs2, null, null, null);
            cursor3.moveToFirst();
            while (!cursor3.isAfterLast()) {
                percorso.getFermate().set(1,cursorToFermata(cursor3));
                cursor3.moveToNext();
            }
            cursor3.close();
            close();
            return percorso;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Fermata cursorToFermata(Cursor cursor) {
        Fermata fermata = new Fermata();
        fermata.setIdFermata(cursor.getInt(0));
        fermata.setIndicazioneStradale(cursor.getString(1));
        fermata.setAltreIndicazioni(cursor.getString(2));
        fermata.setBanchina(cursor.getInt(3)==1);
        fermata.setX(cursor.getDouble(4));
        fermata.setY(cursor.getDouble(5));
        return fermata;
    }

}
