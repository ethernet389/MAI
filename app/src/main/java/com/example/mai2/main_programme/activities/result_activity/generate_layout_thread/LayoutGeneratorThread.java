package com.example.mai2.main_programme.activities.result_activity.generate_layout_thread;

import android.os.Message;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mai2.R;
import com.example.mai2.main_programme.Constants;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class LayoutGeneratorThread extends Thread{
    private final LayoutInflater layoutInflater;
    private final LayoutGeneratorHandler handler;

    private final CountDownLatch count;
    private final ArrayList<TextView> valueTextArray;

    private final String[] criteria, candidates;

    public LayoutGeneratorThread(LayoutInflater layoutInflater,
                                 LinearLayout tl,
                                 ArrayList<TextView> valueTextArray,
                                 String[] criteria,
                                 String[] candidates,
                                 CountDownLatch count){
        this.criteria = criteria;
        this.candidates = candidates;
        this.layoutInflater = layoutInflater;
        this.handler = new LayoutGeneratorHandler(tl);
        this.valueTextArray = valueTextArray;
        this.count = count;
    }

    private void sendObject(Object obj){
        Message msg = new Message();
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    private void generateTableLayoutWithName(String name){
        //Заголовок таблицы
        TableRow titleRow = (TableRow) layoutInflater.inflate(R.layout.row_result_layout, null);
        TextView titleText = (TextView) layoutInflater.inflate(R.layout.result_row_text_view_layout, null);
        titleText.setText(name);
        titleRow.addView(titleText);
        sendObject(titleRow);

        //Таблица для результатов
        TableLayout table = (TableLayout) layoutInflater.inflate(R.layout.matrix_layout, null);
        for (int i = 0; i < candidates.length; ++i){
            TableRow valueRow = (TableRow) layoutInflater.inflate(R.layout.row_result_layout, null);

            //Заголовок строки
            TextView headerText = (TextView) layoutInflater.inflate(R.layout.result_row_text_view_layout, null);
            headerText.setText(candidates[i]);
            //Значение для строки
            TextView valueText = (TextView) layoutInflater.inflate(R.layout.result_row_text_view_layout, null);
            valueText.setText("-");

            valueRow.addView(headerText);
            valueRow.addView(valueText);
            table.addView(valueRow);

            //Добавление элемента значения
            valueTextArray.add(valueText);
            count.countDown();
        }
        sendObject(table);
    }

    @Override
    public void run() {
        for (int i = 0; i < criteria.length; ++i){
            generateTableLayoutWithName(criteria[i]);
        }
        generateTableLayoutWithName("Общий рейтинг");
    }
}