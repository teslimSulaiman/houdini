package com.example.user.houdini.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.houdini.data.HoudiniContract.*;

/**
 * Created by USER on 7/16/2017.
 */

public class HoudiniDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "houdini.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 3;

    /**
     * Constructs a new instance of {@link HoudiniDbHelper}.
     *
     * @param context of the app
     */
    public HoudiniDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String LOG_TAG = HoudiniContract.class.getSimpleName();


    final String SQL_CREATE_TABLE = "CREATE TABLE " + StateEntry.TABLE_NAME + " (" +
            StateEntry._ID + " INTEGER PRIMARY KEY, " +
            StateEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            StateEntry.COLUMN_DATE + " TEXT NOT NULL, " +
            StateEntry.COLUMN_ORDER_BY + " TEXT NOT NULL " + " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StateEntry.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
