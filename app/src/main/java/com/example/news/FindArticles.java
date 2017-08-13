package com.example.news;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FindArticles extends AppCompatActivity {

    TrendingObj trendingObj;
    TextView reminder;
    ArrayList<NewsSource> newsSources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_articles);
        reminder = (TextView) findViewById(R.id.reminder);
        newsSources = NewsSource.getSortedNewsSources();
        if (newsSources == null) {
            reminder.setVisibility(View.VISIBLE);
            reminder.setText("Don't Forget to Set Your News Source Preferences First!");
        } else {
            reminder.setVisibility(View.INVISIBLE);
        }
        Intent intent = getIntent();
        trendingObj = intent.getParcelableExtra("Subject");

        String url = "https://newsapi.org/v1/articles?source=the-new-york-times&sortBy=top&apiKey=1ee07b935e3145039b09ca71535421a0";
        new RetrieveNewsArticles().execute(url);

    }

    class RetrieveNewsArticles extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                Log.d(TrendingSelectionCustomAdapter.TAG, "GET response code: " + String.valueOf(httpURLConnection.getResponseCode()));
                Log.d(TrendingSelectionCustomAdapter.TAG, "JSON response: " + response.toString());
                String httpresponse = response.substring(1, response.length() - 1);
                //twitterTrendsObj = new JSONObject(httpresponse);
                //parseTwitterTrending();
                httpURLConnection.disconnect();
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
