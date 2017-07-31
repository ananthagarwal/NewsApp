package com.example.news;

/**
 * Created by ananthagarwal on 7/30/17.
 */

public class TrendingObj {
    String name;
    String url;
    String promotedContent;
    String query;
    String tweetVolume;

    public TrendingObj(String n, String u, String p, String q, String tv){
        name = n; url = u; promotedContent = p; query = q; tweetVolume = tv;
    }

    public String getName() {
        return name;
    }

    public String getPromotedContent() {
        return promotedContent;
    }

    public String getQuery() {
        return query;
    }

    public String getTweetVolume() {
        return tweetVolume;
    }

    public String getUrl() {
        return url;
    }
}
