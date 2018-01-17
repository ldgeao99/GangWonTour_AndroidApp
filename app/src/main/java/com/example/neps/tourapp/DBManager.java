package com.example.neps.tourapp;

/**
 * Created by Minyong on 2017-11-06.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블을 생성한다.
        // create table 테이블명 (컬럼명 타입 옵션);
        db.execSQL("CREATE TABLE STAMP_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, contentid TEXT, contenttypeid TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String title, String contentId, String contentTypeId) {
        SQLiteDatabase db = getWritableDatabase();
        String _query = "INSERT INTO STAMP_LIST VALUES(null, '" + title + "', '" + contentId + "', '" + contentTypeId + "');";
        db.execSQL(_query);
        db.close();
    }

    public void update(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public String getData() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery("select * from STAMP_LIST", null);
        while(cursor.moveToNext()) {
            str += cursor.getInt(0)
                    + "#"
                    + cursor.getString(1)
                    + "#"
                    + cursor.getInt(2)
                    + "#"
                    + cursor.getInt(3)
                    + "\n";
        }

        return str;
    }

    public boolean exitedValue(String title) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from STAMP_LIST where name='" + title + "';", null);
        String str = "";
        while(cursor.moveToNext()) {
            str += cursor.getString(0);
        }

        if (str == "")
            return false;
        else
            return true;
    }
}
