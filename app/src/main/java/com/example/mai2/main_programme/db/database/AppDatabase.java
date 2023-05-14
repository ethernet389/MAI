package com.example.mai2.main_programme.db.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.db.mai_config.MAIConfig;
import com.example.mai2.main_programme.db.mai_config.MAIConfigDAO;

@Database(entities = {MAIConfig.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MAIConfigDAO getMAIConfigDao();

    //Singleton
    private static AppDatabase INSTANCE = null;
    public static AppDatabase getAppDatabase(Context context){
        AppDatabase tempInstance = INSTANCE;
        if (tempInstance != null){
            return tempInstance;
        }
        AppDatabase instance = Room
                .databaseBuilder(
                        context,
                        AppDatabase.class,
                        Constants.DB_NAME
                ).build();
        INSTANCE = instance;
        return instance;
    }
}
