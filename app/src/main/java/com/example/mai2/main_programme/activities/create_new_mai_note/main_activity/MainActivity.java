package com.example.mai2.main_programme.activities.create_new_mai_note.main_activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.Toast;


import com.example.mai2.R;
import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.ChooseMAIConfigActivity;
import com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.CreateMAIActivity;
import com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.GetNameMAINoteActivity;
import com.example.mai2.main_programme.activities.create_new_mai_note.main_activity.fragments.MatrixFragment;
import com.example.mai2.main_programme.activities.create_new_mai_note.main_activity.view_pagers.MatrixFragmentStateAdapter;
import com.example.mai2.main_programme.activities.create_new_mai_note.main_activity.workers.GetCriteriaWorker;
import com.example.mai2.main_programme.activities.create_new_mai_note.main_activity.workers.InsertNewMAINoteWorker;
import com.example.mai2.main_programme.activities.show_note.ResultActivity;
import com.example.mai2.main_programme.algorithm.matrix.Algorithm;
import com.example.mai2.main_programme.algorithm.matrix.ParseMatrixException;


import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ViewPager2 fragmentPager;
    Button nextButton;
    String[] candidates;
    String[] criteria;
    String name;
    String nameOfConfig;

    private void initialize(){
        fragmentPager = findViewById(R.id.fragment_container);
        nextButton = findViewById(R.id.next_button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        Intent pastIntent = getIntent();
        candidates = pastIntent.getStringArrayExtra(CreateMAIActivity.CANDIDATES_KEY);
        name = pastIntent.getStringExtra(GetNameMAINoteActivity.NAME_KEY);
        nameOfConfig = pastIntent.getStringExtra(ChooseMAIConfigActivity.NAME_OF_CONFIG_KEY);

        Data data = new Data.Builder().putString("nameOfConfig", nameOfConfig).build();
        OneTimeWorkRequest request = new OneTimeWorkRequest
                .Builder(GetCriteriaWorker.class)
                .setInputData(data)
                .build();

        Observer<WorkInfo> observer = workInfo -> {
            if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)){
                criteria = workInfo.getOutputData().getStringArray("criteria");
                MatrixFragmentStateAdapter adapter = new MatrixFragmentStateAdapter(
                        getSupportFragmentManager(), getLifecycle(),
                        getApplicationContext(), criteria, candidates
                );
                fragmentPager.setAdapter(adapter);
                fragmentPager.setOffscreenPageLimit(criteria.length + 1);
                nextButton.setOnClickListener(this);
            }
        };

        WorkManager.getInstance(getApplicationContext()).enqueue(request);
        WorkManager.getInstance(getApplicationContext())
                .getWorkInfoByIdLiveData(request.getId())
                .observe(this, observer);
    }

    private String matrixToString(@Nullable MatrixFragment fragment){
        try{
            TableLayout matrix = fragment.getMatrix();
            return Algorithm.matrixToString(matrix);
        } catch (ParseMatrixException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {/*ignored*/}
        return null;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        StringBuilder formattedAnswer = new StringBuilder();
        formattedAnswer.append(criteria.length).append(" ");

        MatrixFragment criteriaFragment = (MatrixFragment) fm.findFragmentByTag("f0");
        String criteriaMatrixString = matrixToString(criteriaFragment);
        if (criteriaMatrixString == null) return;
        formattedAnswer.append(criteriaMatrixString);

        formattedAnswer.append(candidates.length).append(" ");

        for (int i = 0; i < criteria.length; ++i){
            MatrixFragment fragment = (MatrixFragment) fm.findFragmentByTag("f" + i);
            String candidateMatrixString = matrixToString(fragment);
            if (candidateMatrixString == null) return;
            formattedAnswer.append(candidateMatrixString);
        }

        Data data = new Data.Builder()
                .putString("nameOfConfig", nameOfConfig)
                .putString("name", name)
                .putStringArray("candidates", candidates)
                .putString("formattedAnswer", formattedAnswer.toString())
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest
                .Builder(InsertNewMAINoteWorker.class)
                .setInputData(data)
                .build();
        Observer<WorkInfo> observer = workInfo -> {
            if (!workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) return;
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(Constants.NOTE_KEY, name);
            startActivity(intent);
            finish();
        };
        WorkManager.getInstance(this).enqueue(request);
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.getId())
                .observe(this, observer);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ChooseMAIConfigActivity.class);
        startActivity(intent);
        finish();
    }
}