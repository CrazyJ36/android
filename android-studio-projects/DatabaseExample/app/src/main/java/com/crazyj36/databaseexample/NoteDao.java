package com.crazyj36.databaseexample;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes")
    List<Note> getAll();

    @Query("SELECT content from notes WHERE id = :rowId")
    String getNoteById(long rowId);

    @Query("SELECT id from notes")
    int getId();

    @Insert
    long insertNote(Note note);

    @Delete
    void deleteNote(Note note);
}
