package com.example.mai2.main_programme.activities.check_notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.check_notes.recyclers.MAINoteRecyclerAdapter;
import com.example.mai2.main_programme.activities.check_notes.workers.GetAllNotesNameWorker;
import com.example.mai2.main_programme.activities.check_notes.wrappers.ArrayPairList;
import com.example.mai2.main_programme.activities.start.StartActivity;
import com.google.gson.Gson;

public class CheckMAIActivity extends AppCompatActivity {

    TextView title;
    RecyclerView container;

    private void initialize(){
        container = findViewById(R.id.mai_records_container);
        title = findViewById(R.id.check_mai_title);
        GradientDrawable back = (GradientDrawable) title.getBackground().mutate();
        back.setColor(getColor(R.color.design_blue));
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}