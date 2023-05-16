package com.example.mai2.main_programme.db.tables.mai_note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MAINoteDao {

    @Insert
    void insertNewMAINote(MAINote note);

    @Query("SELECT * FROM MAINote")
    List<MAINote> getAllNotes();

    @Delete
    void deleteMAINote(MAINote note);

}
