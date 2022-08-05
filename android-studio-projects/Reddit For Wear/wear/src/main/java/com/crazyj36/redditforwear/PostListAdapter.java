package com.crazyj36.redditforwear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class PostListAdapter extends ArrayAdapter<String> {
    public PostListAdapter(Context context, ArrayList<String> posts) {
        super(context, 0, posts);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String recentPostsTitles = MainWearActivity.recentPostsTitles.get(position);
        String recentPostsSubs = MainWearActivity.recentPostsSubs.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recent_posts_list, parent, false);
        }
        TextView titleTextView = convertView.findViewById(R.id.title);
        TextView  subTextView = convertView.findViewById(R.id.sub);
        titleTextView.setText(recentPostsTitles);
        subTextView.setText(recentPostsSubs);
        return convertView;
    }
}
