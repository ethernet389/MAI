package com.example.mai2.main_programme.activities.main_activity.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mai2.main_programme.db.database.AppDatabase;
import com.example.mai2.main_programme.db.tables.mai_note.MAINote;

public class InsertNewMAINoteWorker extends Worker {
    public InsertNewMAINoteWorker(@NonNull Context context,
                                  @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String nameOfConfig = getInputData().getString("nameOfConfig");
        String name = getInputData().getString("name");
        String formattedAnswer = getInputData().getString("formattedAnswer");
        String[] candidates = getInputData().getStringArray("candidates");

        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        MAINote note = new MAINote();
        note.candidates = candidates;
        note.configName = nameOfConfig;
        note.name = name;
        note.formattedAnswer = formattedAnswer;
        db.getMAINoteDao().insertNewMAINote(note);

        return Result.success();
    }
}
