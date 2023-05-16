package com.example.mai2.main_programme.activities.create_mai_template_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.mai2.R;
import com.example.mai2.main_programme.db.database.AppDatabase;
import com.example.mai2.main_programme.db.tables.mai_config.MAIConfig;
import com.example.mai2.main_programme.db.wrappers.Strings;

import java.util.ArrayList;

public class CreateMAITemplateActivity extends AppCompatActivity {

    Button saveTemplateButton;
    LinearLayout criteriaContainer;
    Button addCriteriaButton;

    private String nameOfConfig;
    AppDatabase db;

    LayoutInflater layoutInflater;

    private void initialize(){
        saveTemplateButton = findViewById(R.id.save_template_button);
        addCriteriaButton = findViewById(R.id.add_criteria_button);
        criteriaContainer = findViewById(R.id.criteria_container);

        layoutInflater = getLayoutInflater();

        nameOfConfig = getIntent().getStringExtra(SetNameForMAITemplateActivity.NAME_OF_TEMPLATE);

        db = AppDatabase.getAppDatabase(getApplicationContext());
    }



    class SaveTemplateOnClickListener implements View.OnClickListener{
        private ArrayList<String> readCriteria(){
            ArrayList<String> rawData = new ArrayList<>();
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
                    return new ArrayList<>();
                }
                rawData.add(criteria);
            }
            return rawData;
        }

        @Override
        public void onClick(View v) {
            Strings wrappedCriteria = new Strings(readCriteria());
            if (wrappedCriteria.getStringList().isEmpty()){
                return;
            }

            MAIConfig config = new MAIConfig();
            config.name = nameOfConfig;
            config.criteria = wrappedCriteria;
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
}