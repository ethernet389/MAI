package com.example.mai2.main_programme.algorithm.matrix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mai2.R;
import com.example.mai2.main_programme.algorithm.matrix.click_listeners.CellKeyListener;
import com.example.mai2.main_programme.algorithm.matrix.click_listeners.HeaderClickListener;

public class Algorithm {
    //Метод генерации матрицы в TableLayout, учитывая имена и их порядок
    @SuppressLint("InflateParams")
    public static TableLayout generateSquareMatrixTableLayout(
            Context context,
            LayoutInflater inflater,
            String[] inputNames,
            int lengthOfName){

        String[] names = new String[inputNames.length];
        //Укорачивание всех названий (имён) до lengthOfName
        for (int i = 0; i < names.length; ++i){
            String str;
            if (inputNames[i].length() >= lengthOfName) {
                str = inputNames[i].substring(0, lengthOfName).toUpperCase();
            }
            else {
                str = inputNames[i].toUpperCase();
            }
            names[i] = str;
        }
        //Сокращения для частоиспользуемых переменных
        final int size = names.length;
        final int rowLayoutId = R.layout.row_matrix_layout;
        final int editLayoutId = R.layout.edit_text_matrix_layout;
        final int textLayoutId = R.layout.text_matrix_layout;

        TableLayout layout = new TableLayout(context);

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
                    Drawable cellBackground = context.getResources()
                            .getDrawable(R.drawable.cell_shape);
                    cell.setBackground(cellBackground);
                }
                tr.addView(cell);
            }
            layout.addView(tr);
        }

        //Логика для строки с заголовками (нулевая строка матрицы)
        TableRow firstHeaderRow = (TableRow) layout.getChildAt(0);
        for (int j = 1; j <= size; ++j){
            TextView tv = (TextView) firstHeaderRow.getChildAt(j);
            tv.setOnClickListener(new HeaderClickListener(context, inputNames[j - 1]));
        }

        //Логика для столбца с заголовками (нулевой столбец матрицы)
        for (int i = 1; i <= size; ++i){
            TableRow headerRow = (TableRow) layout.getChildAt(i);
            TextView tv = (TextView) headerRow.getChildAt(0);
            tv.setOnClickListener(new HeaderClickListener(context, inputNames[i - 1]));
        }

        //Логика для формы для заполнения
        for (int i = 1; i <= size; ++i){
            TableRow tr = (TableRow) layout.getChildAt(i);
            for (int j = 1; j <= size; ++j){
                if (i == j) continue;
                EditText et = (EditText) tr.getChildAt(j);
                et.setOnKeyListener(new CellKeyListener(context, layout, j, i));
            }
        }
        return layout;
    }

    //Очистка матрицы от введённых значений, смена фона на стандартный
    static public void clearMatrix(TableLayout layout, Drawable background){
        for (int i = 1; i < layout.getChildCount(); ++i){
            TableRow tr = (TableRow) layout.getChildAt(i);
            for (int j = 1; j < tr.getChildCount(); ++j){
                if (i == j) continue;
                EditText et = (EditText) tr.getChildAt(j);
                et.setBackground(background);
                et.setText("");
            }
        }
    }

    //Парсер для матрицы
    static public String matrixToString(TableLayout layout)
            throws ParseMatrixException {
        StringBuilder ret = new StringBuilder();
        Context context = layout.getContext();

        for (int i = 1; i < layout.getChildCount(); ++i){
            TableRow tr = (TableRow) layout.getChildAt(i);
            for (int j = 1; j < tr.getChildCount(); ++j){
                TextView cell = (TextView) tr.getChildAt(j);
                String cellText = cell.getText().toString();

                //Обработка случая, когда не все клетки заполнены
                if (cellText.isEmpty()){
                    throw new ParseMatrixException(
                            context.getString(R.string.matrix_has_empty_cell_message), i, j
                    );
                }

                //Вычисление числа (Преобразование строки в число)
                double num;
                if (cellText.length() > 1) {
                    //Обработка случая вставки через буфер
                    if (cellText.charAt(1) != '/' || cellText.charAt(0) != '1'){
                        throw new ParseMatrixException(
                                context.getString(R.string.matrix_error_format_message), i, j
                        );
                    }

                    double denominator;
                    denominator = Double.parseDouble("" + cellText.charAt(2));
                    num = 1 / denominator;
                }
                else {
                    num = Double.parseDouble("" + cellText.charAt(0));
                }
                ret.append(num).append(" ");
            }
        }

        return ret.toString();
    }
}
