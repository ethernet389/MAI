package com.example.mai2.main_programme.db.converters;

import androidx.room.TypeConverter;

import com.example.mai2.main_programme.db.wrappers.Strings;
import com.google.gson.Gson;

public class StringsConverter {
    @TypeConverter
    public Strings storedStringToStrings(String value){
        return new Gson().fromJson(value, Strings.class);
    }

    @TypeConverter
    public String stringsToStoredString(Strings value){
        return new Gson().toJson(value.getStringList());
    }
}
