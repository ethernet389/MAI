package com.example.mai2.main_programme.activities.check_mai_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.javatuples.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mai2.R;
import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.activities.check_mai_activity.recyclers.MAINoteRecyclerAdapter;
import com.example.mai2.main_programme.activities.check_mai_activity.workers.GetAllNotesNameWorker;
import com.example.mai2.main_programme.activities.check_mai_activity.wrappers.ArrayPairList;
import com.example.mai2.main_programme.activities.result_activity.ResultActivity;
import com.example.mai2.main_programme.db.database.AppDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckMAIActivity extends AppCompatActivity {

    RecyclerView container;

    private void initialize(){
        container = findViewById(R.id.mai_records_container);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mai_activity);
        initialize();

        OneTimeWorkRequest request = new OneTimeWorkRequest
                .Builder(GetAllNotesNameWorker.class)
                .build();
        WorkManager.getInstance(this).enqueue(request);
        Observer<WorkInfo> observer = workInfo -> {
            if (!workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) return;
            String packedCursor = workInfo.getOutputData().getString("packedCursor");
            ArrayPairList cursorList = new Gson().fromJson(packedCursor, ArrayPairList.class);
            MAINoteRecyclerAdapter adapter =
                    new MAINoteRecyclerAdapter(this, cursorList.list);
            container.setLayoutManager(new LinearLayoutManager(this));
            container.setAdapter(adapter);
        };
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.getId())
                .observe(this, observer);
    }
}