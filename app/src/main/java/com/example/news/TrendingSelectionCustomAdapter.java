package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ananthagarwal on 8/11/17.
 */

public class TrendingSelectionCustomAdapter extends ArrayAdapter<TrendingObj> implements View.OnClickListener{

    private ArrayList<TrendingObj> dataBase;
    private Context mContext;
    private static LayoutInflater inflater = null;
    private MainActivity mainActivity;

    public static class ViewHolder {
        TextView trending;
        TextView tweetVolume;
        Button findArticles;
    }

    public TrendingSelectionCustomAdapter(ArrayList<TrendingObj> data, Context context, MainActivity m) {
        super(context, R.layout.trending_row_item, data);
        dataBase = data;
        mContext = context;
        mainActivity = m;
    }

    @Override
    public void onClick(View view) {

        int position = (int) view.getTag();
        TrendingObj item = (TrendingObj) getItem(position);

        switch (view.getId()) {
            case R.id.findArticles:
                Intent intent = new Intent(getContext(), NewsSelection.class);
                getContext().startActivity(intent);

        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrendingObj trendingObj = getItem(position);
        TrendingSelectionCustomAdapter.ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new TrendingSelectionCustomAdapter.ViewHolder();
            inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.trending_row_item, parent, false);
            viewHolder.trending = (TextView) convertView.findViewById(R.id.trending);
            viewHolder.tweetVolume = (TextView) convertView.findViewById(R.id.tweetVol);
            viewHolder.findArticles = (Button) convertView.findViewById(R.id.findArticles);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TrendingSelectionCustomAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.trending.setText(trendingObj.getName());
        viewHolder.tweetVolume.setText(trendingObj.getTweetVolume());

        return convertView;

    }

}