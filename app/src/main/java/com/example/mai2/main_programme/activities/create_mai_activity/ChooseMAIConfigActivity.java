package com.example.mai2.main_programme.activities.create_mai_activity;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.create_mai_activity.recyclers.ChooseTemplateRecyclerAdapter;
import com.example.mai2.main_programme.activities.create_mai_activity.workers.GetAllMAIConfigNamesWorker;
import com.example.mai2.main_programme.db.database.AppDatabase;
import com.google.gson.Gson;

import java.util.List;

public class ChooseMAIConfigActivity extends AppCompatActivity {

    RecyclerView container;

    public final static String NAME_OF_CONFIG_KEY = "nameOfConfigKey";

    private void initialize(){
        container = findViewById(R.id.choose_template_recycler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mai_config);
        initialize();
        OneTimeWorkRequest request = new OneTimeWorkRequest
                .Builder(GetAllMAIConfigNamesWorker.class)
                .build();
        WorkManager.getInstance(this).enqueue(request);
        Observer<WorkInfo> observer = workInfo -> {
            if (!workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) return;
            String packedNames = workInfo.getOutputData().getString("names");
            List<String> names = new Gson().fromJson(packedNames, List.class);

            ChooseTemplateRecyclerAdapter adapter =
                    new ChooseTemplateRecyclerAdapter(this, names);
            container.setLayoutManager(new LinearLayoutManager(this));
            container.setAdapter(adapter);
        };

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.getId())
                .observe(this, observer);
    }
}