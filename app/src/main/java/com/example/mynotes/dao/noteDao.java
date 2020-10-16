package com.example.mynotes.dao;

import android.se.omapi.SEService;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mynotes.entities.Notes;

import java.util.List;

@Dao
public interface noteDao {
    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Notes> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Notes note);

    @Delete
    void deleteNote(Notes note);

}
