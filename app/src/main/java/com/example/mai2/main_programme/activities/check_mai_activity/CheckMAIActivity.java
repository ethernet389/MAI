package com.example.mai2.main_programme.activities.check_mai_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mai2.R;
import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.activities.result_activity.ResultActivity;
import com.example.mai2.main_programme.db.database.AppDatabase;

import java.util.List;

public class CheckMAIActivity extends AppCompatActivity {

    LinearLayout container;

    class CreateElementsThread extends Thread{
        CreateElementsHandler handler = new CreateElementsHandler();

        private LinearLayout createOneElement(String name){
            LinearLayout layout
                    = (LinearLayout) getLayoutInflater().inflate(R.layout.one_choose_element, null);
            Button button = layout.findViewById(R.id.logic_button);
            ImageView deleteButton = layout.findViewById(R.id.delete_one_element);

            deleteButton.setOnClickListener(delete -> {
                container.removeView(layout);
                new Thread(){
                    @Override
                    public void run() {
                        AppDatabase.getAppDatabase(getApplicationContext())
                                .getMAINoteDao()
                                .deleteMAINoteByName(name);
                    }
                }.start();
            });

            button.setOnClickListener(createIntent -> {
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra(Constants.NOTE_KEY, name);
                startActivity(intent);
            });
            button.setText(name);

            return layout;
        };

        @Override
        public void run() {
            AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
            List<String> names = db.getMAINoteDao().getAllNameOfNotes();
            for (String name : names) {
                LinearLayout layout = createOneElement(name);
                Message msg = new Message();
                msg.obj = layout;
                handler.sendMessage(msg);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    class CreateElementsHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            container.addView((View) msg.obj);
        }
    }

    private void initialize(){
        container = findViewById(R.id.mai_records_container);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mai_activity);
        initialize();

        CreateElementsThread thread = new CreateElementsThread();
        thread.start();
    }
}