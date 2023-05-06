package com.example.mai2.main_programme.result_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mai2.R;
import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.math.Buffer;
import com.example.mai2.main_programme.math.CalculatingClass;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class ResultActivity extends AppCompatActivity {
    TextView result;
    TextView koeff;

    private void initialize(){
        result =
                findViewById(R.id.result_of_matrix);
        koeff = findViewById(R.id.ci_ri_cr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initialize();

        try {
            Scanner data = new Scanner(
                    new InputStreamReader(
                            openFileInput(Constants.ANSWER_FILENAME)
                    )
            );
            //Так как double записан через точку, а не через запятую
            data.useLocale(Locale.CANADA);

            Buffer buffer = CalculatingClass.calculate(data);
            for (String university : Constants.CANDIDATES) result.append(university + " ");
            result.append("\n");

            for (int i = 0; i < buffer.relativeWeightsOfEachCandidateForEachOfCriteria.size(); ++i){
                result.append(Constants.CRITERIA[i] + "\n");
                result.append(Arrays.toString(
                        buffer.relativeWeightsOfEachCandidateForEachOfCriteria.get(i)
                ));
                result.append("\n\n");
            }

            result.append("Общий рейтинг\n");
            result.append(Arrays.toString(buffer.finalRatingEachOfCandidate));
            result.append("\n\n");

        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}