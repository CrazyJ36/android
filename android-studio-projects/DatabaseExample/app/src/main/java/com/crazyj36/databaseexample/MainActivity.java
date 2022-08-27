package com.crazyj36.databaseexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    NamesDao namesDao;
    EditText editText;
    ListView listView;
    ArrayAdapter<Names> adapter;
    ArrayList<Names> myList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        listView = findViewById(R.id.listView);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "database-name").build();
        namesDao = db.namesDao();
        adapter = new MyAdapter(MainActivity.this, myList);
        listView.setAdapter(adapter);
        doInsertAll();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAdd();
                //adapter.notifyDataSetChanged();
            }
        });
    }
    public void doInsertAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                myList.clear();
                myList.addAll(namesDao.getAll());
            }
        }).start();
    }
    public void doAdd() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String item = editText.getText().toString();
                namesDao.insertNames(new Names(0, item));
                doInsertAll();
            }
        }).start();
    }
}
