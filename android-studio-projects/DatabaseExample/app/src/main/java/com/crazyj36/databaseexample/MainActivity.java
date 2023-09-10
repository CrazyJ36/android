package com.crazyj36.databaseexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    AppDatabase db; // to be closed later in onDestory()
    static NoteDao noteDao;
    EditText editText;
    ListView listView;
    static ArrayAdapter<Note> adapter;
    static ArrayList<Note> myList = new ArrayList<>();
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
        noteDao = db.noteDao();

        // get all currently in database to populate listview
        doInsertAll();

        // get content of specific note.
        doGetContentById(34);

        // get in by content
        doGetIdByContent("d");

        // add new name to database
        findViewById(R.id.addNoteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAdd();
            }
        });
    }
    public void doInsertAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                myList.clear();
                myList.addAll(noteDao.getAll());
            }
        }).start();
    }
    public void doAdd() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String newNote = editText.getText().toString();
                long id;
                if (!newNote.equals("")) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy h:mm a", Locale.US);
                    id = noteDao.insertNote(new Note(0, newNote, simpleDateFormat.format(new Timestamp(System.currentTimeMillis())))); // id 0 doesn't is redundant here as Names.id auto-generates, but it's needed for doDelete(int id).
                    // receive reference of newly added item for further processing like adding to myList index.
                    myList.clear();
                    myList.addAll(noteDao.getAll());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast("New item id: " + id);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }
    public static void doDeleteNote(Context context, Note note) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                noteDao.deleteNote(note);
                myList.clear();
                myList.addAll(noteDao.getAll());
            }
        }).start();
        context.getMainExecutor().execute(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void doGetContentById(int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                 if (noteDao.getContentById(id) != null) {
                     toast("content set in " + id + ": " + noteDao.getContentById(id));
                 }
            }
        }).start();
    }
    public void doGetIdByContent(String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // in first occurrence of content.
                toast("ID of " + content + ": " + noteDao.getIdByContent(content));
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
