package com.example.mai2.main_programme.db.tables.mai_config;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mai2.main_programme.db.converters.StringArrayConverter;


@Entity
@TypeConverters(StringArrayConverter.class)
public class MAIConfig {
    @PrimaryKey
    @NonNull
    public String name;

    @NonNull
    public String[] criteria;
}