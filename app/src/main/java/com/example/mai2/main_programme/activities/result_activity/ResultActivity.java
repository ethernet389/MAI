package com.example.mai2.main_programme.activities.result_activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;


import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;

import android.widget.Button;


import com.example.mai2.R;
import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.activities.result_activity.recyclers.RecyclerResultAdapter;
import com.example.mai2.main_programme.activities.result_activity.workers.CalculatingWorker;
import com.example.mai2.main_programme.activities.result_activity.workers.GetMAINoteWorker;
import com.example.mai2.main_programme.activities.start_activity.StartActivity;
import com.example.mai2.main_programme.math.Buffer;

public class ResultActivity extends AppCompatActivity {
    RecyclerView resultRecycler;
    Button endViewButton;
    LayoutInflater layoutInflater;

    String name;
    String formattedAnswer;
    String[] criteria;
    String[] candidates;

    private void initialize(){
        resultRecycler = findViewById(R.id.result_pager);
        endViewButton = findViewById(R.id.end_button);
        layoutInflater = getLayoutInflater();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initialize();

        endViewButton.setOnClickListener(endViewing -> {
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
            finish();
        });

        name = getIntent().getStringExtra(Constants.NOTE_KEY);
        Data dataNote = new Data.Builder()
                .putString("name", name)
                .build();
        OneTimeWorkRequest getNoteRequest = new OneTimeWorkRequest.Builder(GetMAINoteWorker.class)
                .setInputData(dataNote)
                .build();

        Data dataToCalculate = new Data.Builder()
                .putString("formattedAnswer", formattedAnswer)
                .build();
        OneTimeWorkRequest calculateRequest = new OneTimeWorkRequest.Builder(CalculatingWorker.class)
                .setInputData(dataToCalculate)
                        .build();

        WorkManager.getInstance(getApplicationContext())
                .beginWith(getNoteRequest)
                .then(calculateRequest)
                .enqueue();

        Observer<WorkInfo> getNoteObserver = workInfo -> {
            if (!workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) return;
            criteria = workInfo.getOutputData().getStringArray("criteria");
            candidates = workInfo.getOutputData().getStringArray("candidates");
            formattedAnswer = workInfo.getOutputData().getString("formattedAnswer");
        };

        Observer<WorkInfo> calculateObserver = workInfo -> {
            if (!workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) return;
            String json = workInfo.getOutputData().getString("data");
            Buffer buffer = Buffer.fromJson(json);

            RecyclerResultAdapter adapter = new RecyclerResultAdapter(
                    getApplicationContext(), buffer, criteria, candidates
            );

            PagerSnapHelper psh = new PagerSnapHelper();
            psh.attachToRecyclerView(resultRecycler);

            resultRecycler.setLayoutManager(
                    new LinearLayoutManager(
                            getApplicationContext(), LinearLayoutManager.HORIZONTAL, false
                    )
            );

            resultRecycler.setAdapter(adapter);
        };

        WorkManager.getInstance(getApplicationContext())
                .getWorkInfoByIdLiveData(getNoteRequest.getId())
                .observe(this, getNoteObserver);

        WorkManager.getInstance(getApplicationContext())
                .getWorkInfoByIdLiveData(calculateRequest.getId())
                .observe(this, calculateObserver);
    }
}