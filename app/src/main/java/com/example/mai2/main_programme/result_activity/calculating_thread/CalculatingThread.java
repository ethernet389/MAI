package com.example.mai2.main_programme.result_activity.calculating_thread;

import android.content.Context;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.math.Buffer;
import com.example.mai2.main_programme.math.CalculatingClass;
import com.example.mai2.main_programme.result_activity.generate_layout_thread.LayoutGeneratorThread;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;

public class CalculatingThread extends Thread{
    private final CalculatingHandler handler;
    private final Context context;
    private final CountDownLatch count;

    public CalculatingThread(Context context,
                             LinearLayout layout,
                             ArrayList<TextView> valueTextArray,
                             CountDownLatch count){
        this.handler = new CalculatingHandler(layout, valueTextArray);
        this.context = context;
        this.count = count;
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

            //Остановка потока до тех пор, пока разметка не сгенерируется до конца
            count.await();

            handler.sendMessage(msg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
