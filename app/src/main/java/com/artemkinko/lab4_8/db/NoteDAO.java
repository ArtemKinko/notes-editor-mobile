package com.artemkinko.lab4_8.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDAO {
    @Query("SELECT * FROM note")
    List<Note> getAll();

    @Query("DELETE FROM note")
    Void deleteAll();

    @Insert
    void insertAll(Note... notes);

    @Query("DELETE FROM note WHERE title = :title and text = :text")
    Void deleteByTitleAndText(String title, String text);
}
