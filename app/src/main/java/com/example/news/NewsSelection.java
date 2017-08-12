package com.example.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class NewsSelection extends AppCompatActivity {

    String[] newsArray;
    String[] logoImages;
    NewsSelectionCustomAdapter adapter;
    ListView newsSelectionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_selection);

        ArrayList<NewsSource> newsSources = initializeNewsSources();

        adapter = new NewsSelectionCustomAdapter(newsSources, getApplicationContext(), this);
        newsSelectionListView = (ListView) findViewById(R.id.newsSelection);
        newsSelectionListView.setAdapter(adapter);

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
        finish();
    }
 }
