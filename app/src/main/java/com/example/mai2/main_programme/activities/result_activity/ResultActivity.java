package com.example.mai2.main_programme.activities.result_activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;


import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.mai2.R;
import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.activities.main_activity.MainActivity;
import com.example.mai2.main_programme.activities.result_activity.calculating_thread.CalculatingThread;
import com.example.mai2.main_programme.activities.result_activity.generate_layout_thread.LayoutGeneratorThread;
import com.example.mai2.main_programme.activities.result_activity.workers.GetMAINoteWorker;
import com.example.mai2.main_programme.activities.start_activity.StartActivity;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ResultActivity extends AppCompatActivity {
    LinearLayout resultContainer;
    Button endViewButton;
    LayoutInflater layoutInflater;

    String name;
    String formattedAnswer;
    String[] criteria;
    String[] candidates;

    private void initialize(){
        resultContainer = findViewById(R.id.result_container);
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
        Data data = new Data.Builder()
                .putString("name", name)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(GetMAINoteWorker.class)
                .setInputData(data)
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(request);

        Observer<WorkInfo> observer = new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) {
                    criteria = workInfo.getOutputData().getStringArray("criteria");
                    candidates = workInfo.getOutputData().getStringArray("candidates");
                    formattedAnswer = workInfo.getOutputData().getString("formattedAnswer");

                    final int operationsCount
                            = (criteria.length + 1) * candidates.length;

                    final CountDownLatch LAYOUT_GENERATED_FLAG
                            = new CountDownLatch(operationsCount);

                    ArrayList<TextView> valueArray = new ArrayList<>();
                    LayoutGeneratorThread lgt =
                            new LayoutGeneratorThread(layoutInflater,
                                    resultContainer,
                                    valueArray,
                                    criteria,
                                    candidates,
                                    LAYOUT_GENERATED_FLAG);
                    lgt.start();

                    CalculatingThread ct = new CalculatingThread(formattedAnswer,
                            valueArray,
                            criteria,
                            candidates,
                            LAYOUT_GENERATED_FLAG);
                    ct.start();
                }
            }
        };

        WorkManager.getInstance(getApplicationContext())
                .getWorkInfoByIdLiveData(request.getId())
                .observe(this, observer);
    }
}