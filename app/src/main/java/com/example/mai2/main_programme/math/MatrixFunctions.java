package com.example.mai2.main_programme.math;

import Jama.Matrix;

import java.util.Scanner;

public final class MatrixFunctions {

    //Сумма каждого столбца матрицы
    static public double[] getSumOfColumns(Matrix matrix){
        double[] columnSum = new double[matrix.getColumnDimension()];

        for (int j = 0; j != matrix.getColumnDimension(); ++j){
            double sum = 0;
            for (int i = 0; i != matrix.getRowDimension(); ++i){
                sum += matrix.get(i, j);
            }
            columnSum[j] = sum;
        }

        return columnSum;
    }

    //Относительные веса матрицы
    static public double[] getRelativeWeights(Matrix matrix){
        double[] ret = getSumOfRows(matrix);
        for (int i = 0; i < ret.length; ++i) ret[i] /= matrix.getColumnDimension();
        return ret;
    }

    //Сумма каждой строки матрицы
    static public double[] getSumOfRows(Matrix matrix){
        double[] linesSum = new double[matrix.getRowDimension()];

        for (int j = 0; j != matrix.getRowDimension(); ++j){
            double sum = 0;
            for (int i = 0; i != matrix.getColumnDimension(); ++i){
                sum += matrix.get(j , i);
            }
            linesSum[j] = sum;
        }

        return linesSum;
    }

    //Нормализация матрицы
    static public Matrix normalize(Matrix matrix){
        double[] columnSum = getSumOfColumns(matrix);

        for (int i = 0; i != matrix.getRowDimension(); ++i){
            for (int j = 0; j != matrix.getColumnDimension(); ++j){
                double newValue = matrix.get(j, i) / columnSum[i];
                matrix.set(j, i, newValue);
            }
        }
        return matrix;
    }

    //Нормализация копии матрицы
    static public Matrix getNormalized(Matrix matrix){
        return MatrixFunctions.normalize(matrix.copy());
    }

    //Преобразование матрицы в строку
    static public String toString(Matrix matrix) {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < matrix.getRowDimension(); ++i){
            buffer.append("[");
            int j = 0;
            for (; j < matrix.getColumnDimension() - 1; ++j) {
                buffer.append(matrix.get(i, j)).append(", ");
            }
            buffer.append(matrix.get(i, j)).append("]\n");
        }

        return buffer.toString();
    }

    //Коэффициент согласованности матрицы
    static public double getCI(Matrix critMatrix){
        double[] normalizedCritMatrixWeights = getRelativeWeights(getNormalized(critMatrix));
        Matrix weights = new Matrix(normalizedCritMatrixWeights.length, 1);
        for (int i = 0; i < weights.getRowDimension(); ++i){
            weights.set(i, 0, normalizedCritMatrixWeights[i]);
        }

        Matrix resultMatrix = critMatrix.times(weights);

        return (getSumOfColumns(resultMatrix)[0] - critMatrix.getColumnDimension())
                / (critMatrix.getColumnDimension() - 1);
    }

    //Стохастический коэффициент согласоввнности матрицы
    static public double getRI(Matrix critMatrix){
        return (1.98 * (critMatrix.getColumnDimension() - 2)) / critMatrix.getColumnDimension();
    }

    //Коэффициент согласованности матрицы
    static public double getCR(Matrix critMatrix){
        return getCI(critMatrix) / getRI(critMatrix);
    }

    //Ввод матрицы через Scanner
    static public Matrix inputMatrix(Scanner in, int rows, int columns){
        double[][] m = new double[rows][columns];
        for (int i = 0; i != rows; ++i){
            for (int j = 0; j != columns; ++j){
                double inp = in.nextDouble();
                m[i][j] = inp;
            }
        }
        return new Matrix(m);
    }


}
