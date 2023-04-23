package com.example.mai2.main_programme.main_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mai2.R;
import com.example.mai2.main_programme.Algorithm;
import com.example.mai2.main_programme.ResultActivity;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    TextView commandText;
    FrameLayout container;
    Button nextButton;

    LayoutInflater inflater;

    public static final String[] CRITERIA = new String[]{"Местонахождение", "Репутация"};
    public static final String[] CANDIDATES = new String[]{"ИГУ", "ИрНИТУ", "ИрГУПС"};
    public static final String ANSWER_FILENAME = "answer_txt";

    //Метод инициализации полей, связанных с разметкой
    private void initialize(){
        commandText = findViewById(R.id.command_text);
        container = findViewById(R.id.answer_matrix);
        nextButton = findViewById(R.id.next_button);
        inflater = getLayoutInflater();
    }

    //Класс для взаимодействия параллельного потока и интерфейса
    private boolean buttonIsClickable = false;
    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    class GenerateMatrixHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            TableLayout tl = (TableLayout) msg.obj;
            container.removeAllViews();
            container.addView(tl);
            //Логика для кнопки (именно в хэндлере, иначе надо синхронизировать потоки)
            if (!buttonIsClickable) {
                //Обнуление файла
                try {
                    BufferedWriter bw = new BufferedWriter(
                            new OutputStreamWriter(
                                    openFileOutput(ANSWER_FILENAME, MODE_PRIVATE)
                            )
                    );
                    bw.write("");
                    bw.close();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            res.append(CRITERIA.length).append(" ");
                            try {
                                res.append(Algorithm.matrixToString(tl));
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            //Генерация матрицы кандидатов
                            GenerateMatrixThread gmt = new GenerateMatrixThread(CANDIDATES, 3);
                            gmt.start();

                            criteriaMatrixWas = true;
                        }

                        //Сценарий для матриц кандидатов
                        else if (candidatesCount < CRITERIA.length){
                            TableLayout tl = (TableLayout) container.getChildAt(0);
                            if (candidatesCount == 0) res.append(CANDIDATES.length).append(" ");
                            try {
                                res.append(Algorithm.matrixToString(tl)).append(" ");
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Algorithm.clearMatrix(tl);
                            ++candidatesCount;
                        }

                        //Запись ответа в файл
                        try {
                            BufferedWriter bw = new BufferedWriter(
                                    new OutputStreamWriter(
                                            openFileOutput(ANSWER_FILENAME, MODE_APPEND)
                                    )
                            );
                            bw.write(res.toString());
                            bw.close();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        //Переход к просмотру результатов
                        if (candidatesCount == CRITERIA.length)  {
                            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                            MainActivity.this.startActivity(intent);
                            MainActivity.this.finishActivity(200);
                            return;
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
        private final String[] names;
        private final int lengthOfName;

        public GenerateMatrixThread(String[] names, int lengthOfName){
            this.handler = new GenerateMatrixHandler();

            this.context = MainActivity.this;
            this.localInflater = inflater;
            this.names = names.clone();
            this.lengthOfName = lengthOfName;
        }

        @Override
        public void run() {
            TableLayout tl =
                    Algorithm.generateSquareMatrixTableLayout(
                        context, localInflater, names, lengthOfName
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

        GenerateMatrixThread gmt =
                new GenerateMatrixThread(CRITERIA, 3);
        gmt.start();
    }
}