package com.gengyufei.gyfdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by FineFan on 2017/6/12.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BoteBook.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTE = "NOTE";
    public static final String NOTE_ID = "_ID";
    public static final String NOTE_CONTENT = "CONTENT";
    public static final String NOTE_UPDATE_TIME = "UPDATE_TIME";
    public static final String NOTE_CREATE_TIME = "CREATE_TIME";
    public static final String NOTE_TYPE = "TYPE";
    public static final String NOTE_USER_ID = "USER_ID";

    public static final String TABLE_USER = "USER";
    public static final String USER_ID = "_ID";
    public static final String USER_TOKEN = "TOKEN";
    public static final String USER_NAME = "NAME";
    public static final String USER_PASSWORD = "PASSWORD";
    public static final String USER_NICKNAME = "NICKNAME";

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE " + TABLE_NOTE + "("
                + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NOTE_CONTENT + " TEXT \"\","
                + NOTE_UPDATE_TIME + " INTEGER\"\","
                + NOTE_CREATE_TIME + " INTEGER\"\","
                + NOTE_USER_ID + " INTEGER\"\","
                + NOTE_TYPE + " TEXT\"\"" + ")";

        String sql2 = "CREATE TABLE " + TABLE_USER + "("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_TOKEN + " TEXT NOT NULL DEFAULT\"\","
                + USER_NAME + " TEXT NOT NULL DEFAULT\"\","
                + USER_PASSWORD + " TEXT NOT NULL DEFAULT\"\","
                + USER_NICKNAME + " TEXT NOT NULL DEFAULT\"\"" + ")";


        Log.e("info", sql1);
        Log.e("info", sql2);
        db.execSQL(sql1);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
