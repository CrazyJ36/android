package com.crazyj36.databaseexample;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { Note.class }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}