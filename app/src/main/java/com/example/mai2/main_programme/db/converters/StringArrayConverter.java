package com.example.mai2.main_programme.db.converters;

import androidx.room.TypeConverter;


import com.google.gson.Gson;

public class StringArrayConverter {
    @TypeConverter
    public String[] storedStringToStrings(String value){
        return new Gson().fromJson(value, String[].class);
    }

    @TypeConverter
    public String stringsToStoredString(String[] value){
        return new Gson().toJson(value);
    }
}
