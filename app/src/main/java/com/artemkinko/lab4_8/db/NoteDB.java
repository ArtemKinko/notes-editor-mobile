package com.artemkinko.lab4_8.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDB extends RoomDatabase {

    private static NoteDB noteDB;

    public static NoteDB getInstance(Context context) {
        if (noteDB == null) {
            noteDB = Room.databaseBuilder(context.getApplicationContext(), NoteDB.class, "noteDB").build();
        }
        return noteDB;
    }

    private NoteDAO noteDAO;

    public abstract NoteDAO getNoteDAO();
}
