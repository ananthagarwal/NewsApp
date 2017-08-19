package com.example.news;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ananthagarwal on 8/7/17.
 */

public class NewsSource implements Comparable<NewsSource>, Parcelable{

    String name;
    String logoLink;
    boolean selected;
    int priority;

    public NewsSource(String name, String logoLink, int priority) {
        this.name = name;
        this.logoLink = logoLink;
        selected = false;
        this.priority = priority;
    }

    public NewsSource(Parcel in) {
        name = in.readString();
        logoLink = in.readString();
        priority = in.readInt();
    }


    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(NewsSource o1) {
        return this.getPriority() - o1.getPriority();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(logoLink);
        dest.writeInt(priority);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NewsSource> CREATOR = new Creator<NewsSource>() {
        @Override
        public NewsSource createFromParcel(Parcel in) {
            return new NewsSource(in);
        }

        @Override
        public NewsSource[] newArray(int size) {
            return new NewsSource[size];
        }
    };


}
