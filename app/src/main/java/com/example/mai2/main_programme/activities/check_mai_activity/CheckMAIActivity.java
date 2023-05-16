package com.example.mai2.main_programme.activities.check_mai_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.mai2.R;
import com.example.mai2.main_programme.db.database.AppDatabase;

public class CheckMAIActivity extends AppCompatActivity {

    AppDatabase db;

    private void initialize(){
        db = AppDatabase.getAppDatabase(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mai_activity);
        initialize();

    }
}