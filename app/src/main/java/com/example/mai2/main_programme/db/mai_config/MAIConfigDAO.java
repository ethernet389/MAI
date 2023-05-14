package com.example.mai2.main_programme.db.mai_config;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MAIConfigDAO {
    //Получение всех имён каждой МАИ-конфигурации
    @Query("SELECT name FROM MAIConfig")
    List<String> getAllNamesOfMAIConfigs();

    //Добавление новой МАИ-конфигурации
    @Insert
    void insertNewMAIConfig(MAIConfig config);

    //Удаление МАИ-конфигурации
    @Delete
    void deleteMAIConfig(MAIConfig config);
}
