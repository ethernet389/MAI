package com.example.mai2.main_programme.activities.start_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.check_mai_activity.CheckMAIActivity;
import com.example.mai2.main_programme.activities.create_mai_activity.CreateMAIActivity;
import com.example.mai2.main_programme.activities.create_mai_template_activity.CreateMAITemplateActivity;
import com.example.mai2.main_programme.db.database.AppDatabase;

public class StartActivity extends AppCompatActivity {

    Button createMAITemplateButton;
    Button createMAIButton;
    Button checkMAIButton;

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
                        new CreateIntentOnClick(CreateMAIActivity.class)
                );

        createMAITemplateButton
                .setOnClickListener(
                        new CreateIntentOnClick(CreateMAITemplateActivity.class)
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

        //Создание базы данных
        AppDatabase.getAppDatabase(getApplicationContext());
        setOnClickListeners();
    }
}