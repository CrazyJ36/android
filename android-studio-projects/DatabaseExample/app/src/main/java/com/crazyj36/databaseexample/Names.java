package com.crazyj36.databaseexample;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Names {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo
    public String name;

    public Names(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
