package com.example.news;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aylien.newsapi.ApiClient;
import com.aylien.newsapi.ApiException;
import com.aylien.newsapi.Configuration;
import com.aylien.newsapi.api.DefaultApi;
import com.aylien.newsapi.auth.ApiKeyAuth;
import com.aylien.newsapi.models.Stories;
import com.aylien.newsapi.models.Story;
import com.aylien.newsapi.parameters.StoriesParams;

import org.json.JSONObject;
import org.w3c.dom.Text;

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
    ArrayList<NewsSource> newsSources;
    ApiClient defaultClient;
    ArrayList<Article> articles;
    List<String> sourceNames;
    FindArticlesCustomAdapter adapter;
    ListView listView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ArticleViewFragment fragment;
    Article selectedArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_articles);
        reminder = (TextView) findViewById(R.id.reminder);
        sourceNames = new ArrayList<>();
        newsSources = filter(NewsSelection.sortedNewsSources);
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
        fragmentManager = getFragmentManager();

        fragment = new ArticleViewFragment();


        listView = (ListView) findViewById(R.id.articles);
        adapter = new FindArticlesCustomAdapter(articles, getApplicationContext(), this);
        listView.setAdapter(adapter);

        String url = "https://newsapi.org/v1/articles?source=the-new-york-times&sortBy=top&apiKey=1ee07b935e3145039b09ca71535421a0";
        new RetrieveNewsArticles().execute(url);
    }

    public ArrayList<NewsSource> filter(ArrayList<NewsSource> original) {
        Iterator<NewsSource> iter = original.iterator();

        while (iter.hasNext()) {
            NewsSource newsSource = iter.next();
            if (newsSource.getPriority() == 0) {
                iter.remove();
            } else {
                sourceNames.add(newsSource.getName());
            }
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
        return new NewsSource(name, "", 0);
    }

    public void launchArticle() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();


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
                storiesBuilder.setTitle(trendObjName);
                storiesBuilder.setLanguage(Arrays.asList("en"));
                storiesBuilder.setPublishedAtStart("NOW-7DAYS");
                storiesBuilder.setSourceName(sourceNames);

                try {
                    Stories result = apiInstance.listStories(storiesBuilder.build());
                    for (Iterator<Story> i = result.getStories().iterator(); i.hasNext();){
                        Story story = i.next();
                        final Article newArticle = new Article(story.getTitle(), findNewsSource(newsSources,
                                story.getSource().getName()), story.getSummary().toString(), story.getLinks().getPermalink(),
                                story.getMedia().get(0).getUrl());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Article a = newArticle;
                                articles.add(a);
                                adapter.notifyDataSetChanged();
                            }
                        });

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
    public void setArticle(Article art) {
        selectedArticle = art;
        Bundle bundle = new Bundle();
        bundle.putParcelable("Article", art);
        fragment.setArguments(bundle);
    }

    public void searchTweet(View view) {
        Intent intent = new Intent(this, TweetSearch.class);
        intent.putExtra("Subject", trendObjName);
        startActivity(intent);
    }




    public static class ArticleViewFragment extends Fragment implements View.OnClickListener {
        WebView webView;
        View view;
        ProgressDialog progressDialog;
        SavedArticleTableHelper savedArticleTableHelper;
        SQLiteDatabase db;
        Article article;
        Button button;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            savedArticleTableHelper = new SavedArticleTableHelper(getActivity().getApplicationContext());
            view = inflater.inflate(R.layout.article_fragment, container, false);
//            progressDialog = ProgressDialog.show(getActivity(), "Loading","Please wait...", true);
//            progressDialog.setCancelable(false);
//            progressDialog.show();
            button = (Button) view.findViewById(R.id.save);
            webView = (WebView) view.findViewById(R.id.webView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setUseWideViewPort(true);
            WebSettings webSettings = webView.getSettings();

            webView.setWebViewClient(new MyCustomWebViewClient());
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            Bundle bundle = getArguments();
            article = bundle.getParcelable("Article");
            String url = article.getUrl();

            button.setOnClickListener(this);

            webView.loadUrl(url);
            return view;

        }

        @Override
        public void onClick(View v) {
            db = savedArticleTableHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SavedArticleTable.ArticleEntry.COLUMN_NAME_TITLE, article.getTitle());
            values.put(SavedArticleTable.ArticleEntry.COLUMN_NAME_SOURCE, article.getSource().getName());
            values.put(SavedArticleTable.ArticleEntry.COLUMN_NAME_SUMMARY, article.getSummary());
            values.put(SavedArticleTable.ArticleEntry.COLUMN_NAME_URL, article.getUrl());
            values.put(SavedArticleTable.ArticleEntry.COLUMN_NAME_IMAGE_URL, article.getUrlToImage());

            // Insert the new row, returning the primary key value of the new row
            db.insert(SavedArticleTable.ArticleEntry.TABLE_NAME, null, values);
            Toast.makeText(getActivity(), "Saved Article!", Toast.LENGTH_LONG).show();
        }

        private class MyCustomWebViewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //progressDialog.dismiss();

            }

        }



    }


}
