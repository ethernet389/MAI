package com.example.mai2.main_programme.activities.result_activity.calculating_thread;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.math.Buffer;

import java.util.ArrayList;


@SuppressWarnings("deprecation")
@SuppressLint("HandlerLeak")
public class CalculatingHandler extends Handler {
    private final ArrayList<TextView> valueTextArray;
    private final String[] criteria, candidates;

    public CalculatingHandler(ArrayList<TextView> valueTextArray,
                              String[] criteria,
                              String[] candidates){
        this.valueTextArray = valueTextArray;
        this.criteria = criteria;
        this.candidates = candidates;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        Buffer buffer = (Buffer) msg.obj;

        int i = 0;
        for (; i < criteria.length; ++i) {
            for (int j = 0; j < candidates.length; ++j){
                TextView valueText
                        = valueTextArray.get(i * candidates.length + j);

                double value = buffer.eachRelativeWeights.get(i)[j];
                String text = Double.toString(value);

                valueText.setText(text);
            }
        }

        for (int j = 0; j < candidates.length; ++j){
            TextView valueText
                    = valueTextArray.get(i * candidates.length + j);

            double value = buffer.finalRelativeWeights[j];
            String text = Double.toString(value);

            valueText.setText(text);
        }
    }
}
