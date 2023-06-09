package com.example.mai2.main_programme.activities.create_mai_template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.create_mai_template.workers.QueryNameWorker;
import com.example.mai2.main_programme.activities.start.StartActivity;

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
        inputMAIName = findViewById(R.id.input_name_text);
        nextButton = findViewById(R.id.go_next_with_name_button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_set_name);
        initialize();

        nextButton.setOnClickListener(new NextOnClickListener());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String templateName = inputMAIName.getText().toString();
        outState.putString("name", templateName);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String templateName = savedInstanceState.getString("name");
        inputMAIName.setText(templateName);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}