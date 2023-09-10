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

    @Query("SELECT content FROM notes WHERE id = :rowId")
    String getContentById(long rowId);

    @Query("SELECT id FROM notes WHERE content = (:content)")
    int getIdByContent(String content);

    @Insert
    long insertNote(Note note);

    @Delete
    void deleteNote(Note note);
}
