package com.example.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ananthagarwal on 8/7/17.
 */

public class NewsSelectionCustomAdapter extends ArrayAdapter<NewsSource> implements View.OnClickListener {

    private ArrayList<NewsSource> dataBase;
    private Context mContext;
    private static LayoutInflater inflater = null;
    private NewsSelection newsSelection;
    ArrayAdapter<CharSequence> arrayAdapter;
    HashMap<String, Integer> priorityMap;

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
        priorityMap = new HashMap<>();
        priorityMap.put("Very Low - Remove", 0);
        priorityMap.put("Low", 1);
        priorityMap.put("Neutral", 2);
        priorityMap.put("High", 3);
        priorityMap.put("Very High", 4);

    }

    @Override
    public void onClick(View view) {

        int position = (int) view.getTag();
        NewsSource item = (NewsSource) getItem(position);

        switch (view.getId()) {
            case R.id.newsPriority:

        }

    }

    public int matchPriority(String priority) {
        return priorityMap.get(priority);
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
        viewHolder.newsLogo.setImageResource(mContext.getResources()
                .getIdentifier(newsSource.getLogoLink(),"drawable", mContext.getPackageName()));
        viewHolder.priority.setAdapter(arrayAdapter);
        viewHolder.priority.setOnItemSelectedListener(new YourSpinnerListener(position));
        viewHolder.priority.setSelection(newsSource.getPriority());

        Log.d(TrendingSelectionCustomAdapter.TAG, newsSource.getName() + Integer.toString(newsSource.getPriority()));

        return convertView;

    }

    private class YourSpinnerListener implements AdapterView.OnItemSelectedListener {

        private int mSpinnerPosition;

        public YourSpinnerListener(int spinnerPosition) {
            mSpinnerPosition = spinnerPosition;
        }

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            NewsSource newsSource = getItem(mSpinnerPosition);
            newsSource.setPriority((Integer) matchPriority((String) arg0.getItemAtPosition(arg2)));
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    }

}
