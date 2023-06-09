package com.example.mai2.main_programme.activities.create_new_mai_note.main_activity.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mai2.R;
import com.example.mai2.main_programme.algorithm.matrix.Algorithm;
import com.example.mai2.main_programme.algorithm.matrix.ParseMatrixException;
import com.google.gson.Gson;


public class MatrixFragment extends Fragment {

    private static final String HEADERS = "headers";
    private static final String TITLE = "title";

    private String[] headers;
    private String title;

    private ConstraintLayout layout;
    private TableLayout matrix;

    public TableLayout getMatrix(){
        return matrix;
    }

    public MatrixFragment() {}

    public MatrixFragment(String[] headers, String title){
        this.headers = headers;
        this.title = title;
    }

    public static MatrixFragment newInstance(String[] headers, String title) {
        MatrixFragment fragment = new MatrixFragment();
        Bundle args = new Bundle();
        args.putStringArray(HEADERS, headers);
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            headers = getArguments().getStringArray(HEADERS);
            title = getArguments().getString(TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout =
                (ConstraintLayout) inflater.inflate(R.layout.fragment_matrix, container, false);
        TextView titleText = layout.findViewById(R.id.matrix_title);
        titleText.setText(title);

        GeneratorThread thread = new GeneratorThread(inflater);
        thread.start();

        return layout;
    }

    private class GeneratorHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            FrameLayout container = layout.findViewById(R.id.matrix_container);
            matrix = (TableLayout) msg.obj;
            container.addView(matrix);
        }
    }

    private class GeneratorThread extends Thread {
        private final LayoutInflater inflater;
        private final GeneratorHandler handler = new GeneratorHandler();

        GeneratorThread(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public void run() {
            TableLayout matrix = Algorithm.generateSquareMatrixTableLayout(
                    getContext(), inflater, headers, 3
            );
            Message msg = new Message();
            msg.obj = matrix;
            handler.sendMessage(msg);
        }
    }
}