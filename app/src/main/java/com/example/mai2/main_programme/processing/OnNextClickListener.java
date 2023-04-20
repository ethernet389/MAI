package com.example.mai2.main_programme.processing;

import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OnNextClickListener implements View.OnClickListener {
    private TableLayout layout;
    private BufferedWriter bw;

    private boolean matrixOfCriteriaWas;
    private int matrixOfCandidatesCount;

    private  final String[] candidates;

    public OnNextClickListener(TableLayout layout, BufferedWriter bw, String[] candidates){
        this.layout = layout;
        this.bw = bw;
        this.matrixOfCriteriaWas = false;
        this.matrixOfCandidatesCount = 0;

        this.candidates = candidates.clone();
    }

    @Override
    public void onClick(View v) {
        try {
            //Обработка матрицы
            if (!matrixOfCriteriaWas){
                bw.write(layout.getChildCount() + " ");
                matrixOfCriteriaWas = true;
            }
            else if (matrixOfCandidatesCount == 0){
                bw.write(layout.getChildCount() + " ");
            }

            String stringMatrix = MatrixStringParser.parseString(layout);
            bw.write(stringMatrix);

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
