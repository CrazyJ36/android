package com.crazyj36.databaseexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Names> {
    public MyAdapter(Context context, ArrayList<Names> names) {
        super(context, 0, names);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView listItemId = convertView.findViewById(R.id.listItemId);
        TextView listItemName = convertView.findViewById(R.id.listItemName);
        Names names = getItem(position);
        listItemId.setText(String.valueOf(names.id));
        listItemName.setText(names.name);
        return convertView;
    }
}