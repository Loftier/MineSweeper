package com.example.loftier.minesweeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "Data.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE Data (id Integer Primary Key AutoIncrement, Username varchar(50), EMail varchar(50), Password varchar(50), MobileNo Integer, image_source Integer)";
        db.execSQL(query);
        String b_scores = "CREATE TABLE Time (id Integer Primary Key AutoIncrement, username varchar(50), beginner varchar(10), specialist varchar(10), expert varchar(10), grandmaster varchar(10))";
        db.execSQL(b_scores);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
