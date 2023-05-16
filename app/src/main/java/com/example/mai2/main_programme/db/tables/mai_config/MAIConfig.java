package com.example.mai2.main_programme.db.tables.mai_config;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mai2.main_programme.db.converters.StringsConverter;
import com.example.mai2.main_programme.db.wrappers.Strings;


@Entity
@TypeConverters(StringsConverter.class)
public class MAIConfig {
    @PrimaryKey
    @NonNull
    public String name;

    @NonNull
    public Strings criteria;
}