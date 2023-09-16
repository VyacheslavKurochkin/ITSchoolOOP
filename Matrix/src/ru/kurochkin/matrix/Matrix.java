package ru.kurochkin.matrix;

import ru.kurochkin.vector.*;

public class Matrix {
    private static final double EPSILON = 1.0e-10;
    private Vector[] rows;

    public Matrix(int rowsCount, int columnsCount) {
        if (rowsCount <= 0 || columnsCount <= 0) {
            throw new IllegalArgumentException("Нельзя создавать матрицу размерности " + rowsCount + "*" + columnsCount +
                    ". Количество строк и столбцов должны быть больше 0");
        }

        rows = new Vector[rowsCount];

        for (int i = 0; i < rowsCount; i++) {
            rows[i] = new Vector(columnsCount);
        }
    }

    public Matrix(Matrix matrix) {
        rows = new Vector[matrix.rows.length];

        for (int i = 0; i < matrix.rows.length; i++) {
            rows[i] = new Vector(matrix.rows[i]);
        }
    }

    public Matrix(double[][] matrix) {
        if (matrix.length == 0) {
            throw new IllegalArgumentException("Нельзя создавать матрицу размерности 0");
        }

        rows = new Vector[matrix.length];
        int maxColumnsCount = 0;

        for (double[] row : matrix) {
            if (maxColumnsCount < row.length) {
                maxColumnsCount = row.length;
            }
        }

        if (maxColumnsCount == 0) {
            throw new IllegalArgumentException("Для матрицы не заданы значения столбцов");
        }

        for (int i = 0; i < matrix.length; i++) {
            rows[i] = new Vector(maxColumnsCount, matrix[i]);
        }
    }

    public Matrix(Vector[] rows) {
        if (rows.length == 0) {
            throw new IllegalArgumentException("Нельзя создавать матрицу размерности 0");
        }

        int maxDimension = 0;

        for (Vector row : rows) {
            if (maxDimension < row.getDimension()) {
                maxDimension = row.getDimension();
            }
        }

        this.rows = new Vector[rows.length];

        Vector zeroRow = new Vector(maxDimension);

        for (int i = 0; i < this.rows.length; i++) {
            if (rows[i].getDimension() != maxDimension) {
                this.rows[i] = Vector.getSum(zeroRow, rows[i]);
            } else {
                this.rows[i] = new Vector(rows[i]);
            }
        }
    }

    public int getRowsCount() {
        return rows.length;
    }

    public int getColumnsCount() {
        return rows[0].getDimension();
    }

    public void setRow(int rowIndex, Vector row) {
        if (rowIndex < 0 || rowIndex >= rows.length) {
            throw new IndexOutOfBoundsException("Индекс " + rowIndex + " за пределами диапазона допустимых значений " +
                    "[0.." + (rows.length - 1) + "]");
        }

        if (row.getDimension() != getColumnsCount()) {
            throw new IllegalArgumentException("Размерность добавляемой строки " + row.getDimension() +
                    " отличается от размерности строки матрицы " + getColumnsCount());
        }

        rows[rowIndex] = new Vector(row);
    }

