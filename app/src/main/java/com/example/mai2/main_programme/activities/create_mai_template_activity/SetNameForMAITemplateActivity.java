package com.example.mai2.main_programme.activities.create_mai_template_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.create_mai_template_activity.workers.QueryNameWorker;
import com.example.mai2.main_programme.db.database.AppDatabase;

import java.util.List;

public class SetNameForMAITemplateActivity extends AppCompatActivity {

    EditText inputMAIName;
    Button nextButton;

    public static final String NAME_OF_TEMPLATE = "nameOfTemplate";

    class NextOnClickListener implements View.OnClickListener{

        private void showShortToastWithText(String text){
            Toast.makeText(
                    getApplicationContext(),
                    text,
                    Toast.LENGTH_SHORT
            ).show();
        }

        @Override
        public void onClick(View v) {
            String nameOfConfig = inputMAIName.getText().toString();
            if (nameOfConfig.isEmpty()) {
                showShortToastWithText("Название не введено!");
                return;
            };

            Data inputData = new Data.Builder()
                    .putString("nameOfConfig", nameOfConfig)
                    .build();
            OneTimeWorkRequest request
                    = new OneTimeWorkRequest.Builder(QueryNameWorker.class)
                    .setInputData(inputData)
                    .build();
            WorkManager.getInstance(getApplicationContext()).enqueue(request);

            Observer<WorkInfo> observer = new Observer<WorkInfo>() {
                @Override
                public void onChanged(WorkInfo workInfo) {
                    switch (workInfo.getState()){
                        case FAILED:
                            showShortToastWithText("Шаблон с таким названием уже есть!");
                            break;
                        case SUCCEEDED:
                            Intent intent = new Intent(
                                    getApplicationContext(),
                                    CreateMAITemplateActivity.class
                            );
                            intent.putExtra(NAME_OF_TEMPLATE, nameOfConfig);
                            startActivity(intent);
                            finish();
                            break;
                    }
                }
            };
            WorkManager.getInstance(getApplicationContext())
                    .getWorkInfoByIdLiveData(request.getId()).observe(
                            SetNameForMAITemplateActivity.this, observer
                    );
        }
    }

    private void initialize(){
        inputMAIName = findViewById(R.id.input_mai_template_name);
        nextButton = findViewById(R.id.go_next_with_name_button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name_for_mai_template);
        initialize();

        nextButton.setOnClickListener(new NextOnClickListener());
    }
}