package com.crazyj36.wearnews;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        convertView.findViewById(R.id.removeUrlBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("WEARNEWS", "Removing URL: " + getItem(position));
                MainPhoneActivity.set.remove(getItem(position));
                MainPhoneActivity.urls.remove(getItem(position));
                PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext()).edit().putStringSet("urls", MainPhoneActivity.set).apply();
                Log.d("WEARNEWS", "URLS Size: " + MainPhoneActivity.set.size());
                for (String str : MainPhoneActivity.set) {
                    Log.d("WEARNEWS", "set contains: " + str);
                }
                notifyDataSetChanged();;
            }
        });


        return convertView;
    }
}