    public Vector getRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= rows.length) {
            throw new IndexOutOfBoundsException("Индекс " + rowIndex + " за пределами диапазона допустимых значений " +
                    "[0.." + (rows.length - 1) + "]");
        }

        return new Vector(rows[rowIndex]);
    }

    public Vector getColumn(int columnIndex) {
        if (columnIndex < 0 || columnIndex > getColumnsCount() - 1) {
            throw new IndexOutOfBoundsException("Индекс столбца " + columnIndex + " за пределами диапазона допустимых значений " +
                    "[0.." + (getColumnsCount() - 1) + "]");
        }

        Vector column = new Vector(rows.length);

        for (int i = 0; i < rows.length; i++) {
            column.setComponent(i, rows[i].getComponent(columnIndex));
        }

        return column;
    }

    public void transpose() {
        Vector[] newRows = new Vector[getColumnsCount()];

        for (int i = 0; i < newRows.length; i++) {
            newRows[i] = getColumn(i);
        }

        rows = newRows;
    }

    public void multiplyByScalar(double scalar) {
        for (Vector row : rows) {
            row.multiplyByScalar(scalar);
        }
    }

    public double getDeterminant() {
        if (rows.length != getColumnsCount()) {
            throw new UnsupportedOperationException("Размерность матрицы " + rows.length + "*" + getColumnsCount() +
                    " не позволяет вычислить определитель, так как количество строк не равно количеству столбцов");
        }

        if (rows.length == 1) {
            return rows[0].getComponent(0);
        }

        double[][] triangularMatrix = new double[rows.length][getColumnsCount()];

        for (int i = 0; i < triangularMatrix.length; i++) {
            for (int j = 0; j < triangularMatrix.length; j++) {
                triangularMatrix[i][j] = rows[i].getComponent(j);
            }
        }

        double determinant = 1;

        for (int i = 1; i < triangularMatrix.length; i++) {
            if (Math.abs(triangularMatrix[i - 1][i - 1]) < EPSILON) {
                int swapIndex = i;

                for (int j = i + 1; j < triangularMatrix.length; j++) {
                    if (Math.abs(triangularMatrix[j][i]) >= EPSILON) {
                        swapIndex = j;
                        break;
                    }
                }

                if (swapIndex == i) {
                    return 0;
                }

                for (int j = 0; j < triangularMatrix.length; j++) {
                    double temp = triangularMatrix[i][j];
                    triangularMatrix[i][j] = triangularMatrix[swapIndex][j];
                    triangularMatrix[swapIndex][j] = temp;
                }
            }

            determinant *= triangularMatrix[i - 1][i - 1];

            for (int j = i; j < triangularMatrix.length; j++) {
                if (Math.abs(triangularMatrix[j][i - 1]) >= EPSILON) {
                    double rowRate = triangularMatrix[j][i - 1] / triangularMatrix[i - 1][i - 1];

                    for (int k = 0; k < triangularMatrix.length; k++) {
                        triangularMatrix[i - 1][k] *= rowRate;
                        triangularMatrix[j][k] -= triangularMatrix[i - 1][k];
                    }
                }
            }
        }

        determinant *= triangularMatrix[triangularMatrix.length - 1][triangularMatrix.length - 1];

        return determinant;
    }

    public Vector multiplyByVector(Vector vector) {
        if (vector.getDimension() != getColumnsCount()) {
            throw new IllegalArgumentException("Размерность вектора " + vector.getDimension() +
                    " не соответствует размерности матрицы " + rows.length + "*" + getColumnsCount());
        }

        Vector resultVector = new Vector(rows.length);

        for (int i = 0; i < rows.length; i++) {
            resultVector.setComponent(i, Vector.getScalarProduct(rows[i], vector));
        }

        return resultVector;
    }

    private static void checkDimensionsEquality(Matrix matrix1, Matrix matrix2) {
        if (matrix1.rows.length != matrix2.rows.length || matrix1.getColumnsCount() != matrix2.getColumnsCount()) {
            throw new IllegalArgumentException("Размерности матриц " + matrix1.rows.length + "*" + matrix1.getColumnsCount() +
                    " и " + matrix2.rows.length + "*" + matrix2.getColumnsCount() + " не совпадают");
        }
    }

    public void add(Matrix matrix) {
        checkDimensionsEquality(this, matrix);

        for (int i = 0; i < rows.length; i++) {
            rows[i].add(matrix.rows[i]);
        }
    }

    public void subtract(Matrix matrix) {
        checkDimensionsEquality(this, matrix);

        for (int i = 0; i < rows.length; i++) {
            rows[i].subtract(matrix.rows[i]);
        }
    }

    public static Matrix getSum(Matrix matrix1, Matrix matrix2) {
        checkDimensionsEquality(matrix1, matrix2);

        Matrix resultMatrix = new Matrix(matrix1);
        resultMatrix.add(matrix2);

        return resultMatrix;
    }

    public static Matrix getDifference(Matrix matrix1, Matrix matrix2) {
        checkDimensionsEquality(matrix1, matrix2);

        Matrix resultMatrix = new Matrix(matrix1);
        resultMatrix.subtract(matrix2);

        return resultMatrix;
    }

    public static Matrix getProduct(Matrix matrix1, Matrix matrix2) {
        if (matrix1.getColumnsCount() != matrix2.rows.length) {
            throw new IllegalArgumentException("Количество столбцов " + matrix1.getColumnsCount() + " матрицы1" +
                    " не соответствует количеству строк " + matrix2.rows.length + " матрицы2");
        }

        Matrix resultMatrix = new Matrix(matrix1.rows.length, matrix2.getColumnsCount());

        Vector[] columns = new Vector[resultMatrix.getColumnsCount()];

        for (int i = 0; i < matrix2.getColumnsCount(); i++) {
            columns[i] = matrix2.getColumn(i);
        }

        for (int i = 0; i < resultMatrix.rows.length; i++) {
            for (int j = 0; j < resultMatrix.getColumnsCount(); j++) {
                resultMatrix.rows[i].setComponent(j, Vector.getScalarProduct(matrix1.rows[i], columns[j]));
            }
        }

        return resultMatrix;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('{');

        for (Vector row : rows) {
            sb.append(row).append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append('}');

        return sb.toString();
    }
}
