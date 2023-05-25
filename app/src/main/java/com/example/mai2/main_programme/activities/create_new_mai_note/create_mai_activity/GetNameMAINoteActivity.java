package com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity;

import static com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.ChooseMAIConfigActivity.NAME_OF_CONFIG_KEY;
import static com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.CreateMAIActivity.CANDIDATES_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.workers.QueryNameWorker;
import com.example.mai2.main_programme.activities.create_new_mai_note.main_activity.MainActivity;

public class GetNameMAINoteActivity extends AppCompatActivity {

    TextView title;
    Button next;
    EditText inputName;

    static public String NAME_KEY = "nameKey";

    class NextOnClickListener implements View.OnClickListener{

        private void showShortToastWithText(String text){
            Toast.makeText(
                    getApplicationContext(),
                    text,
                    Toast.LENGTH_SHORT
            ).show();
        }

        @Override
        public void onClick(View v) {
            String name = inputName.getText().toString();
            if (name.isEmpty()) {
                showShortToastWithText("Название не введено!");
                return;
            }

            Data data = new Data.Builder()
                    .putString("name", name)
                    .build();
            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(QueryNameWorker.class)
                    .setInputData(data)
                    .build();
            WorkManager.getInstance(getApplicationContext()).enqueue(request);

            Observer<WorkInfo> observer = new Observer<WorkInfo>() {
                @Override
                public void onChanged(WorkInfo workInfo) {
                    switch (workInfo.getState()){
                        case SUCCEEDED:
                            Intent intent = new Intent(getApplicationContext(),
                                    MainActivity.class);
                            intent.putExtra(NAME_OF_CONFIG_KEY,
                                    getIntent().getStringExtra(NAME_OF_CONFIG_KEY));
                            intent.putExtra(CANDIDATES_KEY,
                                    getIntent().getStringArrayExtra(CANDIDATES_KEY));
                            intent.putExtra(NAME_KEY, name);
                            startActivity(intent);
                            finish();
                            break;
                        case FAILED:
                            showShortToastWithText("Запись с таким названием уже есть!");
                            break;
                    }
                }
            };
            WorkManager.getInstance(getApplicationContext())
                    .getWorkInfoByIdLiveData(request.getId())
                    .observe(GetNameMAINoteActivity.this, observer);
        }
    }

    private void initialize(){
        title = findViewById(R.id.title_of_set_name_mai_template);
        inputName = findViewById(R.id.input_mai_template_name);
        next = findViewById(R.id.go_next_with_name_button);

        title.setText(getString(R.string.title_of_get_name_mai_note));
        inputName.setHint(getString(R.string.input_mai_note_name));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name_for_mai_template);
        initialize();

        next.setOnClickListener(new NextOnClickListener());
    }
}