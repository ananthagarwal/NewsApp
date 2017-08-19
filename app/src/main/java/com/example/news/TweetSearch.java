package com.example.news;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TweetSearch extends AppCompatActivity {

    String tweetSubject;
    String baseUrl;
    String TAG = "NewsApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_search);

        baseUrl = "https://api.twitter.com/1.1/search/tweets.json?";
        Intent intent = getIntent();
        tweetSubject = intent.getStringExtra("Subject");

        tweetSubject = MainActivity.percentEncode(tweetSubject, false);
        baseUrl = baseUrl + "q=" + tweetSubject + "&result_type=recent";
        Log.d(TAG, baseUrl);

        new RetrieveTweets().execute(baseUrl);


    }

    class RetrieveTweets extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Authorization", MainActivity.header);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                Log.d(TAG, "GET response code: " + String.valueOf(httpURLConnection.getResponseCode()));
                Log.d(TAG, "JSON response: " + response.toString());
                String httpresponse = response.substring(1, response.length() - 1);
//                twitterTrendsObj = new JSONObject(httpresponse);
//                parseTwitterTrending();
                httpURLConnection.disconnect();
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
