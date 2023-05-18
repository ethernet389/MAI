package com.example.mai2.main_programme.activities.main_activity.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mai2.main_programme.db.database.AppDatabase;
import com.google.gson.Gson;

public class GetCriteriaWorker extends Worker {

    public GetCriteriaWorker(@NonNull Context context,
                             @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        String nameOfConfig = getInputData().getString("nameOfConfig");
        String[] criteria = db.getMAIConfigDao().getCriteriaByName(nameOfConfig);
        criteria = new Gson().fromJson(criteria[0], String[].class);

        Data  data = new Data
                .Builder()
                .putStringArray("criteria", criteria)
                .build();
        return Result.success(data);
    }
}
