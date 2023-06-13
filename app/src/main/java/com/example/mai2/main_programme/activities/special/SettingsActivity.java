package com.example.mai2.main_programme.activities.special;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.create_new_mai_note.main_activity.MainActivity;
import com.example.mai2.main_programme.activities.start.StartActivity;
import com.example.mai2.main_programme.db.database.AppDatabase;
import com.yariksoffice.lingver.Lingver;

public class SettingsActivity extends AppCompatActivity {
    LinearLayout dbArea;
    LinearLayout languageArea;
    Button deleteDdButton;
    Button russianButton;
    Button englishButton;

    private void setBackground(ViewGroup layout){
        GradientDrawable back = (GradientDrawable) layout.getBackground().mutate();
        back.setColor(getColor(R.color.design_gray));
    }

    private void initialize(){
        deleteDdButton = findViewById(R.id.delete_db_button);
        russianButton = findViewById(R.id.russian_button);
        englishButton = findViewById(R.id.english_button);
        dbArea = findViewById(R.id.db_area);
        languageArea = findViewById(R.id.language_area);

        setBackground(dbArea);
        setBackground(languageArea);
    }

    class OnClickDeleteListener implements View.OnClickListener {
        private int counter = 0;
        private final int LIMIT_COUNTER = 5;
        private final int MILLIS = 500;

        private void showToast(Toast toast) {
            toast.show();
            new CountDownTimer(MILLIS, MILLIS) {
                @Override
                public void onTick(long millisUntilFinished) {/*ignored*/}

                @Override
                public void onFinish() {
                    toast.cancel();
                }
            }.start();
        }

        @Override
        public void onClick(View v) {
            Toast toast;
            if (counter < LIMIT_COUNTER) {
                ++counter;
                toast = Toast.makeText(getApplicationContext(),
                        R.string.database_is_deleting_message,
                        Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(getApplicationContext(),
                        R.string.database_is_cleared_message,
                        Toast.LENGTH_SHORT);
                new Thread(() -> {
                    AppDatabase.getAppDatabase(getApplicationContext()).clearAllTables();
                }).start();
                counter = 0;
            }
            showToast(toast);
        }
    }

    class ChangeLanguageOnClickListener implements View.OnClickListener {
        private final String language;
        public ChangeLanguageOnClickListener(String lang){
            this.language = lang;
        }

        @Override
        public void onClick(View v) {
            Lingver.getInstance().setLocale(SettingsActivity.this, language);

            Intent intent = new Intent(SettingsActivity.this, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initialize();

        deleteDdButton.setOnClickListener(new OnClickDeleteListener());
        russianButton.setOnClickListener(new ChangeLanguageOnClickListener("ru"));
        englishButton.setOnClickListener(new ChangeLanguageOnClickListener("en"));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}