package com.example.mynotes.DB;

import android.content.Context;

import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mynotes.dao.noteDao;
import com.example.mynotes.entities.Notes;

@Database(entities = Notes.class, version = 1, exportSchema = false)
public abstract class notesDB extends RoomDatabase {
    private static notesDB Notesdb;
    public static synchronized notesDB getDatabase(Context context){

        if(Notesdb==null){
            Notesdb= Room.databaseBuilder(
                    context,
                    notesDB.class,
                    "notes_db"

            ).build();
        }
        return Notesdb;
    }
    public abstract noteDao dao();
}
