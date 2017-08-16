package com.example.news;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ananthagarwal on 8/7/17.
 */

public class NewsSource implements Comparable<NewsSource>{

    String name;
    String logoLink;
    boolean selected;
    int priority;
    static ArrayList<NewsSource> sortedNewsSources;

    public NewsSource(String name, String logoLink) {
        this.name = name;
        this.logoLink = logoLink;
        selected = false;
        priority = 0;
//        sortedNewsSources.add(this);
//        Collections.sort(sortedNewsSources);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isSelected() {
        return selected;
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

    public static void setSortedNewsSources(ArrayList<NewsSource> sortedNewsSources) {
        NewsSource.sortedNewsSources = sortedNewsSources;
    }

    public static ArrayList<NewsSource> getSortedNewsSources() {
        return sortedNewsSources;
    }
}
