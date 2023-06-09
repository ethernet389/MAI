package com.example.mai2.main_programme.algorithm.matrix.click_listeners;

import static com.example.mai2.main_programme.Constants.millisOfHeaderToastDuration;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.example.mai2.main_programme.Constants;

public class HeaderClickListener implements View.OnClickListener{
    private final String headerFullName;
    private final Context context;

    public HeaderClickListener(Context context, String headerFullName){
        this.headerFullName = headerFullName;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        //Показать пользователю полное название заголовка (0.7 сек)
        Toast toast = Toast.makeText(context, headerFullName, Toast.LENGTH_SHORT);
        new CountDownTimer(Constants.millisOfHeaderToastDuration,
                millisOfHeaderToastDuration){

            @Override
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            @Override
            public void onFinish() {
                toast.cancel();
            }
        }.start();
    }
}
