package com.example.mai2.main_programme;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Arrays;

//Класс матрицы, обрабатывающий нажатие на клавиатуру телефона
public class CellKeyListener implements View.OnKeyListener {
    private final TableLayout layout;
    private final int i, j;
    private static final int[] KEYCODE_BLACK_LIST
            = new int[]{KeyEvent.KEYCODE_DEL,
            KeyEvent.KEYCODE_0,
            KeyEvent.KEYCODE_ENTER,
            KeyEvent.KEYCODE_NUMPAD_DOT,
            KeyEvent.KEYCODE_NUMPAD_COMMA};

    static {
        //Сортировка для бинарного поиска
        Arrays.sort(KEYCODE_BLACK_LIST);
    }

    public CellKeyListener(TableLayout layout, int i, int j){
        this.layout = layout;
        this.i = i;
        this.j = j;
    }

    private boolean keyCodeInBlackList(int keyCode){
        boolean status = Arrays.binarySearch(KEYCODE_BLACK_LIST, keyCode) >= 0;
        return status;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            TableRow inverseTableRow = (TableRow) layout.getChildAt(i);
            EditText inverseEditText = (EditText) inverseTableRow.getChildAt(j);

            //Действие на запрещённые клавиши
            if (keyCodeInBlackList(keyCode)){
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
