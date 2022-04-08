package com.example.medkit2006;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.medkit2006.entity.Post;

import java.util.ArrayList;

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
                    R.layout.feed_tile, parent, false);
        }
        Post current = getItem(position);
        TextView question = (TextView) listItemView.findViewById(R.id.question);
        question.setText(current.getQuestion());

        TextView username = (TextView) listItemView.findViewById(R.id.userId);
        username.setText(current.getUsername());

        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(current.getDate());

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }

}
