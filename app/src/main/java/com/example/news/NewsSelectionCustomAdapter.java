package com.example.news;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ananthagarwal on 8/7/17.
 */

public class NewsSelectionCustomAdapter extends ArrayAdapter<NewsSource> implements View.OnClickListener {

    private ArrayList<NewsSource> dataBase;
    private Context mContext;
    private static LayoutInflater inflater = null;
    private NewsSelection newsSelection;

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
    }

    @Override
    public void onClick(View view) {

        int position = (int) view.getTag();
        NewsSource item = (NewsSource) getItem(position);

        switch (view.getId()) {
            case R.id.item_infobutton:
                if (purchaseActivity != null) {
                    purchaseActivity.editItem(view, item);
                } else {
                    returnActivity.editItem(view, item);
                }
                break;

        }

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.store = (TextView) convertView.findViewById(R.id.location);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.itemImage);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_infobutton);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.name.setText(item.getName());
        viewHolder.image.setImageBitmap(BitmapFactory.decodeFile(item.getImage()));
        viewHolder.store.setText(item.getStore());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);

        return convertView;

    }
}
