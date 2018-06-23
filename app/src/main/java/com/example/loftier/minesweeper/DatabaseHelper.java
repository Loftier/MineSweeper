package com.example.loftier.minesweeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String col1="id";
    public DatabaseHelper(Context context) {
        super(context, "Data.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "CREATE TABLE Data ("+col1+" Integer Primary Key AutoIncrement, Username varchar(50), EMail varchar(50), Password varchar(50), MobileNo Integer)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
