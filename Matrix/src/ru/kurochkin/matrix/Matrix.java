package ru.kurochkin.matrix;

import ru.kurochkin.vector.*;

public class Matrix {
    private Vector[] rows;

    public Matrix(int rowsCount, int columnsCount) {
        if (rowsCount <= 0 || columnsCount <= 0) {
            throw new IllegalArgumentException("Нельзя создавать матрицу размерности " + rowsCount + "*" + columnsCount +
                    ". Количество строк и стольбцов должны быть больше 0");
        }

        rows = new Vector[rowsCount];

        for (int i = 0; i < rowsCount; i++) {
            rows[i] = new Vector(columnsCount);
        }
    }

    public Matrix(Matrix matrix) {
        this.rows = new Vector[matrix.rows.length];

        for (int i = 0; i < matrix.rows.length; i++) {
            this.rows[i] = new Vector(matrix.rows[i]);
        }
    }

    public Matrix(double[][] matrix) {
        if (matrix.length == 0) {
            throw new IllegalArgumentException("Нельзя создавать матрицу размерности 0");
        }

        rows = new Vector[matrix.length];
        int maxDimension = 0;

        for (double[] row : matrix) {
            if (maxDimension < row.length) {
                maxDimension = row.length;
            }
        }

        if (maxDimension == 0) {
            throw new IllegalArgumentException("Для матрицы не заданы значения столбцов");
        }

        for (int i = 0; i < matrix.length; i++) {
            rows[i] = new Vector(maxDimension, matrix[i]);
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

        if (maxDimension == 0) {
            throw new IllegalArgumentException("Для матрицы не заданы значения столбцов");
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

        if (row.getDimension() > rows[0].getDimension()) {
            throw new IllegalArgumentException("Размерность добавляемой строки " + row.getDimension() +
                    " превышает максимально допустимую размерность строки матрицы " + rows[0].getDimension());
        }

        if (row.getDimension() < rows[0].getDimension()) {
            rows[rowIndex] = Vector.getSum(new Vector(rows[0].getDimension()), row);
        } else {
            rows[rowIndex] = new Vector(row);
        }
    }

    public Vector getRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= rows.length) {
            throw new IndexOutOfBoundsException("Индекс " + rowIndex + " за пределами диапазона допустимых значений " +
                    "[0.." + (rows.length - 1) + "]");
        }

        return new Vector(rows[rowIndex]);
    }

    public Vector getColumn(int columnIndex) {
        Vector column = new Vector(rows.length);

        for (int i = 0; i < rows.length; i++) {
            column.setComponent(i, rows[i].getComponent(columnIndex));
        }

        return column;
    }

