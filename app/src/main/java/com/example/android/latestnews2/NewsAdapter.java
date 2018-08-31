package com.example.android.latestnews2;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gikonyo hannah on 6.06.2018.
 */

public class NewsAdapter extends ArrayAdapter<NewsUpdate> {

    private static final String DATE_SEPARATOR = "T";

    public NewsAdapter(Activity context, List<NewsUpdate> articles) {
        super(context, 0, articles);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_items, parent, false);
        }

        NewsUpdate currentArticle = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.title);

        titleView.setText(currentArticle.getTitle());

        TextView authorView = (TextView) listItemView.findViewById(R.id.byLine);

        authorView.setText(currentArticle.getAuthor());

        TextView sectionView = (TextView) listItemView.findViewById(R.id.sectionName);

        sectionView.setText(currentArticle.getSection());

        String originalDate = currentArticle.getDate();

        String[] parts = originalDate.split(DATE_SEPARATOR);

        String date = parts[0];

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        dateView.setText(date);

        TextView imageView = (TextView) listItemView.findViewById(R.id.image);

        imageView.setText(currentArticle.getImage());

        return listItemView;
    }
}


