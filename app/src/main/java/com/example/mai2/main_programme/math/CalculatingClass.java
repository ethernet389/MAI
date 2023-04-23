package com.example.mai2.main_programme.math;

import Jama.Matrix;

import java.util.ArrayList;
import java.util.Scanner;

public final class CalculatingClass {
    //Решение задачи для одного человека (один уровень критериев)
    static public Buffer completeTask(Scanner data){
        //Буфер для хранения данных
        Buffer buffer = new Buffer();

        //Кол-во личных критериев
        int numPar = data.nextInt();

        //Матрица отношений личных критериев
        Matrix relPar = MatrixFunctions.inputMatrix(data, numPar, numPar);
        MatrixFunctions.normalize(relPar);

        //Кол-во альтернатив
        int numAlt = data.nextInt();

        //Относительная оценка альтернатив по каждому из критериев
        ArrayList<Matrix> listOfRelAlts = new ArrayList<>(numAlt);
        for (int i = 0; i < numPar; ++i){
            Matrix matrix = MatrixFunctions.inputMatrix(data, numAlt, numAlt);
            MatrixFunctions.normalize(matrix);
            listOfRelAlts.add(matrix);
        }

        //Относительные веса между альтернативами по каждому критерию
        ArrayList<double[]> listOfWeights = new ArrayList<>();
        for (Matrix listOfRelAlt : listOfRelAlts) {
            listOfWeights.add(
                    MatrixFunctions.getRelativeWeights(listOfRelAlt)
            );
        }

        //Обход дерева иерархий
        //Домножение каждого веса кандидата на вес критерия
        double[] perWeights = MatrixFunctions.getRelativeWeights(relPar);
        for (int i = 0; i < perWeights.length; ++i) {
            for (int j = 0; j < numAlt; ++j) {
                listOfWeights.get(i)[j] *= perWeights[i];
            }
        }
        buffer.relativeWeightsOfEachCandidateForEachOfCriteria = listOfWeights;

        //Суммирование каждого веса критериев для рейтинга кандидатов
        double[] result = new double[numAlt];
        for (int i = 0; i < perWeights.length; ++i) {
            for (int j = 0; j < numAlt; ++j) {
                result[j] += listOfWeights.get(i)[j];
            }
        }
        buffer.finalRatingEachOfCandidate = result;

        return buffer;
    }
}
