package com.example.mai2.main_programme.activities.create_mai_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.mai2.R;
import com.example.mai2.main_programme.db.database.AppDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChooseMAIConfigActivity extends AppCompatActivity {

    LinearLayout container;

    public final static String NAME_OF_CONFIG_KEY = "nameOfConfigKey";

    private void initialize(){
        container = findViewById(R.id.mai_config_container);
    }

    class ListGeneratorThread extends Thread{
        ListHandler handler = new ListHandler();

        @Override
        public void run() {
            AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
            List<String> names = db.getMAIConfigDao().getAllNamesOfMAIConfigs();
            LayoutInflater inflater = getLayoutInflater();
            for (String name : names){
                LinearLayout view =
                        (LinearLayout) inflater.inflate(R.layout.one_choose_element, null);
                Button button = view.findViewById(R.id.config_button);
                button.setText(name);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),
                                CreateMAIActivity.class);
                        intent.putExtra(NAME_OF_CONFIG_KEY, name);
                        startActivity(intent);
                    }
                });

                ImageView deleteConfig = view.findViewById(R.id.delete_config);
                deleteConfig.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        container.removeView(view);
                        Runnable deleteConfigQuery = () ->
                                db.getMAIConfigDao().deleteMAIConfigByName(name);
                        new Thread(deleteConfigQuery).start();
                    }
                });

                Message msg = new Message();
                msg.obj = view;
                handler.sendMessage(msg);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    class ListHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            container.addView((View) msg.obj);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mai_config);
        initialize();
        ListGeneratorThread thread = new ListGeneratorThread();
        thread.start();
    }
}