package com.example.mai2.main_programme.activities.result_activity.calculating_thread;

import android.content.Context;
import android.os.Message;
import android.widget.TextView;

import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.math.Buffer;
import com.example.mai2.main_programme.math.CalculatingClass;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class CalculatingThread extends Thread{
    private final CalculatingHandler handler;
    private final String formattedAnswer;
    private final CountDownLatch count;

    public CalculatingThread(String formattedAnswer,
                             ArrayList<TextView> valueTextArray,
                             String[] criteria,
                             String[] candidates,
                             CountDownLatch count){
        this.handler = new CalculatingHandler(valueTextArray, criteria, candidates);
        this.formattedAnswer = formattedAnswer;
        this.count = count;
    }

    @Override
    public void run() {
        try {
            Scanner data = new Scanner(formattedAnswer);
            data.useLocale(Locale.CANADA);

            Buffer buffer = CalculatingClass.calculate(data);
            data.close();
            Message msg = new Message();
            msg.obj = buffer;

            //Остановка потока до тех пор, пока разметка не сгенерируется до конца
            count.await();

            handler.sendMessage(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
