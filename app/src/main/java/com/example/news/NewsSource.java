package com.example.news;

/**
 * Created by ananthagarwal on 8/7/17.
 */

public class NewsSource {

    String name;
    String logoLink;
    boolean selected;
    int priority;

    public NewsSource(String name, String logoLink) {
        this.name = name;
        this.logoLink = logoLink;
        selected = false;
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
}
