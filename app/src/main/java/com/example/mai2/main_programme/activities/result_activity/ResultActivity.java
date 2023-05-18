package com.example.mai2.main_programme.activities.result_activity;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.mai2.R;
import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.activities.result_activity.calculating_thread.CalculatingThread;
import com.example.mai2.main_programme.activities.result_activity.generate_layout_thread.LayoutGeneratorThread;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ResultActivity extends AppCompatActivity {
    LinearLayout resultContainer;
    Button endViewButton;
    LayoutInflater layoutInflater;

    String[] criteria;
    String[] candidates;

    private void initialize(){
        resultContainer = findViewById(R.id.result_container);
        endViewButton = findViewById(R.id.end_button);
        layoutInflater = getLayoutInflater();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initialize();

        Intent pastIntent = getIntent();
        criteria = pastIntent.getStringArrayExtra("criteria");
        candidates = pastIntent.getStringArrayExtra("candidates");

        final int operationsCount
                = (criteria.length + 1) * candidates.length;

        final CountDownLatch LAYOUT_GENERATED_FLAG
                = new CountDownLatch(operationsCount);

        ArrayList<TextView> valueArray = new ArrayList<>();
        LayoutGeneratorThread lgt =
                new LayoutGeneratorThread(layoutInflater,
                        resultContainer,
                        valueArray,
                        criteria,
                        candidates,
                        LAYOUT_GENERATED_FLAG);
        lgt.start();

        CalculatingThread ct = new CalculatingThread(this,
                valueArray,
                criteria,
                candidates,
                LAYOUT_GENERATED_FLAG);
        ct.start();
    }
}