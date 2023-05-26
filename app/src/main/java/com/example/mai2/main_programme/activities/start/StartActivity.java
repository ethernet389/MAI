package com.example.mai2.main_programme.activities.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.check_notes.CheckMAIActivity;
import com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.ChooseMAIConfigActivity;
import com.example.mai2.main_programme.activities.create_mai_template.SetNameForMAITemplateActivity;
import com.example.mai2.main_programme.db.database.AppDatabase;
import com.example.mai2.main_programme.db.tables.mai_config.MAIConfig;
import com.manojbhadane.QButton;

public class StartActivity extends AppCompatActivity {

    QButton createMAITemplateButton;
    QButton createMAIButton;
    QButton checkMAIButton;

    private void initialize(){
        createMAITemplateButton = findViewById(R.id.create_template_button);
        createMAIButton = findViewById(R.id.start_mai_button);
        checkMAIButton = findViewById(R.id.check_mai_button);
    }

    @SuppressWarnings("rawtypes")
    class CreateIntentOnClick implements View.OnClickListener {
        Class activity;

        public CreateIntentOnClick(Class activity){
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StartActivity.this, activity);
            startActivity(intent);
        }
    }

    private void setOnClickListeners(){
        createMAIButton
                .setOnClickListener(
                        new CreateIntentOnClick(ChooseMAIConfigActivity.class)
                );

        createMAITemplateButton
                .setOnClickListener(
                        new CreateIntentOnClick(SetNameForMAITemplateActivity.class)
                );

        checkMAIButton
                .setOnClickListener(
                        new CreateIntentOnClick(CheckMAIActivity.class)
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initialize();

        setOnClickListeners();
    }
}