package com.example.mai2.main_programme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mai2.R;

public class MainActivity extends AppCompatActivity {
    TextView commandText;
    FrameLayout container;
    Button nextButton;

    LayoutInflater inflater;

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
        GenerateMatrixHandler handler;
        String[] names;

        public GenerateMatrixThread(GenerateMatrixHandler handler, String[] names){
            this.handler = handler;
            this.names = names.clone();
        }

        @Override
        public void run() {
            TableLayout tl = generateSquareMatrixTableLayout(names, 3);
            Message msg = new Message();
            msg.obj = tl;
            handler.sendMessage(msg);
        }
    }

    //Метод генерации матрицы в TableLayout, учитывая имена и их порядок
    @SuppressLint("InflateParams")
    private TableLayout generateSquareMatrixTableLayout(String[] inputNames, int lengthOfName){

        String[] names = new String[inputNames.length];
        //Укорачивание всех названий (имён) до lengthOfName
        for (int i = 0; i < names.length; ++i){
            names[i] = inputNames[i].substring(0, lengthOfName).toUpperCase();
        }
        //Сокращения для частоиспользуемых переменных
        final int size = names.length;
        final int rowLayoutId = R.layout.row_matrix_layout;
        final int editLayoutId = R.layout.edit_text_matrix_layout;
        final int textLayoutId = R.layout.text_matrix_layout;


        TableLayout layout = new TableLayout(this);

        //Генерация первой строки матрицы (заголовков)
        TableRow firstRow = (TableRow) inflater.inflate(rowLayoutId, null);
        TextView firstEmptyText = (TextView) inflater.inflate(textLayoutId, null);
        firstEmptyText.setText("   ");
        firstRow.addView(firstEmptyText);

        //Заполнение первой строки заголовками
        for (String name : names) {
            TextView ft = (TextView) inflater.inflate(textLayoutId, null);
            ft.setText(name);
            firstRow.addView(ft);
        }
        layout.addView(firstRow);

        //Генерация полей для заполнения и заголовков в первом столбце
        for (int i = 0; i < size; ++i){
            TableRow tr = (TableRow) inflater.inflate(rowLayoutId, null);
            TextView tv = (TextView)  inflater.inflate(textLayoutId, null);
            tv.setText(names[i]);
            tr.addView(tv);

            for (int j = 0; j < size; ++j){
                View cell;
                //Все элементы главной диагонали матрицы равны единице
                if (i == j){
                    cell = inflater.inflate(textLayoutId, null);
                    ((TextView) cell).setText("1");
                }
                else {
                    cell = inflater.inflate(editLayoutId, null);
                }
                tr.addView(cell);
            }
            layout.addView(tr);
        }

        //Создание логики для каждого элемента матрицы (исключая главную диагональ и заголовки)
        for (int i = 1; i <= size; ++i){
            TableRow tr = (TableRow) layout.getChildAt(i);
            for (int j = 1; j <= size; ++j){
                if (i == j) continue;
                EditText et = (EditText) tr.getChildAt(j);
                et.setOnKeyListener(new CellKeyListener(layout, j, i));
            }
        }
        return layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        GenerateMatrixHandler<FrameLayout> handler = new GenerateMatrixHandler<>(container);
        GenerateMatrixThread gmt =
                new GenerateMatrixThread(
                        handler, new String[]{"Игу", "Мгу", "Политех"}
                );
        gmt.start();

    }
}