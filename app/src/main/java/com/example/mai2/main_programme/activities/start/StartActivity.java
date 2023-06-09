package com.example.mai2.main_programme.activities.start;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.check_notes.CheckMAIActivity;
import com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.ChooseMAIConfigActivity;
import com.example.mai2.main_programme.activities.create_mai_template.SetNameForMAITemplateActivity;

public class StartActivity extends AppCompatActivity {

    TextView title;
    LinearLayout createArea;
    LinearLayout viewArea;

    Button createMAITemplateButton;
    Button createMAIButton;
    Button checkMAIButton;

    private void initialize(){
        createMAITemplateButton = findViewById(R.id.create_template_button);
        createMAIButton = findViewById(R.id.start_mai_button);
        checkMAIButton = findViewById(R.id.check_mai_button);

        title = findViewById(R.id.start_title_text);
        createArea = findViewById(R.id.create_area);
        viewArea = findViewById(R.id.view_area);
        changeShapeColor(title, R.color.design_brown);
        changeShapeColor(createArea, R.color.design_green);
        changeShapeColor(viewArea, R.color.design_blue);
    }

    private void changeShapeColor(View view, int colorId){
        GradientDrawable back = (GradientDrawable) view.getBackground();
        int color = getColor(colorId);
        back.mutate();
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
        createMAIButton
                .setOnClickListener(
                        new CreateIntentOnClick(ChooseMAIConfigActivity.class)
                );

        createMAITemplateButton
                .setOnClickListener(
                        new CreateIntentOnClick(SetNameForMAITemplateActivity.class)
                );

        checkMAIButton
                .setOnClickListener(
                        new CreateIntentOnClick(CheckMAIActivity.class)
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initialize();

        setOnClickListeners();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}