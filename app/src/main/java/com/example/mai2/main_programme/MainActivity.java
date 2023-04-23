package com.example.mai2.main_programme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mai2.R;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;

public class MainActivity extends AppCompatActivity {
    TextView commandText;
    FrameLayout container;
    Button nextButton;

    LayoutInflater inflater;

    public static final String[] criteria = new String[]{"Местонахождение", "Репутация"};
    public static final String[] candidates = new String[]{"ИГУ", "ИрНИТУ", "ИрГУПС"};
    public static final String ANSWER_FILENAME = "answer_txt";

    //Метод инициализации полей, связанных с разметкой
    private void initialize(){
        commandText = findViewById(R.id.command_text);
        container = findViewById(R.id.answer_matrix);
        nextButton = findViewById(R.id.next_button);
        inflater = getLayoutInflater();
    }

    //Класс для взаимодействия параллельного потока и интерфейса
    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    class GenerateMatrixHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            TableLayout tl = (TableLayout) msg.obj;
            container.addView(tl);

            //Класс для переключения вопросов
            class OnNextClickListener implements View.OnClickListener {
                //TODO: логика продолжения сценария
                @Override
                public void onClick(View v) {
                    try {
                        BufferedWriter bw = new BufferedWriter(
                                new OutputStreamWriter(
                                        getApplicationContext().openFileOutput(ANSWER_FILENAME, MODE_APPEND)
                                )
                        );
                        bw.write(Algorithm.matrixToString(tl));
                        bw.close();
                    }
                    catch (Exception e) {
                        Toast.makeText(
                                MainActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }

            nextButton.setOnClickListener(new OnNextClickListener());
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

            this.context = getApplicationContext();
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
                new GenerateMatrixThread(criteria, 3);
        gmt.start();
    }
}