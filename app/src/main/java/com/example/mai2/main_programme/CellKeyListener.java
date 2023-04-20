package com.example.mai2.main_programme;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

//Класс матрицы, обрабатывающий нажатие на клавиатуру телефона
public class CellKeyListener implements View.OnKeyListener {
    private final TableLayout layout;
    private final int i, j;

    public CellKeyListener(TableLayout layout, int i, int j){
        this.layout = layout;
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            TableRow inverseTableRow = (TableRow) layout.getChildAt(i);
            EditText inverseEditText = (EditText) inverseTableRow.getChildAt(j);

            //Действие на Backspace и на 0
            if (keyCode == KeyEvent.KEYCODE_DEL
                    || keyCode == KeyEvent.KEYCODE_0){
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

            ((EditText) v).setText(Character.toString(ch));
            inverseEditText.setText(s);
            return true;
        }
        return false;
    }
}
