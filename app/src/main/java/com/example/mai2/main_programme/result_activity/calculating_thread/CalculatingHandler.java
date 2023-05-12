package com.example.mai2.main_programme.result_activity.calculating_thread;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.math.Buffer;
import com.example.mai2.main_programme.result_activity.generate_layout_thread.LayoutGeneratorHandler;
import com.example.mai2.main_programme.result_activity.generate_layout_thread.LayoutGeneratorThread;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;


@SuppressWarnings("deprecation")
@SuppressLint("HandlerLeak")
public class CalculatingHandler extends Handler {
    private final LinearLayout layout;
    private final ArrayList<TextView> valueTextArray;

    public CalculatingHandler(LinearLayout layout, ArrayList<TextView> valueTextArray){
        this.layout = layout;
        this.valueTextArray = valueTextArray;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        Buffer buffer = (Buffer) msg.obj;

        int i = 0;
        for (; i < Constants.CRITERIA.length; ++i) {
            for (int j = 0; j < Constants.CANDIDATES.length; ++j){
                TextView valueText
                        = valueTextArray.get(i * Constants.CANDIDATES.length + j);

                double value = buffer.eachRelativeWeights.get(i)[j];
                String text = Double.toString(value);

                valueText.setText(text);
            }
        }

        for (int j = 0; j < Constants.CANDIDATES.length; ++j){
            TextView valueText
                    = valueTextArray.get(i * Constants.CANDIDATES.length + j);

            double value = buffer.finalRelativeWeights[j];
            String text = Double.toString(value);

            valueText.setText(text);
        }
    }
}
