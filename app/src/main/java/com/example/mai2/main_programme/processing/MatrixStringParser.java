package com.example.mai2.main_programme.processing;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MatrixStringParser {
    static public String parseString(TableLayout layout){
        StringBuilder ret = new StringBuilder();

        for (int i = 1; i < layout.getChildCount(); ++i){
            TableRow tr = (TableRow) layout.getChildAt(i);
            for (int j = 1; j < tr.getChildCount(); ++j){
                TextView cell = (TextView) tr.getChildAt(j);
                String cellText = cell.getText().toString();

                //Вычисление числа (Преобразование из строки в число)
                double num;
                if (cellText.length() > 1){
                    double firstNum, secondNum;
                    firstNum = (double) cellText.charAt(0);
                    secondNum = (double) cellText.charAt(2);
                    num = firstNum / secondNum;
                }
                else {
                    num = (double) cellText.charAt(0);
                }
                ret.append(num).append(" ");
            }
        }

        return ret.toString();
    }
}
