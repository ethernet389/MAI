package com.example.mai2.main_programme.result_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mai2.R;
import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.math.Buffer;
import com.example.mai2.main_programme.math.CalculatingClass;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Scanner;

public class ResultActivity extends AppCompatActivity {
    LinearLayout resultContainer;
    Button endViewButton;
    LayoutInflater layoutInflater;

    private void initialize(){
        resultContainer = findViewById(R.id.result_container);
        endViewButton = findViewById(R.id.end_button);
        layoutInflater = getLayoutInflater();
    }

    static class LayoutGeneratorThread extends Thread{
        private final LayoutInflater layoutInflater;
        private final LayoutGeneratorHandler handler;

        public LayoutGeneratorThread(LayoutInflater layoutInflater, LinearLayout tl){
            this.layoutInflater = layoutInflater;
            this.handler = new LayoutGeneratorHandler(tl);
        }

        private void sendObjectToHandler(Object obj){
            Message msg = new Message();
            msg.obj = obj;
            handler.sendMessage(msg);
        }

        private void generateTableLayoutWithName(String name){
            TableRow titleRow = (TableRow) layoutInflater.inflate(R.layout.row_result_layout, null);
            TextView titleText = (TextView) layoutInflater.inflate(R.layout.result_row_text_view_layout, null);
            titleText.setText(name);
            titleRow.addView(titleText);
            sendObjectToHandler(titleRow);

            TableLayout tableLayout = (TableLayout) layoutInflater.inflate(R.layout.talbe_layout, null);
            TableRow headerRow = (TableRow) layoutInflater.inflate(R.layout.row_result_layout, null);
            for (int j = 0; j < Constants.CANDIDATES.length; ++j){
                TextView header = (TextView) layoutInflater.inflate(R.layout.result_row_text_view_layout, null);
                header.setText(Constants.CANDIDATES[j]);
                headerRow.addView(header);
            }
            tableLayout.addView(headerRow);
            TableRow valueRow = (TableRow) layoutInflater.inflate(R.layout.row_result_layout, null);
            for (int j = 0; j < Constants.CANDIDATES.length; ++j){
                View valueOfCandidate = layoutInflater.inflate(R.layout.result_row_text_view_layout, null);
                valueRow.addView(valueOfCandidate);
            }
            tableLayout.addView(valueRow);
            sendObjectToHandler(tableLayout);
        }

        @Override
        public void run() {
            for (int i = 0; i < Constants.CRITERIA.length; ++i){
                generateTableLayoutWithName(Constants.CRITERIA[i]);
            }
            generateTableLayoutWithName("Общий рейтинг");
        }
    }

    @SuppressLint("HandlerLeak")
    static class LayoutGeneratorHandler extends Handler{
        private LinearLayout layout;

        public LayoutGeneratorHandler(LinearLayout layout){
            this.layout = layout;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            layout.addView((View) msg.obj);
        }
    }

    static class CalculatingThread extends Thread{
        private CalculatingHandler handler;
        private Context context;

        public CalculatingThread(Context context, LinearLayout layout, LayoutGeneratorThread lgt){
            this.handler = new CalculatingHandler(lgt, layout);
            this.context = context;
        }

        @Override
        public void run() {
            try {
                Scanner data = new Scanner(
                        new InputStreamReader(
                                context.openFileInput(Constants.ANSWER_FILENAME)
                        )
                );
                data.useLocale(Locale.CANADA);

                Buffer buffer = CalculatingClass.calculate(data);
                data.close();
                Message msg = new Message();
                msg.obj = buffer;
                handler.sendMessage(msg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    static class CalculatingHandler extends Handler{
        private final LayoutGeneratorThread lgt;
        private final LinearLayout layout;
        private Buffer buffer;

        public CalculatingHandler(LayoutGeneratorThread lgt, LinearLayout layout){
            this.lgt = lgt;
            this.layout = layout;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            buffer = (Buffer) msg.obj;

            while(lgt.isAlive());
            //todo: Чини эту штуку
            for(int i = 0; i < layout.getChildCount() - 1; ++i){
                TableLayout valueTable = (TableLayout) layout.getChildAt(i);
                TableRow valueRow = (TableRow) valueTable.getChildAt(valueTable.getChildCount() - 2);
                for (int j = 0; j < valueRow.getChildCount(); ++j){
                    TextView valueText = (TextView) valueRow.getChildAt(j);
                    double roundedValue
                            = Math.round(buffer.relativeWeightsOfEachCandidateForEachOfCriteria.get(i)[j] * 100) / 100;
                    valueText.setText(Double.toString(roundedValue));
                }
            }

            TableLayout valueTable
                    = (TableLayout) layout.getChildAt(layout.getChildCount() - 1);
            TableRow valueRow = (TableRow) valueTable.getChildAt(valueTable.getChildCount() - 1);
            for (int j = 0; j < valueRow.getChildCount(); ++j){
                TextView valueText = (TextView) valueRow.getChildAt(j);
                double roundedValue
                        = Math.round(buffer.finalRatingEachOfCandidate[j] * 100) / 100;
                valueText.setText(Double.toString(roundedValue));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initialize();

        LayoutGeneratorThread lgt = new LayoutGeneratorThread(layoutInflater, resultContainer);
        lgt.start();

        CalculatingThread ct = new CalculatingThread(this, resultContainer, lgt);
        ct.start();
    }
}