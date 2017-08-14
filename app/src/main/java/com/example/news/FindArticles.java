package com.example.news;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aylien.newsapi.ApiClient;
import com.aylien.newsapi.ApiException;
import com.aylien.newsapi.Configuration;
import com.aylien.newsapi.api.DefaultApi;
import com.aylien.newsapi.auth.ApiKeyAuth;
import com.aylien.newsapi.models.Stories;
import com.aylien.newsapi.models.Story;
import com.aylien.newsapi.parameters.StoriesParams;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FindArticles extends AppCompatActivity {

    TrendingObj trendingObj;
    String trendObjName;
    TextView reminder;
    List<NewsSource> newsSources;
    ApiClient defaultClient;
    ArrayList<Article> articles;
    List<String> sourceNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_articles);
        reminder = (TextView) findViewById(R.id.reminder);
        newsSources = filter(NewsSource.getSortedNewsSources());
        if (newsSources == null) {
            reminder.setVisibility(View.VISIBLE);
            reminder.setText("Don't Forget to Set Your News Source Preferences First!");
        } else {
            reminder.setVisibility(View.INVISIBLE);
        }
        Intent intent = getIntent();
        trendingObj = intent.getParcelableExtra("Subject");
        trendObjName = removeHashtag(trendingObj.getName());
        articles = new ArrayList<>();

        String url = "https://newsapi.org/v1/articles?source=the-new-york-times&sortBy=top&apiKey=1ee07b935e3145039b09ca71535421a0";
        new RetrieveNewsArticles().execute(url);
    }

    public List<NewsSource> filter(ArrayList<NewsSource> original) {
        for (NewsSource source: original) {
            if (source.getPriority() != 0) {
                original.remove(source);
            }
        }
        sourceNames = new ArrayList<>();
        for (NewsSource source: original) {
            sourceNames.add(source.getName());
        }
        return original;
    }

    public String removeHashtag(String original) {
        if (original.contains("#")) {
            original = original.substring(1);
        }
        return original;
    }

    public NewsSource findNewsSource(ArrayList<NewsSource> original, String name) {
        for (NewsSource news: original) {
            if (news.getName().equals(name)) {
                return news;
            }
        }
        return null;
    }

    class RetrieveNewsArticles extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... urls) {
            try {
                defaultClient = Configuration.getDefaultApiClient();
                ApiKeyAuth app_id = (ApiKeyAuth) defaultClient.getAuthentication("app_id");
                app_id.setApiKey(getString(R.string.AYLIEN_API_ID));

                ApiKeyAuth app_key = (ApiKeyAuth) defaultClient.getAuthentication("app_key");
                app_key.setApiKey(getString(R.string.AYLIEN_API_KEY));

                DefaultApi apiInstance = new DefaultApi();

                StoriesParams.Builder storiesBuilder = StoriesParams.newBuilder();
                storiesBuilder.setTitle("Russia");
                storiesBuilder.setLanguage(Arrays.asList("en"));
                storiesBuilder.setPublishedAtStart("NOW-7DAYS");
                storiesBuilder.setSourceName(Arrays.asList("BBC"));

                try {
                    Stories result = apiInstance.listStories(storiesBuilder.build());
                    for (Iterator<Story> i = result.getStories().iterator(); i.hasNext();){
                        Story story = i.next();
                        System.out.println(story.getMedia().toString());
//                        //Article newArticle = new Article(story.getTitle(), findNewsSource((ArrayList) newsSources,
//                                story.getSource().getName()), story.getSummary().toString(), story.getLinks().getPermalink(),
//                                story.getMedia())

                        System.out.println(story.toString());
                    }
                } catch (ApiException e) {
                    System.err.println("Exception when calling DefaultApi#listStories");
                    e.printStackTrace();
                }

//                URL url = new URL(urls[0]);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("GET");
//                httpURLConnection.setRequestProperty("Content-Type", "application/json");
//                httpURLConnection.connect();
//
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
//                StringBuilder response = new StringBuilder();
//                String line;
//
//                while ((line = bufferedReader.readLine()) != null) {
//                    response.append(line);
//                }
//                Log.d(TrendingSelectionCustomAdapter.TAG, "GET response code: " + String.valueOf(httpURLConnection.getResponseCode()));
//                Log.d(TrendingSelectionCustomAdapter.TAG, "JSON response: " + response.toString());
//                String httpresponse = response.substring(1, response.length() - 1);
//                //twitterTrendsObj = new JSONObject(httpresponse);
//                //parseTwitterTrending();
//                httpURLConnection.disconnect();
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
