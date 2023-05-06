package com.example.mai2.main_programme.algorithm.click_listeners;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.mai2.R;
import com.example.mai2.main_programme.algorithm.color.ColorGenerator;

import java.util.Arrays;

//Класс матрицы, обрабатывающий нажатие на клавиатуру телефона
//(Правило построения матрицы Aij = 1/Aji + смена цвета по шкале от 1 до 9)
public class CellKeyListener implements View.OnKeyListener {
    private static final int[] KEYCODE_WHITELIST;
    private final Resources resources;

    private final EditText inverseEditText;

    private GradientDrawable background, inverseBackground;

    private ColorGenerator negativeGenerator, positiveGenerator;

    static {
        //Генерация разрешённого массива (1, 2, 3, 4, 5, 6, 7, 8, 9)
        KEYCODE_WHITELIST = new int[9];
        int startKeyCode = KeyEvent.KEYCODE_1;
        for (int i = 0; i < KEYCODE_WHITELIST.length; ++i){
            KEYCODE_WHITELIST[i] = startKeyCode + i;
        }
    }

    public CellKeyListener(Context context, TableLayout layout, int i, int j){
        TableRow inverseTableRow = (TableRow) layout.getChildAt(i);
        this.inverseEditText = (EditText) inverseTableRow.getChildAt(j);
        this.resources = context.getResources();

        //Mutable backgrounds
        background = (GradientDrawable) resources.getDrawable(R.drawable.empty_cell_shape);
        inverseBackground = (GradientDrawable) resources.getDrawable(R.drawable.empty_cell_shape);
        background = (GradientDrawable) background.mutate();
        inverseBackground = (GradientDrawable) inverseBackground.mutate();

        //Цвета рейтинга
        int maxNegativeRatingColor = resources.getColor(R.color.min_positive_rating);
        int maxPositiveRatingColor = resources.getColor(R.color.max_positive_rating);
        int minNegativeRatingColor = resources.getColor(R.color.max_negative_rating);

        negativeGenerator = new ColorGenerator(9,
                maxNegativeRatingColor,
                minNegativeRatingColor);
        positiveGenerator = new ColorGenerator(9,
                maxNegativeRatingColor,
                maxPositiveRatingColor);
    }

    private boolean keyCodeInWhiteList(int keyCode){
        return Arrays.binarySearch(KEYCODE_WHITELIST, keyCode) >= 0;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            //Обработка Backspace
            if (keyCode == KeyEvent.KEYCODE_DEL){
                //Очистка клеток
                ((EditText) v).setText("");
                inverseEditText.setText("");

                Drawable standardBackground = resources.getDrawable(R.drawable.empty_cell_shape);
                inverseEditText.setBackground(standardBackground);
                v.setBackground(standardBackground);

                return true;
            }


            if (!keyCodeInWhiteList(keyCode)) return true;

            //Действие для разрешённых клавиш
            String s = "1";
            char ch = ((char) event.getUnicodeChar());
            if (ch != '1') {
                s += "/" + ch;
            }

            int posColorOfNumber = positiveGenerator
                    .getColorOfNumber(Integer.parseInt("" + ch));
            background.setColor(posColorOfNumber);
            v.setBackground(background);

            int negColorOfNumber = negativeGenerator
                    .getColorOfNumber(Integer.parseInt("" + ch));
            inverseBackground.setColor(negColorOfNumber);
            inverseEditText.setBackground(inverseBackground);

            ((EditText) v).setText(Character.toString(ch));
            inverseEditText.setText(s);
            return true;
        }
        return false;
    }
}
