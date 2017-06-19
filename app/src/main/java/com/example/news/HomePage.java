package com.example.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.FacebookDialog;
import com.facebook.FacebookSdk;
import com.twitter.sdk.android.core.Twitter;


import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Twitter.initialize(this);
=
    }
}
