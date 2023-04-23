package com.example.mai2.main_programme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mai2.R;
import com.example.mai2.main_programme.main_activity.MainActivity;
import com.example.mai2.main_programme.math.Buffer;
import com.example.mai2.main_programme.math.CalculatingClass;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class ResultActivity extends AppCompatActivity {
    TextView relativeWeightsOfEachCandidateForEachOfCriteria;
    TextView CI_RI_CR;

    private void initialize(){
        relativeWeightsOfEachCandidateForEachOfCriteria =
                findViewById(R.id.relative_weights_of_each_candidate_for_each_of_criteria);
        CI_RI_CR = findViewById(R.id.ci_ri_cr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initialize();

        try {
            Scanner data = new Scanner(
                    new InputStreamReader(
                            openFileInput(MainActivity.ANSWER_FILENAME)
                    )
            );
            //Так как double записан через точку, а не через запятую
            data.useLocale(Locale.CANADA);

            Buffer buffer = CalculatingClass.calculate(data);
            relativeWeightsOfEachCandidateForEachOfCriteria
                    .setText("Relative Weights Of Each Candidate For Each Of Criteria: \n");
            for (double[] list : buffer.relativeWeightsOfEachCandidateForEachOfCriteria){
                relativeWeightsOfEachCandidateForEachOfCriteria.append(Arrays.toString(list));
                relativeWeightsOfEachCandidateForEachOfCriteria.append("\n\n");
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}