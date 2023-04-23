package com.example.mai2.main_programme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mai2.R;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    TextView commandText;
    FrameLayout container;
    Button nextButton;

    LayoutInflater inflater;

    public static final String[] criteria = new String[]{"Location", "Reputation", "Ambition", "Interes", "Art", "Header"};
    public static final String[] candidates = new String[]{"A", "B", "C"};
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
    static class GenerateMatrixHandler<T extends ViewGroup> extends Handler {
        private final T container;

        public GenerateMatrixHandler(T container){
            this.container = container;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            TableLayout tl = (TableLayout) msg.obj;
            container.addView(tl);
        }
    }
    //Класс параллельного потока для создания матрицы
    @SuppressWarnings("rawtypes")
    class GenerateMatrixThread extends Thread{
        private GenerateMatrixHandler handler;

        private Context context;
        private LayoutInflater inflater;
        private String[] names;
        private int lengthOfName;

        public GenerateMatrixThread(
                Context context,
                LayoutInflater inflater,
                GenerateMatrixHandler handler,
                String[] names,
                int lengthOfName){

            this.context = context;
            this.inflater = inflater;
            this.handler = handler;
            this.names = names.clone();
            this.lengthOfName = lengthOfName;
        }

        @Override
        public void run() {
            TableLayout tl =
                    Algorithm.generateSquareMatrixTableLayout(
                        context,
                        inflater,
                        names,
                        lengthOfName
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

        GenerateMatrixHandler<FrameLayout> handler = new GenerateMatrixHandler<>(container);
        GenerateMatrixThread gmt =
                new GenerateMatrixThread(
                        this, inflater, handler, criteria, 3
                );
        gmt.start();
    }
}