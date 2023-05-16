package com.example.mai2.main_programme.activities.create_mai_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mai2.R;
import com.example.mai2.main_programme.db.database.AppDatabase;

public class CreateMAIActivity extends AppCompatActivity {

    AppDatabase db;

    private void initialize(){
        db = AppDatabase.getAppDatabase(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mai_activity);
        initialize();
    }
}