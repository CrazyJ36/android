package com.crazyj36.databaseexample;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface NamesDao {
    @Query("SELECT * FROM names")
    List<Names> getAll();

    @Query("SELECT name from names WHERE id = :rowId")
    String getNameById(long rowId);

    @Insert
    long insertNames(Names names);

    @Delete
    void deleteNames(Names names);
}
