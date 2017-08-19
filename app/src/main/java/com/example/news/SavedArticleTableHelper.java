package com.example.news;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ananthagarwal on 8/18/17.
 */

public class SavedArticleTableHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Article.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SavedArticleTable.ArticleEntry.TABLE_NAME + " (" +
                    SavedArticleTable.ArticleEntry._ID + " INTEGER PRIMARY KEY, " +
                    SavedArticleTable.ArticleEntry.COLUMN_NAME_TITLE + " TEXT, " +
                    SavedArticleTable.ArticleEntry.COLUMN_NAME_SOURCE + " TEXT, " +
                    SavedArticleTable.ArticleEntry.COLUMN_NAME_SUMMARY + " TEXT, " +
                    SavedArticleTable.ArticleEntry.COLUMN_NAME_URL + " TEXT, " +
                    SavedArticleTable.ArticleEntry.COLUMN_NAME_IMAGE_URL + " TEXT" + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SavedArticleTable.ArticleEntry.TABLE_NAME;


    public SavedArticleTableHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}