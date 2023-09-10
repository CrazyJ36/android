package com.crazyj36.databaseexample;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    long id;

    @ColumnInfo
    public String content;

    @ColumnInfo
    public String date;

    public Note(long id, String content, String date) {
        this.id = id;
        this.content = content;
        this.date = date;
    }
}
