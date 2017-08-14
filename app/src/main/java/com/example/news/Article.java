package com.example.news;

/**
 * Created by ananthagarwal on 8/12/17.
 */

public class Article {

    String title;
    NewsSource source;
    String summary;
    String url;
    String urlToImage;

    public Article(String title, NewsSource source, String summary, String url, String urlToImage) {
        this.title = title;
        this.source = source;
        this.summary = summary;
        this.url = url;
        this.urlToImage = urlToImage;
    }

    public String getUrl() {
        return url;
    }

    public String getSummary() {
        return summary;
    }

    public NewsSource getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }
}

