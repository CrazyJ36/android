package com.crazyj36.databaseexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    AppDatabase db; // to be closed later in onDestory()
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
        adapter = new MyAdapter(MainActivity.this, myList);
        listView.setAdapter(adapter);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "database-name").build();
        namesDao = db.namesDao();

        // get all currently in database to populate listview
        doInsertAll();

        // add new name to database
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAdd();
            }
        });

        // delete from database
        //doDelete(4);

        // get database entry name from rowId
        doGetNameById(6);
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
    public void doDelete(int idToDelete) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                namesDao.deleteNames(new Names(idToDelete, null));
            }
        }).start();
    }
    public void doAdd() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String item = editText.getText().toString();
                long id;
                if (!item.equals("")) {
                    id = namesDao.insertNames(new Names(0, item)); // id 0 doesn't is redundant here as Names.id auto-generates, but it's needed for doDelete(int id).
                    // receive reference of newly added item for further processing like adding to myList index.
                    toast("New item id: " + String.valueOf(id));
                    myList.clear();
                    myList.addAll(namesDao.getAll());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }
    public void doGetNameById(long id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                 if (namesDao.getNameById(id) != null) {
                     toast("name set in " + id + ": " + namesDao.getNameById(id));
                 }
            }
        }).start();
    }
    public void toast(String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onDestroy() {
        super.onDestroy();
        db.close(); // Close the AppDatabase to avoid memory leaks;
    }
}
