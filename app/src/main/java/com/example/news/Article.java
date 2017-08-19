package com.example.news;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ananthagarwal on 8/12/17.
 */

public class Article implements Parcelable{

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

    public Article(Parcel in) {
        title = in.readString();
        source = in.readParcelable(NewsSource.class.getClassLoader());
        summary = in.readString();
        url = in.readString();
        urlToImage = in.readString();

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



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeParcelable(source, 0);
        dest.writeString(summary);
        dest.writeString(url);
        dest.writeString(urlToImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}

