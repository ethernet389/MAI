package com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.recyclers.ChooseTemplateRecyclerAdapter;
import com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.workers.GetAllMAIConfigNamesWorker;
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