package com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.mai2.main_programme.activities.create_mai_template.CreateMAITemplateActivity;


public class CreateMAIActivity extends AppCompatActivity {

    Button startAnsweringButton;
    Button addCandidateButton;
    LinearLayout candidateContainer;

    LayoutInflater inflater;

    public final static String CANDIDATES_KEY = "candidatesKey";

    private void initialize(){
        startAnsweringButton = findViewById(R.id.start_answering);
        addCandidateButton = findViewById(R.id.add_candidate_button);
        candidateContainer = findViewById(R.id.candidate_container);
        inflater = getLayoutInflater();
    }

    class DeleteOnClickListener implements View.OnClickListener{

        private final View parentView;

        public DeleteOnClickListener(View parentView){
            this.parentView = parentView;
        }

        @Override
        public void onClick(View v) {
            if (candidateContainer.getChildCount() == 1) return;
            candidateContainer.removeView(parentView);
        }
    }

    class AddCandidateOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            TableRow newCandidateRow =
                    (TableRow) inflater.inflate(R.layout.candidate_table_row, null);
            newCandidateRow
                    .findViewById(R.id.delete_x)
                    .setOnClickListener(new DeleteOnClickListener(newCandidateRow));
            candidateContainer.addView(newCandidateRow);
        }
    }

    class StartAnsweringOnClickListener implements View.OnClickListener{
        private String[] readCandidates(){
            String[] rawData = new String[candidateContainer.getChildCount()];
            for (int i = 0; i < candidateContainer.getChildCount(); ++i){
                TableRow criteriaRow = (TableRow) candidateContainer.getChildAt(i);
                EditText criteriaText = criteriaRow.findViewById(R.id.candidate_text);
                String criteria = criteriaText.getText().toString();
                if (criteria.isEmpty()){
                    Toast.makeText(
                            getApplicationContext(),
                            R.string.missing_field_name_message,
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
            String[] candidates = readCandidates();
            if (candidates == null) return;

            Intent intent = new Intent(getApplicationContext(),
                    GetNameMAINoteActivity.class);
            String name = getIntent().getStringExtra(ChooseMAIConfigActivity.NAME_OF_CONFIG_KEY);
            intent.putExtra(ChooseMAIConfigActivity.NAME_OF_CONFIG_KEY, name);
            intent.putExtra(CANDIDATES_KEY, candidates);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mai_activity);
        initialize();
        TableRow firstRow = findViewById(R.id.one_candidate);
        ImageView firstDelete = firstRow.findViewById(R.id.delete_x);
        firstDelete.setOnClickListener(new DeleteOnClickListener(firstRow));

        addCandidateButton.setOnClickListener(new AddCandidateOnClickListener());
        startAnsweringButton.setOnClickListener(new StartAnsweringOnClickListener());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String[] criteriaArray = new String[candidateContainer.getChildCount()];
        for (int i = 0; i < candidateContainer.getChildCount(); ++i){
            ViewGroup element = (ViewGroup) candidateContainer.getChildAt(i);
            EditText criteria = element.findViewById(R.id.candidate_text);
            String criteriaText = criteria.getText().toString();
            criteriaArray[i] = criteriaText;
        }
        outState.putStringArray("candidates", criteriaArray);
    }

    private void setTextAndListeners(TableRow restoredTableRow, String text){
        EditText candidate = restoredTableRow.findViewById(R.id.candidate_text);
        candidate.setText(text);
        ImageView button = restoredTableRow.findViewById(R.id.delete_x);
        button.setOnClickListener(new DeleteOnClickListener(restoredTableRow));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String[] criteriaArray = savedInstanceState.getStringArray("candidates");
        TableRow firstRow = (TableRow) candidateContainer.getChildAt(0);
        setTextAndListeners(firstRow, criteriaArray[0]);
        for (int i = 1; i < criteriaArray.length; ++i){
            @SuppressLint("InflateParams")
            TableRow restoredTableRow
                    = (TableRow) inflater.inflate(R.layout.candidate_table_row, null);
            setTextAndListeners(restoredTableRow, criteriaArray[i]);
            candidateContainer.addView(restoredTableRow);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ChooseMAIConfigActivity.class);
        startActivity(intent);
        finish();
    }
}