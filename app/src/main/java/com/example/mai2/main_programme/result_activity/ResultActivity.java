package com.example.mai2.main_programme.result_activity;


import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.view.LayoutInflater;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.mai2.R;
import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.result_activity.calculating_thread.CalculatingThread;
import com.example.mai2.main_programme.result_activity.generate_layout_thread.LayoutGeneratorThread;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;

public class ResultActivity extends AppCompatActivity {
    LinearLayout resultContainer;
    Button endViewButton;
    LayoutInflater layoutInflater;

    //Синхронизатор потока LGT и CT, (1 операция для подстраховки)
    private final int operationsCount
            = (Constants.CRITERIA.length + 1) * Constants.CANDIDATES.length;
    private final CountDownLatch LAYOUT_GENERATED_FLAG
            = new CountDownLatch(operationsCount);

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

        ArrayList<TextView> valueArray = new ArrayList<>();
        LayoutGeneratorThread lgt =
                new LayoutGeneratorThread(layoutInflater,
                        resultContainer,
                        valueArray,
                        LAYOUT_GENERATED_FLAG);
        lgt.start();

        CalculatingThread ct = new CalculatingThread(this,
                resultContainer,
                valueArray,
                LAYOUT_GENERATED_FLAG);
        ct.start();
    }
}