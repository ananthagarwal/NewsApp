package com.example.news;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class NewsSelection extends AppCompatActivity {

    String[] newsArray;
    String[] logoImages;
    NewsSelectionCustomAdapter adapter;
    ListView newsSelectionListView;
    ArrayList<NewsSource> newsSources;
    NewsSourceTableHelper mDbHelper;
    static ArrayList<NewsSource> sortedNewsSources;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_selection);

        mDbHelper = new NewsSourceTableHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();

        newsSources = initializeNewsSources();

        adapter = new NewsSelectionCustomAdapter(newsSources, getApplicationContext(), this);
        newsSelectionListView = (ListView) findViewById(R.id.newsSelection);
        newsSelectionListView.setAdapter(adapter);


    }

    public ArrayList<NewsSource> initializeNewsSources() {
        ArrayList<NewsSource> result = new ArrayList<>();
        newsArray = getApplicationContext().getResources().getStringArray(R.array.newsSources);
        logoImages = getApplicationContext().getResources().getStringArray(R.array.newsLogos);
        if (tableExists()) {
            db = mDbHelper.getReadableDatabase();
            Cursor mCursor = db.rawQuery("SELECT * FROM " + NewsSourceTable.NewsEntry.TABLE_NAME, null);
            while(mCursor.moveToNext()) {
                result.add(new NewsSource(mCursor.getString(mCursor.getColumnIndex(NewsSourceTable.NewsEntry.COLUMN_NAME_NAME)),
                                mCursor.getString(mCursor.getColumnIndex(NewsSourceTable.NewsEntry.COLUMN_NAME_LOGOLINK)),
                        Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(NewsSourceTable.NewsEntry.COLUMN_NAME_PRIORITY)))));
            }
        } else {
            for (int i = 0; i < newsArray.length; i++) {
                result.add(new NewsSource(newsArray[i], logoImages[i], 0));
            }
        }

        return result;
    }

    public boolean tableExists() {
        Cursor mCursor = db.rawQuery("SELECT * FROM " + NewsSourceTable.NewsEntry.TABLE_NAME, null);
        return mCursor.moveToFirst();
    }

    public void done(View view) {

        db = mDbHelper.getWritableDatabase();
        if (!tableExists()) {
            createTable();
        } else {
            updateTable();
        }
        Collections.sort(newsSources);
        sortedNewsSources = newsSources;
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void createTable() {
        for (NewsSource newsSource : newsSources) {
            ContentValues values = new ContentValues();
            values.put(NewsSourceTable.NewsEntry.COLUMN_NAME_NAME, newsSource.getName());
            values.put(NewsSourceTable.NewsEntry.COLUMN_NAME_LOGOLINK, newsSource.getLogoLink());
            values.put(NewsSourceTable.NewsEntry.COLUMN_NAME_PRIORITY, Integer.toString(newsSource.getPriority()));
            db.insert(NewsSourceTable.NewsEntry.TABLE_NAME, null, values);
        }
    }
    public void updateTable() {
        for (NewsSource newsSource : newsSources) {
            ContentValues values = new ContentValues();
            values.put(NewsSourceTable.NewsEntry.COLUMN_NAME_PRIORITY, Integer.toString(newsSource.getPriority()));

            String selection = NewsSourceTable.NewsEntry.COLUMN_NAME_NAME + " LIKE ?";
            String[] selectionArgs = { newsSource.getName() };

            db.update(NewsSourceTable.NewsEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }

    }

 }
