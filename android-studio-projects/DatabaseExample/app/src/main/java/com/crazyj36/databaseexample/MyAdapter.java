package com.crazyj36.databaseexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Note> {
    public MyAdapter(Context context, ArrayList<Note> note) {
        super(context, 0, note);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView listItemId = convertView.findViewById(R.id.listItemId);
        TextView listItemContent = convertView.findViewById(R.id.listItemContent);
        TextView listItemDate = convertView.findViewById(R.id.listItemDate);
        Note note = getItem(position);
        listItemId.setText(String.valueOf(note.id));
        listItemContent.setText(note.content);
        listItemDate.setText(note.date);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle(getContext().getString(R.string.dialogTitleText));
                dialog.setNegativeButton(getContext().getString(R.string.dialogNegativeButtonText), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.setPositiveButton(getContext().getString(R.string.dialogPositiveButtonText), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.doDeleteNote(getContext(), note);
                            }
                        }).start();
                    }
                });
                dialog.show();
            }
        });
        return convertView;
    }
}