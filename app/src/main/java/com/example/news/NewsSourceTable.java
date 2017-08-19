package com.example.news;

import android.provider.BaseColumns;

/**
 * Created by ananthagarwal on 8/18/17.
 */

public final class NewsSourceTable {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private NewsSourceTable() {}

    /* Inner class that defines the table contents */
    public static class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "NewsSourcePreferences";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_LOGOLINK = "Logo";
        public static final String COLUMN_NAME_PRIORITY = "Priority";
    }
}
