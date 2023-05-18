package com.example.mai2.main_programme.activities.create_mai_template_activity.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mai2.main_programme.db.database.AppDatabase;
import com.example.mai2.main_programme.db.tables.mai_config.MAIConfig;
import com.example.mai2.main_programme.db.wrappers.Strings;

import java.util.ArrayList;
import java.util.Arrays;

public class QueryAddMAIConfigWorker extends Worker {
    public QueryAddMAIConfigWorker(@NonNull Context context,
                                   @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());

        String nameOfConfig = getInputData().getString("nameOfConfig");
        String[] criteria = getInputData().getStringArray("criteria");

        assert criteria != null;
        ArrayList<String> arrayCriteria = new ArrayList<>(Arrays.asList(criteria));
        Strings completeCriteria = new Strings(arrayCriteria);

        MAIConfig config = new MAIConfig();
        config.criteria = completeCriteria;
        assert nameOfConfig != null;
        config.name = nameOfConfig;
        db.getMAIConfigDao().insertNewMAIConfig(config);

        return Result.success();
    }
}
