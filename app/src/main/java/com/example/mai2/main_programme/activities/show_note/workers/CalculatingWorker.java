package com.example.mai2.main_programme.activities.show_note.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mai2.main_programme.algorithm.math.Buffer;
import com.example.mai2.main_programme.algorithm.math.CalculatingClass;

import java.util.Locale;
import java.util.Scanner;

public class CalculatingWorker extends Worker {
    public CalculatingWorker(@NonNull Context context,
                             @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String formattedAnswer = getInputData().getString("formattedAnswer");
        Scanner data = new Scanner(formattedAnswer);
        data.useLocale(Locale.CANADA);
        Buffer buffer = CalculatingClass.calculate(data);
        data.close();

        Data outputData = new Data.Builder()
                .putString("data", Buffer.toJson(buffer))
                .build();

        return Result.success(outputData);
    }
}
