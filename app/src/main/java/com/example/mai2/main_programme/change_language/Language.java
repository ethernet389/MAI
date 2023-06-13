package com.example.mai2.main_programme.change_language;

import android.content.Context;
import android.content.SharedPreferences;

import com.yariksoffice.lingver.Lingver;

public class Language {
    static public void setLanguage(Context context){
        SharedPreferences prefs =
                context.getSharedPreferences("language", Context.MODE_PRIVATE);
        String language = prefs.getString("language", "ru");
        Lingver.getInstance().setLocale(context, language);
    }
}
