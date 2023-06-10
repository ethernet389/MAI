package com.example.mai2.main_programme.activities.start;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.check_notes.CheckMAIActivity;
import com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.ChooseMAIConfigActivity;
import com.example.mai2.main_programme.activities.create_mai_template.SetNameForMAITemplateActivity;
import com.example.mai2.main_programme.activities.special.InfoActivity;
import com.example.mai2.main_programme.activities.special.SettingsActivity;
import com.example.mai2.main_programme.db.database.AppDatabase;

public class StartActivity extends AppCompatActivity {

    TextView title;
    LinearLayout createArea;
    LinearLayout viewArea;
    ConstraintLayout specialArea;

    Button createMAITemplateButton;
    Button createMAIButton;
    Button checkMAIButton;

    ImageView exitButton;
    ImageView settingsButton;
    ImageView infoButton;

    private void initialize(){
        createMAITemplateButton = findViewById(R.id.create_template_button);
        createMAIButton = findViewById(R.id.start_mai_button);
        checkMAIButton = findViewById(R.id.check_mai_button);
        exitButton = findViewById(R.id.exit_button);
        settingsButton = findViewById(R.id.settings_image);
        infoButton = findViewById(R.id.info_image);

        title = findViewById(R.id.start_title_text);
        createArea = findViewById(R.id.create_area);
        viewArea = findViewById(R.id.view_area);
        specialArea = findViewById(R.id.special_area);
        changeShapeColor(title, R.color.design_brown);
        changeShapeColor(createArea, R.color.design_green);
        changeShapeColor(viewArea, R.color.design_blue);
        changeShapeColor(specialArea, R.color.design_gray);
    }

    private void changeShapeColor(View view, int colorId){
        GradientDrawable back = (GradientDrawable) view.getBackground().mutate();
        int color = getColor(colorId);
        back.setColor(color);
    }

    @SuppressWarnings("rawtypes")
    class CreateIntentOnClick implements View.OnClickListener {
        Class activity;

        public CreateIntentOnClick(Class activity){
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StartActivity.this, activity);
            startActivity(intent);
            finish();
        }
    }

    private void setOnClickListeners(){
        createMAIButton.setOnClickListener(
                new CreateIntentOnClick(ChooseMAIConfigActivity.class)
        );
        createMAITemplateButton.setOnClickListener(
                new CreateIntentOnClick(SetNameForMAITemplateActivity.class)
        );
        checkMAIButton.setOnClickListener(
                new CreateIntentOnClick(CheckMAIActivity.class)
        );
        settingsButton.setOnClickListener(
                new CreateIntentOnClick(SettingsActivity.class)
        );
        infoButton.setOnClickListener(
                new CreateIntentOnClick(InfoActivity.class)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initialize();

        setOnClickListeners();
        exitButton.setOnClickListener(exit -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {}
}