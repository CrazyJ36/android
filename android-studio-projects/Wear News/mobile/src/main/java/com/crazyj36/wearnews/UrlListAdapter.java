package com.crazyj36.wearnews;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class UrlListAdapter extends ArrayAdapter<String> {
    public UrlListAdapter(Context context, ArrayList<String> posts) {
        super(context, 0, posts);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String urlTitleString = MainPhoneActivity.urls.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.url_list, parent, false);
        }
        TextView urlTitle = convertView.findViewById(R.id.urlTitle);
        urlTitle.setText(urlTitleString);
        Button btn = convertView.findViewById(R.id.removeUrlBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainPhoneActivity.urls.remove(position);

                ArrayList<String> mainUrls = MainPhoneActivity.urls;
                for (int i = 0; i < MainPhoneActivity.urls.size(); i++) {
                    Log.d("WEARNEWS", MainPhoneActivity.urls.get(i));
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}