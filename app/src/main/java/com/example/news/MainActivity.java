package com.example.news;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.apache.commons.codec.digest.HmacUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;


public class MainActivity extends AppCompatActivity {

    public String TAG = "Twitter Main Activity";

    TwitterLoginButton loginButton;
    TwitterSession twitterSession;
    TwitterApiClient twitterApiClient;
    String token; String secret; String header;
    String url = "https://api.twitter.com/1.1/trends/place.json?id=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                Log.d(TAG, "Twitter login successful");
                twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = twitterSession.getAuthToken();
                Log.d(TAG, "Username " + twitterSession.getUserName());
                token = authToken.token;
                secret = authToken.secret;

                Log.d(TAG, token);
                Log.d(TAG, secret);
                String consumerKey = "8offNEtRlkvLzjzg7mb7HKKmS";
                String nonce = "kYjzDWR8Y0ZFnhxSebBDvY3uYSQ2pwgmaeew2VS4dg";
                String signature = "GET&https%3A%2F%2Fapi.twitter.com%2F1.1%2Ftrends%2Fplace.json&id%3D1%26" +
                        "oauth_consumer_key%3D8offNEtRlkvLzjzg7mb7HKKmS%26oauth_nonce%3DkYjzDWR8Y0ZFnhxSebBDvY3uYSQ2pwgmaeew2VS4dg" +
                        "%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1501381586%26oauth_token%3D"+token+"%26oauth_version" +
                        "%3D1.0"; //Signature is complete
                String signatureMethod = "HMAC-SHA1";
                String signingKey = "TCyar7WDr0JEXm4zBQ36BIUpo3g6RHbCyFIJHYUxIfnMdHu5Qv&" + secret;
                String timestamp = "1501381586";
                String version = "1.0";
                String parameterstring = "id=1&oauth_consumer_key=8offNEtRlkvLzjzg7mb7HKKmS&oauth_nonce=" +
                        "kYjzDWR8Y0ZFnhxSebBDvY3uYSQ2pwgmaeew2VS4dg&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" +
                        "1501381586&oauth_token="+token+"&oauth_version=1.0";
                String oauthsig = Base64.encodeToString(HmacUtils.hmacSha1(signingKey, signature), Base64.DEFAULT);
                Log.d(TAG, token + " " + secret);
                Log.d(TAG, oauthsig);
                String sig = "1lrJjo2Fa+lhwBe5kAopWGnhmq4=";

                header = "OAuth oauth_consumer_key=\"8offNEtRlkvLzjzg7mb7HKKmS\", oauth_nonce=\"kYjzDWR8Y0ZFnhxSebBDvY3uYSQ2pwgmaeew2VS4dg" +
                        "\", oauth_signature=\"1lrJjo2Fa%2BlhwBe5kAopWGnhmq4%3D\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp" +
                        "=\"1501381586\", oauth_token=\"" + token + "\", oauth_version=\"1.0\"";

                new RetrieveTwitterTrend().execute(url);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.d(TAG, "Twitter login failed");

            }
        });







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
                    Log.d(TAG, line);
                }
                Log.d(TAG,
                        "GET response code: "
                                + String.valueOf(httpURLConnection
                                .getResponseCode()));
                Log.d(TAG, "JSON response: " + response.toString());
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
    }
}
