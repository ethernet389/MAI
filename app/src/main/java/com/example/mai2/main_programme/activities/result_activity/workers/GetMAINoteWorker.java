package com.example.mai2.main_programme.activities.result_activity.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mai2.main_programme.db.database.AppDatabase;
import com.example.mai2.main_programme.db.tables.mai_note.MAINote;
import com.google.gson.Gson;

public class GetMAINoteWorker extends Worker {
    public GetMAINoteWorker(@NonNull Context context,
                            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String name = getInputData().getString("name");

        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        MAINote note = db.getMAINoteDao().getNoteByName(name);

        Data data = new Data.Builder()
                .putStringArray("criteria", note.criteria)
                .putStringArray("candidates", note.candidates)
                .putString("formattedAnswer", note.formattedAnswer)
                .build();

        return Result.success(data);
    }
}
