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

    private void initialize(){
        commandText = findViewById(R.id.command_text);
        container = findViewById(R.id.answer_matrix);
        nextButton = findViewById(R.id.next_button);

        inflater = getLayoutInflater();
    }

    class GenerateMatrixHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            TableLayout tl = (TableLayout) msg.obj;
            container.addView(tl);
        }
    }

    class GenerateMatrixThread extends Thread{
        GenerateMatrixHandler handler;
        String[] names;

        public GenerateMatrixThread(GenerateMatrixHandler handler, String[] names){
            this.handler = handler;
            this.names = names.clone();
        }

        @Override
        public void run() {
            TableLayout tl = generateSquareMatrixLayout(names);
            Message msg = new Message();
            msg.obj = tl;
            handler.sendMessage(msg);
        }
    }

    private TableLayout generateSquareMatrixLayout(String[] names){

        String[] namesOfColumnsAndRows = new String[names.length];
        for (int i = 0; i < namesOfColumnsAndRows.length; ++i){
            namesOfColumnsAndRows[i] = names[i].substring(0, 3).toUpperCase();
        }
        final int size = namesOfColumnsAndRows.length;
        TableLayout layout = new TableLayout(this);

        //Генерация первой строки матрицы
        TableRow firstRow = (TableRow) inflater.inflate(R.layout.row_matrix_layout, null);
        TextView firstEmptyText = (TextView) inflater.inflate(R.layout.text_matrix_layout, null);
        firstEmptyText.setText("   ");
        firstRow.addView(firstEmptyText);

        //Заполнение первой строки заголовками
        for (int i = 0; i < size; ++i) {
            TextView ft = (TextView) inflater.inflate(R.layout.text_matrix_layout, null);
            ft.setText(namesOfColumnsAndRows[i].toUpperCase());
            firstRow.addView(ft);
        }
        layout.addView(firstRow);

        //Генерация полей для заполнения и заголовков в столбцах
        int columnHeadersCounter = 0;
        for (int i = 1; i <= size; ++i){
            TableRow tr = (TableRow) inflater.inflate(R.layout.row_matrix_layout, null);
            TextView tv = (TextView)  inflater.inflate(R.layout.text_matrix_layout, null);
            tv.setText(namesOfColumnsAndRows[columnHeadersCounter++].toUpperCase());
            tr.addView(tv);

            for (int j = 1; j <= size; ++j){
                View cell;
                //Все элементы главной диагонали равны единице
                if (j == i){
                    cell = inflater.inflate(R.layout.text_matrix_layout, null);
                    ((TextView) cell).setText("1");
                }
                else {
                    cell = inflater.inflate(R.layout.edit_text_matrix_layout, null);
                }
                tr.addView(cell);
            }
            layout.addView(tr);
        }
        return layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        GenerateMatrixHandler handler = new GenerateMatrixHandler();

        GenerateMatrixThread gmt =
                new GenerateMatrixThread(
                        handler, new String[]{"Игу", "Мгу", "Политех", "Игу", "Мгу", "Политех","Игу", "Мгу", "Политех",
                        "Игу", "Мгу", "Политех", "Игу", "Мгу", "Политех","Игу", "Мгу", "Политех"}
                );
        gmt.start();
    }
}