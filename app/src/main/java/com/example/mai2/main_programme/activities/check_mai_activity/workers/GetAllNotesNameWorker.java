package com.example.mai2.main_programme.activities.check_mai_activity.workers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQuery;

import androidx.annotation.NonNull;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mai2.main_programme.activities.check_mai_activity.wrappers.ArrayPairList;
import com.example.mai2.main_programme.db.database.AppDatabase;
import com.google.gson.Gson;

import org.javatuples.Pair;

import java.util.ArrayList;

public class GetAllNotesNameWorker extends Worker {


    public GetAllNotesNameWorker(@NonNull Context context,
                                 @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT name, configName FROM MAINote");
        Cursor cursor = db.query(query);
        ArrayList<Pair<String, String>> mappedCursor = new ArrayList<>();
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String configName = cursor.getString(cursor.getColumnIndexOrThrow("configName"));
            Pair<String, String> pair = new Pair<>(name, configName);
            mappedCursor.add(pair);
        }
        ArrayPairList wrappedCursor = new ArrayPairList(mappedCursor);
        String packedCursor = new Gson().toJson(wrappedCursor);
        Data output = new Data.Builder()
                .putString("packedCursor", packedCursor)
                .build();
        return Result.success(output);
    }
}
