package com.example.mai2.main_programme.activities.result_activity.generate_layout_thread;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mai2.main_programme.Constants;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;

@SuppressWarnings("deprecation")
@SuppressLint("HandlerLeak")
public class LayoutGeneratorHandler extends Handler {
    private final LinearLayout layout;

    public LayoutGeneratorHandler(LinearLayout layout){
        this.layout = layout;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        layout.addView((View) msg.obj);
    }
}