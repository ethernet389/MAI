package com.example.mai2.main_programme.db.tables.mai_config;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.example.mai2.main_programme.db.converters.StringArrayConverter;


import java.util.List;

@Dao
@TypeConverters(StringArrayConverter.class)
public interface MAIConfigDAO {
    //Получение всех имён каждой МАИ-конфигурации
    @Query("SELECT name FROM MAIConfig")
    List<String> getAllNamesOfMAIConfigs();

    //Получение МАИ-конфигурации по name
    @Query("SELECT criteria FROM MAIConfig WHERE name = :name")
    String[] getCriteriaByName(String name);

    //Добавление новой МАИ-конфигурации
    @Insert
    void insertNewMAIConfig(MAIConfig config);

    @Query("DELETE FROM MAIConfig WHERE name=:name")
    void deleteMAIConfigByName(String name);
}
