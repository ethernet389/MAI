package com.example.mai2.main_programme.algorithm.matrix;

//Exception-класс для парсинга матрицы (сохраняет место ошибки)
public class ParseMatrixException extends Exception {
    private final int row, column;

    public ParseMatrixException(String message, int row, int column){
        super(message);
        this.row = row;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
