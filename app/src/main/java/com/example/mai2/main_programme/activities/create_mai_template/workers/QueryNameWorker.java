package com.example.mai2.main_programme.activities.create_mai_template.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mai2.main_programme.db.database.AppDatabase;

import java.util.List;

public class QueryNameWorker extends Worker{

    public QueryNameWorker(@NonNull Context context,
                       @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        List<String> namesOfConfigs = db.getMAIConfigDao().getAllNamesOfMAIConfigs();
        String inputNameOfConfig = getInputData().getString("nameOfConfig");
        boolean nameInDB = namesOfConfigs.contains(inputNameOfConfig);

        if (nameInDB){
            return Result.failure();
        }
        return Result.success();
    }
}