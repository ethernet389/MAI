package com.example.mai2.main_programme.db.tables.mai_note;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mai2.main_programme.db.converters.StringArrayConverter;


@Entity
@TypeConverters(StringArrayConverter.class)
public class MAINote {
    @PrimaryKey
    @NonNull
    public String name;

    //many-to-one MAIConfig.name
    @NonNull
    public String configName;

    @NonNull
    public String[] criteria;

    @NonNull
    public String[] candidates;

    @NonNull
    public String formattedAnswer;
}
