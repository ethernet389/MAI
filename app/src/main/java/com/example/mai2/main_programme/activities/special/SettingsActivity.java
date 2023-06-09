package com.example.mai2.main_programme.activities.special;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.start.StartActivity;
import com.example.mai2.main_programme.db.database.AppDatabase;

public class SettingsActivity extends AppCompatActivity {
    LinearLayout dbArea;
    Button deleteDdButton;

    private void initialize(){
        deleteDdButton = findViewById(R.id.delete_db_button);
        dbArea = findViewById(R.id.db_area);

        GradientDrawable back = (GradientDrawable) dbArea.getBackground().mutate();
        back.setColor(getColor(R.color.design_gray));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initialize();

        deleteDdButton.setOnClickListener(delete -> {
            new Thread(() -> {
                AppDatabase.getAppDatabase(this).clearAllTables();
            }).start();
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}