package com.example.news;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ananthagarwal on 7/30/17.
 */

public class TrendingObj implements Parcelable{
    String name;
    String url;
    String promotedContent;
    String query;
    int tweetVolume;

    public TrendingObj(String n, String u, String p, String q, int tv){
        name = n; url = u; promotedContent = p; query = q; tweetVolume = tv;
    }

    protected TrendingObj(Parcel in) {
        name = in.readString();
        url = in.readString();
        promotedContent = in.readString();
        query = in.readString();
        tweetVolume = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(promotedContent);
        dest.writeString(query);
        dest.writeInt(tweetVolume);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrendingObj> CREATOR = new Creator<TrendingObj>() {
        @Override
        public TrendingObj createFromParcel(Parcel in) {
            return new TrendingObj(in);
        }

        @Override
        public TrendingObj[] newArray(int size) {
            return new TrendingObj[size];
        }
    };

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
        return Integer.toString(tweetVolume);
    }

    public String getUrl() {
        return url;
    }
}
