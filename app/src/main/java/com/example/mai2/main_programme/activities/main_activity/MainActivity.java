package com.example.mai2.main_programme.activities.main_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.create_mai_activity.ChooseMAIConfigActivity;
import com.example.mai2.main_programme.activities.create_mai_activity.CreateMAIActivity;
import com.example.mai2.main_programme.activities.main_activity.workers.GetCriteriaWorker;
import com.example.mai2.main_programme.activities.result_activity.ResultActivity;
import com.example.mai2.main_programme.algorithm.matrix.Algorithm;
import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.algorithm.matrix.ParseMatrixException;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    TextView commandText;
    FrameLayout container;
    Button nextButton;

    LayoutInflater inflater;
    Resources resources;

    String[] criteria;
    String[] candidates;

    //Метод инициализации полей, связанных с разметкой
    private void initialize(){
        commandText = findViewById(R.id.command_text);
        container = findViewById(R.id.answer_matrix);
        nextButton = findViewById(R.id.next_button);
        inflater = getLayoutInflater();
        resources = getResources();
    }

    //Метод для смены цвета ячейки при неправильном заполнении (подсказка пользователю)
    private void changeCellColorOnParseException(TableLayout tl, ParseMatrixException e){
        TableRow tr, inverseTr;
        EditText et, inverseEt;
        tr = (TableRow) tl.getChildAt(e.getRow());
        inverseTr = (TableRow) tl.getChildAt(e.getColumn());
        et = (EditText) tr.getChildAt(e.getColumn());
        inverseEt = (EditText) inverseTr.getChildAt(e.getRow());

        new CountDownTimer(Constants.millisOfChangeColorCell,
                Constants.millisOfChangeColorCell){

            @Override
            public void onTick(long millisUntilFinished) {
                Drawable exceptionBackground = getDrawable(R.drawable.exception_red_cell_shape);
                et.setBackground(exceptionBackground);
                inverseEt.setBackground(exceptionBackground);
            }

            @Override
            public void onFinish() {
                Drawable emptyBackground = getDrawable(R.drawable.cell_shape);
                et.setBackground(emptyBackground);
                inverseEt.setBackground(emptyBackground);
            }
        }.start();

        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    //Класс для взаимодействия параллельного потока и интерфейса
    private boolean buttonIsClickable = false;
    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    class GenerateMatrixHandler extends Handler {

        private final String[] candidates;
        private final String[] criteria;

        public GenerateMatrixHandler(String[] criteria, String[] candidates){
            this.criteria = criteria;
            this.candidates = candidates;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            TableLayout tl = (TableLayout) msg.obj;
            container.removeAllViews();
            container.addView(tl);
            //Логика для кнопки продолжения (именно в хэндлере, иначе надо синхронизировать потоки)
            if (!buttonIsClickable) {
                //Обнуление файла
                try {
                    BufferedWriter bw = new BufferedWriter(
                            new OutputStreamWriter(
                                    openFileOutput(Constants.ANSWER_FILENAME, MODE_PRIVATE)
                            )
                    );
                    bw.write("");
                    bw.close();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

                //Класс для переключения сценария
                class ScenarioClickListener implements View.OnClickListener{
                    private boolean criteriaMatrixWas = false;
                    private int candidatesCount = 0;

                    @Override
                    public void onClick(View v) {
                        StringBuilder res = new StringBuilder();
                        //Сценарий для матрицы критериев
                        if (!criteriaMatrixWas){
                            res.append(criteria.length).append(" ");
                            try {
                                res.append(Algorithm.matrixToString(tl));
                            } catch (ParseMatrixException e) {
                                changeCellColorOnParseException(tl, e);
                                return;
                            }
                            //Смена вопроса (указания)
                            String text = getString(R.string.command_text_template)
                                    + criteria[candidatesCount];
                            commandText.setText(text);

                            //Генерация матрицы кандидатов
                            GenerateMatrixThread gmt = new GenerateMatrixThread(candidates,
                                    null, 3);
                            gmt.start();

                            criteriaMatrixWas = true;
                        }

                        //Сценарий для матриц кандидатов
                        else if (candidatesCount < criteria.length){
                            TableLayout tl = (TableLayout) container.getChildAt(0);
                            if (candidatesCount == 0) res.append(candidates.length).append(" ");
                            try {
                                res.append(Algorithm.matrixToString(tl)).append(" ");
                            } catch (ParseMatrixException e) {
                                changeCellColorOnParseException(tl, e);
                                return;
                            }

                            //Очистка матрицы
                            Drawable standardBackground = resources.getDrawable(R.drawable.cell_shape);
                            Algorithm.clearMatrix(tl, standardBackground);

                            //Обновление вопроса (указания)
                            ++candidatesCount;
                            if (candidatesCount < criteria.length) {
                                String text = getString(R.string.command_text_template)
                                        + criteria[candidatesCount];
                                commandText.setText(text);
                            }
                        }

                        //Запись ответа в файл
                        try {
                            BufferedWriter bw = new BufferedWriter(
                                    new OutputStreamWriter(
                                            openFileOutput(Constants.ANSWER_FILENAME, MODE_APPEND)
                                    )
                            );
                            bw.write(res.toString());
                            bw.close();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        //Переход к просмотру результатов
                        if (candidatesCount == criteria.length)  {
                            Intent intent = new Intent(getApplicationContext(),
                                    ResultActivity.class);
                            intent.putExtra("criteria", criteria);
                            intent.putExtra("candidates", candidates);
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                ScenarioClickListener scl = new ScenarioClickListener();
                nextButton.setOnClickListener(scl);
                buttonIsClickable = true;
            }
        }
    }
    //Класс параллельного потока для создания матрицы
    class GenerateMatrixThread extends Thread{
        private final GenerateMatrixHandler handler;

        private final Context context;
        private final LayoutInflater localInflater;
        private final String[] criteria, candidates;
        private final int lengthOfName;

        public GenerateMatrixThread(String[] criteria, String[] candidates, int lengthOfName){
            this.handler = new GenerateMatrixHandler(criteria, candidates);

            this.context = MainActivity.this;
            this.localInflater = inflater;
            this.criteria = criteria;
            this.candidates = candidates;
            this.lengthOfName = lengthOfName;
        }

        @Override
        public void run() {
            TableLayout tl =
                    Algorithm.generateSquareMatrixTableLayout(
                        context, localInflater, criteria, lengthOfName
                    );
            Message msg = new Message();
            msg.obj = tl;
            handler.sendMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        Intent pastIntent = getIntent();
        candidates = pastIntent.getStringArrayExtra(CreateMAIActivity.CANDIDATES_KEY);
        String nameOfConfig = pastIntent.getStringExtra(ChooseMAIConfigActivity.NAME_OF_CONFIG_KEY);

        Data data = new Data.Builder().putString("nameOfConfig", nameOfConfig).build();
        OneTimeWorkRequest request = new OneTimeWorkRequest
                .Builder(GetCriteriaWorker.class)
                .setInputData(data)
                .build();

        Observer<WorkInfo> observer = new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)){
                    criteria = workInfo.getOutputData().getStringArray("criteria");
                    GenerateMatrixThread gmt =
                            new GenerateMatrixThread(criteria, candidates, 3);
                    gmt.start();
                }
            }
        };

        WorkManager.getInstance(getApplicationContext()).enqueue(request);
        WorkManager.getInstance(getApplicationContext())
                .getWorkInfoByIdLiveData(request.getId())
                .observe(this, observer);
    }
}