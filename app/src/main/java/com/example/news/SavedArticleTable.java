package com.example.news;

import android.provider.BaseColumns;

/**
 * Created by ananthagarwal on 8/18/17.
 */

public final class SavedArticleTable {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SavedArticleTable() {}

    /* Inner class that defines the table contents */
    public static class ArticleEntry implements BaseColumns {
        public static final String TABLE_NAME = "NewsSourcePreferences";
        public static final String COLUMN_NAME_TITLE = "Title";
        public static final String COLUMN_NAME_SOURCE = "Source";
        public static final String COLUMN_NAME_SUMMARY = "Summary";
        public static final String COLUMN_NAME_URL = "URL";
        public static final String COLUMN_NAME_IMAGE_URL = "Image_URL";
    }
}