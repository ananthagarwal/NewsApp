package com.example.news;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public String TAG = "Twitter Main Activity";
    HashMap<Character, String> percentEncoding;
    TwitterLoginButton loginButton;
    TwitterSession twitterSession;
    TwitterApiClient twitterApiClient;
    String token; String secret; String header;
    String url = "https://api.twitter.com/1.1/trends/place.json?id=1";
    JSONObject twitterTrendsObj;
    ArrayList<TrendingObj> trendingObjArrayList = new ArrayList<>();
    TextView encourage;
    TrendingSelectionCustomAdapter trendingSelectionCustomAdapter;

    static final int SELECT_NEWS = 1;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateHashMap();

        encourage = (TextView) findViewById(R.id.logInEncourage);
        listView = (ListView) findViewById(R.id.trend);
        listView.setVisibility(View.INVISIBLE);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        trendingSelectionCustomAdapter = new TrendingSelectionCustomAdapter(trendingObjArrayList,
                getApplicationContext(), this);
        listView.setAdapter(trendingSelectionCustomAdapter);

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                Log.d(TAG, "Twitter login successful");
                encourage.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = twitterSession.getAuthToken();
                Log.d(TAG, "Username " + twitterSession.getUserName());
                token = authToken.token;
                secret = authToken.secret;

                header = accessRestAPI(url);
                new RetrieveTwitterTrend().execute(url);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.d(TAG, "Twitter login failed");

            }
        });
    }

    private void generateHashMap() {
        percentEncoding = new HashMap<>();
        percentEncoding.put(' ', "%20"); percentEncoding.put('!', "%21"); percentEncoding.put('\"', "%22");
        percentEncoding.put('#', "%23"); percentEncoding.put('$', "%24"); percentEncoding.put('%', "%25");
        percentEncoding.put('&', "%26"); percentEncoding.put('\'', "%27"); percentEncoding.put('(', "%28");
        percentEncoding.put(')', "%29"); percentEncoding.put('*', "%2A"); percentEncoding.put('+', "%2B");
        percentEncoding.put(',', "%2C"); percentEncoding.put('-', "%2D"); percentEncoding.put('.', "%2E");
        percentEncoding.put('/', "%2F"); percentEncoding.put('=', "%3D");
    }

    private String percentEncode(String original) {
        String answer = "";
        original = original.substring(0, original.length() - 1);
        for (int i = 0; i < original.length(); i++) {
            if (percentEncoding.containsKey(original.charAt(i))) {
                answer = answer + percentEncoding.get(original.charAt(i));
            } else {
                answer = answer + original.charAt(i);
            }
        }
        return answer;
    }

    public void selectNews(View view) {
        Intent intent = new Intent(this, NewsSelection.class);
        startActivityForResult(intent, SELECT_NEWS);
    }

    private String accessRestAPI(String url) {

        String unixTime = Long.toString(System.currentTimeMillis() / 1000L);
        String consumerKey = getString(R.string.com_twitter_sdk_android_CONSUMER_KEY);
        String nonce = "kYjzNBV8Y0ZFnhxSebBDvY3uYSQ2iaomaeew2VS4dg";
        String signature = "GET&https%3A%2F%2Fapi.twitter.com%2F1.1%2Ftrends%2Fplace.json&id%3D1%26" +
                "oauth_consumer_key%3D"+ consumerKey +"%26oauth_nonce%3D" + nonce +
                "%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D"+ unixTime +"%26oauth_token%3D"+ token +"%26oauth_version" +
                "%3D1.0"; //Signature is complete

        String signingKey = "TCyar7WDr0JEXm4zBQ36BIUpo3g6RHbCyFIJHYUxIfnMdHu5Qv&" + secret;
        String oauthsig = Base64.encodeToString(HmacUtils.hmacSha1(signingKey, signature), Base64.DEFAULT);
        Log.d(TAG, oauthsig);
        Log.d(TAG, Integer.toString(oauthsig.length()));
        oauthsig = percentEncode(oauthsig);
        Log.d(TAG, oauthsig);
        String head = "OAuth oauth_consumer_key=\""+consumerKey+"\", oauth_nonce=\"" + nonce +
                "\", oauth_signature=\"" + oauthsig + "\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp" +
                "=\"" + unixTime + "\", oauth_token=\"" + token + "\", oauth_version=\"1.0\"";

        return head;
    }

    public void parseTwitterTrending() throws JSONException {
        JSONArray trendingObjects = twitterTrendsObj.getJSONArray("trends");
        for (int i = 0; i < trendingObjects.length(); i++) {
            final JSONObject trendingObj = trendingObjects.getJSONObject(i);
            final String temp = (String) trendingObj.get("name");
            int tweetVolume;
            if (!(trendingObj.get("tweet_volume") instanceof Integer)) {
                tweetVolume = 0;
            } else {
                tweetVolume = trendingObj.getInt("tweet_volume");
            }
            final TrendingObj trending = new TrendingObj(trendingObj.getString("name"), trendingObj.getString("url"),
                   trendingObj.getString("promoted_content"), trendingObj.getString("query"),
                   tweetVolume);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final TrendingObj t = trending;
                    trendingObjArrayList.add(t);
                    trendingSelectionCustomAdapter.notifyDataSetChanged();

                }
            });
        }

    }

    class RetrieveTwitterTrend extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Authorization", header);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();
                Log.d(TAG, httpURLConnection.toString());

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                Log.d(TAG, "GET response code: " + String.valueOf(httpURLConnection.getResponseCode()));
                Log.d(TAG, "JSON response: " + response.toString());
                String httpresponse = response.substring(1, response.length() - 1);
                twitterTrendsObj = new JSONObject(httpresponse);
                parseTwitterTrending();
                httpURLConnection.disconnect();
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_NEWS) {
            if (resultCode == RESULT_OK) {
                trendingSelectionCustomAdapter.notifyDataSetChanged();
            }

        }

    }
}





