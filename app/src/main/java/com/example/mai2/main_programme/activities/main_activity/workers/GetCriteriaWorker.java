package com.example.mai2.main_programme.activities.main_activity.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mai2.main_programme.db.database.AppDatabase;

public class GetCriteriaWorker extends Worker {

    public GetCriteriaWorker(@NonNull Context context,
                             @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        getInputData();

        return Result.success();
    }
}
