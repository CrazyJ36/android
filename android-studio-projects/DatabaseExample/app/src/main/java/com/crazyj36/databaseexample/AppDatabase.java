package com.crazyj36.databaseexample;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { Names.class }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NamesDao namesDao();
}