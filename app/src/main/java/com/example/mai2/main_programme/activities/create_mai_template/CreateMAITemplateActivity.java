package com.example.mai2.main_programme.activities.create_mai_template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.create_mai_template.workers.QueryAddMAIConfigWorker;
import com.example.mai2.main_programme.activities.start.StartActivity;

public class CreateMAITemplateActivity extends AppCompatActivity {

    Button saveTemplateButton;
    LinearLayout criteriaContainer;
    Button addCriteriaButton;

    private String nameOfConfig;

    LayoutInflater layoutInflater;

    private void initialize(){
        saveTemplateButton = findViewById(R.id.save_template_button);
        addCriteriaButton = findViewById(R.id.add_criteria_button);
        criteriaContainer = findViewById(R.id.criteria_container);

        layoutInflater = getLayoutInflater();

        nameOfConfig = getIntent().getStringExtra(SetNameForMAITemplateActivity.NAME_OF_TEMPLATE);
    }

    class SaveTemplateOnClickListener implements View.OnClickListener{
        private String[] readCriteria(){
            String[] rawData = new String[criteriaContainer.getChildCount()];
            for (int i = 0; i < criteriaContainer.getChildCount(); ++i){
                TableRow criteriaRow = (TableRow) criteriaContainer.getChildAt(i);
                EditText criteriaText = criteriaRow.findViewById(R.id.criteria_text);
                String criteria = criteriaText.getText().toString();
                if (criteria.isEmpty()){
                    Toast.makeText(
                            getApplicationContext(),
                            "Есть пустой критерий!",
                            Toast.LENGTH_SHORT
                    ).show();
                    return null;
                }
                rawData[i] = criteria;
            }
            return rawData;
        }

        @Override
        public void onClick(View v) {
            String[] criteria = readCriteria();
            if (criteria == null){
                return;
            }

            Data inputData = new Data.Builder()
                    .putString("nameOfConfig", nameOfConfig)
                    .putStringArray("criteria", criteria)
                    .build();

            OneTimeWorkRequest request =
                    new OneTimeWorkRequest.Builder(QueryAddMAIConfigWorker.class)
                            .setInputData(inputData)
                            .build();
            WorkManager.getInstance(getApplicationContext()).enqueue(request);

            Observer<WorkInfo> observer = new Observer<WorkInfo>() {
                @Override
                public void onChanged(WorkInfo workInfo) {
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        Intent intent = new Intent(
                                getApplicationContext(),
                                StartActivity.class
                        );
                        startActivity(intent);
                        finish();
                    }
                }
            };
            WorkManager.getInstance(getApplicationContext())
                    .getWorkInfoByIdLiveData(request.getId()).observe(
                            CreateMAITemplateActivity.this, observer
                    );
        }
    }

    class DeleteOnClickListener implements View.OnClickListener {
        private final TableRow parentView;

        public DeleteOnClickListener(TableRow parentView){
            this.parentView = parentView;
        }

        @Override
        public void onClick(View v) {
            if (criteriaContainer.getChildCount() == 1) return;
            criteriaContainer.removeView(parentView);
        }
    }

    class AddOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            @SuppressLint("InflateParams")
            TableRow newTableRow
                    = (TableRow) layoutInflater.inflate(R.layout.criteria_table_row, null);
            ImageView button = newTableRow.findViewById(R.id.delete_x);
            button.setOnClickListener(new DeleteOnClickListener(newTableRow));
            criteriaContainer.addView(newTableRow);
        }
    }

    private void setClickListenerOnFirstImageButton(){
        TableRow firstRow = (TableRow) criteriaContainer.getChildAt(0);
        ImageView firstDeleteButton = firstRow.findViewById(R.id.delete_x);
        firstDeleteButton.setOnClickListener(new DeleteOnClickListener(firstRow));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mai_template);
        initialize();
        setClickListenerOnFirstImageButton();
        addCriteriaButton.setOnClickListener(new AddOnClickListener());
        saveTemplateButton.setOnClickListener(new SaveTemplateOnClickListener());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String[] criteriaArray = new String[criteriaContainer.getChildCount()];
        for (int i = 0; i < criteriaContainer.getChildCount(); ++i){
            ViewGroup element = (ViewGroup) criteriaContainer.getChildAt(i);
            EditText criteria = element.findViewById(R.id.criteria_text);
            String criteriaText = criteria.getText().toString();
            criteriaArray[i] = criteriaText;
        }
        outState.putStringArray("criteriaArray", criteriaArray);
    }

    private void setTextAndListeners(TableRow restoredTableRow, String text){
        EditText criteria = restoredTableRow.findViewById(R.id.criteria_text);
        criteria.setText(text);
        ImageView button = restoredTableRow.findViewById(R.id.delete_x);
        button.setOnClickListener(new DeleteOnClickListener(restoredTableRow));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String[] criteriaArray = savedInstanceState.getStringArray("criteriaArray");
        TableRow firstRow = (TableRow) criteriaContainer.getChildAt(0);
        setTextAndListeners(firstRow, criteriaArray[0]);
        for (int i = 1; i < criteriaArray.length; ++i){
            @SuppressLint("InflateParams")
            TableRow restoredTableRow
                    = (TableRow) layoutInflater.inflate(R.layout.criteria_table_row, null);
            setTextAndListeners(restoredTableRow, criteriaArray[i]);
            criteriaContainer.addView(restoredTableRow);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SetNameForMAITemplateActivity.class);
        startActivity(intent);
        finish();
    }
}