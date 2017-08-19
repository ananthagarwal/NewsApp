package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Exchanger;

/**
 * Created by ananthagarwal on 8/13/17.
 */

public class FindArticlesCustomAdapter extends ArrayAdapter<Article>{

    private ArrayList<Article> dataBase;
    private Context mContext;
    private static LayoutInflater inflater = null;
    private FindArticles findArticles;
    private MainActivity mainActivity;

    public static class ViewHolder {
        ImageView articleImage;
        TextView newsSourceName;
        TextView title;
        TextView summary;
        Button launch;
    }

    public FindArticlesCustomAdapter(ArrayList<Article> data, Context context, FindArticles article) {
        super(context, R.layout.find_article_row_item, data);
        dataBase = data;
        mContext = context;
        findArticles = article;

    }

    public FindArticlesCustomAdapter(ArrayList<Article> data, Context context, MainActivity article) {
        super(context, R.layout.find_article_row_item, data);
        dataBase = data;
        mContext = context;
        mainActivity = article;
    }

//    @Override
//    public void onClick(View view) {
//
//        int position = (int) view.getTag();
//        Article item = (Article) getItem(position);
//
//        switch (view.getId()) {
//            case R.id.newsPriority:
//        }
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);
        FindArticlesCustomAdapter.ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new FindArticlesCustomAdapter.ViewHolder();
            inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.find_article_row_item, parent, false);
            viewHolder.articleImage = (ImageView) convertView.findViewById(R.id.articleImage);
            viewHolder.newsSourceName = (TextView) convertView.findViewById(R.id.newsSourceName);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.summary = (TextView) convertView.findViewById(R.id.summary);
            viewHolder.launch = (Button) convertView.findViewById(R.id.launch);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FindArticlesCustomAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }
        RetrieveImage retrieveImage = new RetrieveImage();
        retrieveImage.execute(article.getUrlToImage());
        viewHolder.articleImage.setImageDrawable(retrieveImage.getDrawable());
        viewHolder.newsSourceName.setText(article.getSource().getName());
        viewHolder.title.setText(article.getTitle());
        viewHolder.summary.setText(article.getSummary());
        viewHolder.launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article art = (Article) v.getTag();
                findArticles.launchArticle();
                findArticles.setArticle(art);
            }
        });
        viewHolder.launch.setTag(article);

        return convertView;
    }

    class RetrieveImage extends AsyncTask<String, Void, Boolean> {
        Drawable d;
        protected Boolean doInBackground(String... urls) {
            try {
                InputStream is = (InputStream) new URL(urls[0]).getContent();
                d = Drawable.createFromStream(is, "src name");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public Drawable getDrawable() {
            return d;
        }
    }

}
