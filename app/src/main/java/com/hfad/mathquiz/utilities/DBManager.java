package com.hfad.mathquiz.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    private static DBManager instance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "highscores.db";
    private static final String TABLE_HIGHSCORES = "highscores";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_HIGHSCORES = "highscores";

    public static synchronized DBManager getInstance(Context context) {
        if(instance == null) {
            instance = new DBManager(context.getApplicationContext(), null);
        }
        return instance;
    }

    private DBManager(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_HIGHSCORES + "(" + COLUMN_ID + " INTEGER PRIMARY " +
                "KEY AUTOINCREMENT," + COLUMN_HIGHSCORES + " INTEGER );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORES);
        onCreate(db);
    }

    public int getMaxScore() {
        int maxScore;
        SQLiteDatabase db = getReadableDatabase();
        String findMaxQuery = "SELECT MAX(" + COLUMN_HIGHSCORES + ") as max FROM " +
                TABLE_HIGHSCORES;
        Cursor cursor = db.rawQuery(findMaxQuery, null);
        cursor.moveToFirst();
        maxScore = cursor.getInt(cursor.getColumnIndex("max"));
        cursor.close();
        return maxScore;
    }

    public boolean addHighscore(int highscore) {
        boolean isHighScore = false;
        SQLiteDatabase db = getWritableDatabase();
        if(highscore > getMaxScore()) {
            isHighScore = true;
            ContentValues values = new ContentValues();
            values.put(COLUMN_HIGHSCORES, highscore);
            db.insert(TABLE_HIGHSCORES, null, values);
        }
        db.close();
        return isHighScore;
    }

    public String databaseToString() {
        StringBuilder dbString = new StringBuilder();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HIGHSCORES + " WHERE 1";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            if(cursor.getString(cursor.getColumnIndex("highscores")) != null) {
                dbString.append(cursor.getString(cursor.getColumnIndex("highscores")));
                dbString.append("\n");
            }
            cursor.moveToNext();
        }
        db.close();
        cursor.close();
        return dbString.toString();
    }
}
