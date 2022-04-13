package com.example.medkit2006.adapter;

import android.app.Activity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.medkit2006.R;
import com.example.medkit2006.entity.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Student on 4/29/2017.
 */

public class FeedAdapter extends ArrayAdapter<Post>{

    public FeedAdapter(Activity context, ArrayList<Post> feeds)
    {
        super(context,0,feeds);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.tile_post, parent, false);
        }
        Post current = getItem(position);
        TextView question = listItemView.findViewById(R.id.question);
        question.setText(current.getTitle());

        TextView facility = listItemView.findViewById(R.id.post_facility);
        facility.setText(current.getFacility());

        TextView username = listItemView.findViewById(R.id.userId);
        username.setText(current.getUsername());

        TextView date = listItemView.findViewById(R.id.date);
        try {
            date.setText(DateUtils.getRelativeTimeSpanString(Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd H:m:s", Locale.US).parse(current.getDate())).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listItemView;
    }

}
