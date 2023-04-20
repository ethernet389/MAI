package com.example.mai2.main_programme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
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

    //Класс для взаимодействия параллельного потока и интерфейса
    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    class GenerateMatrixHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            TableLayout tl = (TableLayout) msg.obj;
            container.addView(tl);
        }
    }
    //Параллельный поток для создания матрицы
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

    @SuppressLint("InflateParams")
    private TableLayout generateSquareMatrixLayout(String[] inputNames){

        String[] names = new String[inputNames.length];
        for (int i = 0; i < names.length; ++i){
            names[i] = inputNames[i].substring(0, 3).toUpperCase();
        }
        final int size = names.length;

        TableLayout layout = new TableLayout(this);

        //Генерация первой строки матрицы
        TableRow firstRow = (TableRow) inflater.inflate(R.layout.row_matrix_layout, null);
        TextView firstEmptyText = (TextView) inflater.inflate(R.layout.text_matrix_layout, null);
        firstEmptyText.setText("   ");
        firstRow.addView(firstEmptyText);

        //Заполнение первой строки заголовками
        for (String name : names) {
            TextView ft = (TextView) inflater.inflate(R.layout.text_matrix_layout, null);
            ft.setText(name);
            firstRow.addView(ft);
        }
        layout.addView(firstRow);

        //Генерация полей для заполнения и заголовков в столбцах
        for (int i = 0; i < size; ++i){
            TableRow tr = (TableRow) inflater.inflate(R.layout.row_matrix_layout, null);
            TextView tv = (TextView)  inflater.inflate(R.layout.text_matrix_layout, null);
            tv.setText(names[i]);
            tr.addView(tv);

            for (int j = 0; j < size; ++j){
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

        //Создание логики для каждой клетки TextChangedListener
        for (int i = 1; i <= size; ++i){
            TableRow tr = (TableRow) layout.getChildAt(i);
            for (int j = 1; j <= size; ++j){
                if (i == j) continue;
                //Логика
                /*
                class EditTextWatcher implements TextWatcher{
                    private final int i, j;

                    public EditTextWatcher(int i, int j){
                        this.i = i;
                        this.j = j;
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        TableRow inverseTableRow = (TableRow) layout.getChildAt(i);
                        EditText inverseEditText = (EditText) inverseTableRow.getChildAt(j);

                        String str = "1/" + s.toString();
                        inverseEditText.setText(str);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                }*/

                class CellKeyListener implements View.OnKeyListener {
                    private final int i, j;

                    public CellKeyListener(int i, int j){
                        this.i = i;
                        this.j = j;
                    }

                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            TableRow inverseTableRow = (TableRow) layout.getChildAt(i);
                            EditText inverseEditText = (EditText) inverseTableRow.getChildAt(j);

                            //Действие на Backspace
                            if (keyCode == KeyEvent.KEYCODE_DEL){
                                ((EditText) v).setText("");
                                inverseEditText.setText("");
                                return true;
                            }

                            //Действие для других клавиш
                            String s = "1";
                            char ch = ((char)event.getUnicodeChar());
                            if (ch != '1'){
                                s += "/" + ch;
                            }

                            ((EditText) v).setText("" + ch);
                            inverseEditText.setText(s);
                            return true;
                        }
                        return false;
                    }
                }

                EditText et = (EditText) tr.getChildAt(j);
                et.setOnKeyListener(new CellKeyListener(j, i));
                Log.d("ADDED", et.getKeyListener().toString());
                //et.addTextChangedListener(new EditTextWatcher(j, i));
            }
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
                        handler, new String[]{"Игу", "Мгу", "Политех"}
                );
        gmt.start();
    }
}