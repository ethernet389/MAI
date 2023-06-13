package com.example.mai2.main_programme.activities.special;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.start.StartActivity;


public class InfoActivity extends AppCompatActivity {

    LinearLayout textContainer;
    LinearLayout literatureContainer;

    private void initialize(){
        textContainer = findViewById(R.id.text_container);
        literatureContainer = findViewById(R.id.literature_container);

        int gray = getColor(R.color.design_gray);
        int littleGray = getColor(R.color.design_little_dark_gray);
        for (int i = 0; i < textContainer.getChildCount(); ++i){
            View view = textContainer.getChildAt(i);
            GradientDrawable back = (GradientDrawable) view.getBackground().mutate();
            back.setColor(i % 2 == 0 ? littleGray : gray);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initialize();

        String[] literature = getResources().getStringArray(R.array.literature_text);
        String[] hyperlinks = getResources().getStringArray(R.array.hyperlink_literature);
        for (int i = 0; i < literature.length; ++i) {
            TextView text =
                    (TextView) getLayoutInflater().inflate(R.layout.literature_text_view, null);
            text.setPaintFlags(text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            text.setText(literature[i]);
            Uri uri = Uri.parse(hyperlinks[i]);
            text.setOnClickListener(link -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            });
            literatureContainer.addView(text);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}