package com.example.mai2.main_programme.db.tables.mai_note;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;

import com.example.mai2.main_programme.db.converters.StringsConverter;
import com.example.mai2.main_programme.db.wrappers.Strings;

@Entity
@TypeConverters(StringsConverter.class)
public class MAINote {
    @PrimaryKey
    @NonNull
    public String name;

    //many-to-one MAIConfig.name
    public String configName;

    public Strings candidates;

    public String formattedAnswer;
}
