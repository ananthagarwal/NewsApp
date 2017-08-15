package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_selection);

        newsSources = initializeNewsSources();
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        adapter = new NewsSelectionCustomAdapter(newsSources, getApplicationContext(), this);
        newsSelectionListView = (ListView) findViewById(R.id.newsSelection);
        newsSelectionListView.setAdapter(adapter);


        int exists = sharedPreferences.getInt(newsSources.get(0).getName(), Integer.MIN_VALUE);
        if (exists != Integer.MIN_VALUE) {
            for (NewsSource newsSource : newsSources) {
                newsSource.setPriority(sharedPreferences.getInt(newsSource.getName(), 0));
            }
        }


    }

    public ArrayList<NewsSource> initializeNewsSources() {
        ArrayList<NewsSource> result = new ArrayList<>();
        newsArray = getApplicationContext().getResources().getStringArray(R.array.newsSources);
        logoImages = getApplicationContext().getResources().getStringArray(R.array.newsLogos);

        for (int i = 0; i < newsArray.length; i++) {
            result.add(new NewsSource(newsArray[i], logoImages[i]));
        }
        return result;
    }

    public void done(View view) {
        Collections.sort(newsSources);
        for (NewsSource newsSource: newsSources) {
            editor.putInt(newsSource.getName(), newsSource.getPriority());
            editor.commit();
        }
        NewsSource.setSortedNewsSources(newsSources);
        finish();
    }
 }
