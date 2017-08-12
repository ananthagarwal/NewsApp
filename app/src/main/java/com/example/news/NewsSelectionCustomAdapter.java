package com.example.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ananthagarwal on 8/7/17.
 */

public class NewsSelectionCustomAdapter extends ArrayAdapter<NewsSource> implements View.OnClickListener {

    private ArrayList<NewsSource> dataBase;
    private Context mContext;
    private static LayoutInflater inflater = null;
    private NewsSelection newsSelection;
    ArrayAdapter<CharSequence> arrayAdapter;

    public static class ViewHolder {
        ImageView newsLogo;
        TextView newsSourceName;
        Spinner priority;
    }

    public NewsSelectionCustomAdapter(ArrayList<NewsSource> data, Context context, NewsSelection news) {
        super(context, R.layout.row_item, data);
        dataBase = data;
        mContext = context;
        newsSelection = news;
        arrayAdapter = ArrayAdapter.createFromResource(context,
                R.array.priorities, android.R.layout.simple_spinner_item);
    }

    @Override
    public void onClick(View view) {

        int position = (int) view.getTag();
        NewsSource item = (NewsSource) getItem(position);


        switch (view.getId()) {
            case R.id.newsPriority:
                break;

        }

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsSource newsSource = getItem(position);
        ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.newsSourceName = (TextView) convertView.findViewById(R.id.newsSource);
            viewHolder.priority = (Spinner) convertView.findViewById(R.id.newsPriority);
            viewHolder.newsLogo = (ImageView) convertView.findViewById(R.id.newsLogo);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.newsSourceName.setText(newsSource.getName());
        viewHolder.newsLogo.setImageResource(getContext().getResources()
                .getIdentifier(newsSource.getLogoLink(),"drawable", getContext().getPackageName()));
        viewHolder.priority.setAdapter(arrayAdapter);

        return convertView;

    }

}
