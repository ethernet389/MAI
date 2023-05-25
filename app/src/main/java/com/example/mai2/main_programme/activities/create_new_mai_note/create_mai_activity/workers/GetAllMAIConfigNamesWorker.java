package com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mai2.main_programme.db.database.AppDatabase;
import com.google.gson.Gson;

import java.util.List;

public class GetAllMAIConfigNamesWorker extends Worker {
    public GetAllMAIConfigNamesWorker(@NonNull Context context,
                                      @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        List<String> names = db.getMAIConfigDao().getAllNamesOfMAIConfigs();
        String packedNames = new Gson().toJson(names);
        Data output = new Data.Builder()
                .putString("names", packedNames)
                .build();
        return Result.success(output);
    }
}