    public void transpose() {
        Vector[] newRows = new Vector[rows[0].getDimension()];

        for (int i = 0; i < rows[0].getDimension(); i++) {
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
        if (rows.length != rows[0].getDimension()) {
            throw new UnsupportedOperationException("Размерность матрицы " + rows.length + ", " + rows[0].getDimension() +
                    " не позволяет вычислить определитель, так как количество строк не равно количеству столбцов");
        }

        if (rows.length == 1) {
            return rows[0].getComponent(0);
        }

        Vector[] triangularMatrix = new Vector[rows.length];

        for (int i = 0; i < rows.length; i++) {
            triangularMatrix[i] = new Vector(rows[i]);
        }

        double determinant = 1;

        for (int i = 1; i < triangularMatrix.length; i++) {
            if (triangularMatrix[i - 1].getComponent(i - 1) == 0) {
                int swapIndex = i;

                for (int j = i + 1; j < triangularMatrix.length; j++) {
                    if (triangularMatrix[j].getComponent(i) != 0) {
                        swapIndex = j;
                        break;
                    }
                }

                if (swapIndex == i) {
                    return 0;
                }

                Vector temp = triangularMatrix[i];
                triangularMatrix[i] = triangularMatrix[swapIndex];
                triangularMatrix[swapIndex] = temp;
            }

            determinant *= triangularMatrix[i - 1].getComponent(i - 1);

            for (int j = i; j < triangularMatrix.length; j++) {
                if (triangularMatrix[j].getComponent(i - 1) != 0) {
                    Vector temp = new Vector(triangularMatrix[i - 1]);
                    temp.multiplyByScalar(triangularMatrix[j].getComponent(i - 1) / triangularMatrix[i - 1].getComponent(i - 1));
                    triangularMatrix[j].subtract(temp);
                }
            }
        }

        determinant *= triangularMatrix[triangularMatrix.length - 1].getComponent(triangularMatrix.length - 1);

        return determinant;
    }

    public Vector multiplyByVector(Vector input) {
        if (input.getDimension() != rows[0].getDimension()) {
            throw new IllegalArgumentException("Размерность вектора " + input.getDimension() +
                    " не соответствует размерности матрицы " + rows.length + "*" + rows[0].getDimension());
        }

        Vector output = new Vector(rows.length);

        for (int i = 0; i < rows.length; i++) {
            output.setComponent(i, Vector.getScalarProduct(rows[i], input));
        }

        return output;
    }

    private static boolean isEqualsDimensions(Matrix matrix1, Matrix matrix2) {
        return matrix1.rows.length == matrix2.rows.length && matrix1.rows[0].getDimension() == matrix2.rows[0].getDimension();
    }

    private static String getEqualsDimensionsExceptionText(Matrix matrix1, Matrix matrix2) {
        return "РазмерностИ матриц " + matrix1.rows.length + "*" + matrix1.rows[0].getDimension() +
                " и " + matrix2.rows.length + "*" + matrix2.rows[0].getDimension() + " не совпадают по количествоу строк либо столбцов";
    }

    public void add(Matrix matrix) {
        if (!isEqualsDimensions(this, matrix)) {
            throw new IllegalArgumentException(getEqualsDimensionsExceptionText(this, matrix));
        }

        for (int i = 0; i < this.rows.length; i++) {
            this.rows[i].add(matrix.rows[i]);
        }
    }

    public void subtract(Matrix matrix) {
        if (!isEqualsDimensions(this, matrix)) {
            throw new IllegalArgumentException(getEqualsDimensionsExceptionText(this, matrix));
        }

        for (int i = 0; i < this.rows.length; i++) {
            this.rows[i].subtract(matrix.rows[i]);
        }
    }

    public static Matrix getSum(Matrix matrix1, Matrix matrix2) {
        if (!isEqualsDimensions(matrix1, matrix2)) {
            throw new IllegalArgumentException(getEqualsDimensionsExceptionText(matrix1, matrix2));
        }

        Matrix resultMatrix = new Matrix(matrix1);
        resultMatrix.add(matrix2);

        return resultMatrix;
    }

    public static Matrix getDifference(Matrix matrix1, Matrix matrix2) {
        if (!isEqualsDimensions(matrix1, matrix2)) {
            throw new IllegalArgumentException(getEqualsDimensionsExceptionText(matrix1, matrix2));
        }

        Matrix resultMatrix = new Matrix(matrix1);
        resultMatrix.subtract(matrix2);

        return resultMatrix;
    }

    public static Matrix getProduct(Matrix matrix1, Matrix matrix2) {
        if (matrix1.rows[0].getDimension() != matrix2.rows.length) {
            throw new IllegalArgumentException("Количество столбцов " + matrix1.rows[0].getDimension() + " матрицы1" +
                    " не соответствует количеству строк " + matrix2.rows.length + " матрицы2");
        }

        Matrix resultMatrix = new Matrix(matrix1.rows.length, matrix2.rows[0].getDimension());

        Vector[] vectorColumns = new Vector[resultMatrix.rows[0].getDimension()];

        for (int i = 0; i < resultMatrix.rows[0].getDimension(); i++) {
            vectorColumns[i] = matrix2.getColumn(i);
        }

        for (int i = 0; i < resultMatrix.rows.length; i++) {
            for (int j = 0; j < resultMatrix.rows[0].getDimension(); j++) {
                resultMatrix.rows[i].setComponent(j, Vector.getScalarProduct(matrix1.rows[i], vectorColumns[j]));
            }
        }


        return resultMatrix;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('{');

        for (Vector row : rows) {
            sb.append(row);
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append('}');

        return sb.toString();
    }
}
