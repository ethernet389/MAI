package com.example.mai2.main_programme.activities.check_mai_activity.workers;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mai2.main_programme.db.database.AppDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class GetAllNotesNameWorker extends Worker {


    public GetAllNotesNameWorker(@NonNull Context context,
                                 @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        List<String> names = db.getMAINoteDao().getAllNameOfNotes();
        List<String> configNames = db.getMAINoteDao().getAllConfigNameOfNotes();
        String packedNames = new Gson().toJson(names);
        String packedConfigNames= new Gson().toJson(configNames);

        Data output = new Data.Builder()
                .putString("names", packedNames)
                .putString("configNames", packedConfigNames)
                .build();
        return Result.success(output);
    }
}
