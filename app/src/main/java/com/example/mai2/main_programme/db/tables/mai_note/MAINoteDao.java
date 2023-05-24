package com.example.mai2.main_programme.db.tables.mai_note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.example.mai2.main_programme.db.converters.StringArrayConverter;

import java.util.HashMap;
import java.util.List;

@Dao
@TypeConverters(StringArrayConverter.class)
public interface MAINoteDao {

    @Insert
    void insertNewMAINote(MAINote note);

    @Query("SELECT name FROM MAINote")
    List<String> getAllNameOfNotes();

    @Query("SELECT configName FROM MAINote")
    List<String> getAllConfigNameOfNotes();

    @Query("SELECT * FROM MAINote WHERE name = :name")
    MAINote getNoteByName(String name);

    @Query("DELETE FROM MAINote WHERE name = :name")
    void deleteMAINoteByName(String name);

    @Delete
    void deleteMAINote(MAINote note);

}
