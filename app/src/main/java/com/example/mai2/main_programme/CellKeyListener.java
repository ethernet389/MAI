package com.example.mai2.main_programme;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;

//Класс матрицы, обрабатывающий нажатие на клавиатуру телефона
public class CellKeyListener implements View.OnKeyListener {
    private final TableLayout layout;
    private final int i, j;
    private static final int[] KEYCODE_WHITELIST;

    static {
        //Генерация разрешённого массива (1, 2, 3, 4, 5, 6, 7, 8, 9)
        KEYCODE_WHITELIST = new int[9];
        int startKeyCode = KeyEvent.KEYCODE_1;
        for (int i = 0; i < KEYCODE_WHITELIST.length; ++i){
            KEYCODE_WHITELIST[i] = startKeyCode + i;
        }
    }

    public CellKeyListener(TableLayout layout, int i, int j){
        this.layout = layout;
        this.i = i;
        this.j = j;
    }

    private boolean keyCodeInWhiteList(int keyCode){
        return Arrays.binarySearch(KEYCODE_WHITELIST, keyCode) >= 0;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            TableRow inverseTableRow = (TableRow) layout.getChildAt(i);
            EditText inverseEditText = (EditText) inverseTableRow.getChildAt(j);

            //Обработка Backspace
            if (keyCode == KeyEvent.KEYCODE_DEL){
                ((EditText) v).setText("");
                inverseEditText.setText("");
            }

            //Обработка запрещённых клавиш (не устраняет случаи вставки текста через буфер обмена)
            if (!keyCodeInWhiteList(keyCode)) return true;

            //Действие для разрешённых клавиш
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
