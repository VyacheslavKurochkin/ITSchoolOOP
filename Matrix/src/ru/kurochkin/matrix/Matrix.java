package ru.kurochkin.matrix;

import ru.kurochkin.vector.*;

public class Matrix {
    private Vector[] vectors;
    private int columns;

    public Matrix(int rows, int columns) {
        this.columns = columns;

        vectors = new Vector[rows];

        for (int i = 0; i < rows; i++) {
            vectors[i] = new Vector(columns);
        }
    }

    public Matrix(Matrix matrix) {
        this.vectors = new Vector[matrix.vectors.length];
        columns = matrix.columns;

        for (int i = 0; i < matrix.vectors.length; i++) {
            this.vectors[i] = new Vector(matrix.vectors[i]);
        }
    }

    public Matrix(double[][] matrix) {
        columns = matrix[0].length;

        this.vectors = new Vector[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            this.vectors[i] = new Vector(columns, matrix[i]);
        }
    }

    public Matrix(Vector[] vectors) {
        int maxDimension = 0;

        for (int i = 0; i < vectors.length; i++) {
            if (maxDimension < vectors[i].getDimension()) {
                maxDimension = vectors[i].getDimension();
            }
        }

        this.vectors = new Vector[vectors.length];
        columns = maxDimension;

        for (int i = 0; i < this.vectors.length; i++) {
            this.vectors[i] = new Vector(maxDimension, vectors[i]);
        }
    }

    public int getRowsCount() {
        return vectors.length;
    }

    public int getColumnsCount() {
        return columns;
    }

    public void setRow(int rowIndex, Vector vector) {
        if (rowIndex < 0 || rowIndex > vectors.length) {
            throw new IndexOutOfBoundsException("Индекс " + rowIndex + " за пределами диапазона допустимых значений " +
                    "[0.." + (vectors.length - 1) + "]");
        }

        vectors[rowIndex] = new Vector(columns, vector);
    }

    public Vector getRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex > vectors.length) {
            throw new IndexOutOfBoundsException("Индекс " + rowIndex + " за пределами диапазона допустимых значений " +
                    "[0.." + (vectors.length - 1) + "]");
        }

        return vectors[rowIndex];
    }

    public Vector getColumn(int columnIndex) {
        Vector vector = new Vector(vectors.length);

        for (int i = 0; i < vectors.length; i++) {
            vector.setComponent(i, vectors[i].getComponent(columnIndex));
        }

        return vector;
    }

    public void transpose() {
        Vector[] transposedMatrix = new Vector[columns];

        for (int i = 0; i < columns; i++) {
            transposedMatrix[i] = getColumn(i);
        }

        columns = vectors.length;
        vectors = transposedMatrix;
    }

    public void multiplyByScalar(double scalar) {
        for (int i = 0; i < vectors.length; i++) {
            vectors[i].multiplyByScalar(scalar);
        }
    }

    public double getDeterminant() {
        if (vectors.length != columns) {
            throw new IllegalArgumentException("Размерность матрицы " + vectors.length + ", " + columns +
                    " не позволяет вычислить определитель, так как количество строк не равно количеству столбцов");
        }

        if (vectors.length == 1) {
            return vectors[0].getComponent(0);
        }

        Vector[] triangularMatrix = new Vector[vectors.length];

        for (int i = 0; i < vectors.length; i++) {
            triangularMatrix[i] = new Vector(vectors[i]);
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

    public Vector multiplyByVector(Vector inputVector) {
        if (inputVector.getDimension() != columns) {
            throw new IllegalArgumentException("Размерность вектора " + inputVector.getDimension() +
                    " не соответствует размерности матрицы " + vectors.length + "*" + columns);
        }

        Vector resultingVector = new Vector(vectors.length);

        for (int i = 0; i < vectors.length; i++) {
            double sum = 0;

            for (int j = 0; j < columns; j++) {
                sum += vectors[i].getComponent(j) * inputVector.getComponent(j);
            }

            resultingVector.setComponent(i, sum);
        }

        return resultingVector;
    }

    public void add(Matrix matrix) {
        if (matrix.vectors.length != this.vectors.length || matrix.columns != columns) {
            throw new IllegalArgumentException("Размерность матриц " + vectors.length + "*" + columns +
                    " и " + matrix.vectors.length + "*" + matrix.columns + " не совпадает");
        }

        for (int i = 0; i < this.vectors.length; i++) {
            this.vectors[i].add(matrix.vectors[i]);
        }
    }

    public void subtract(Matrix matrix) {
        if (matrix.vectors.length != this.vectors.length || matrix.columns != columns) {
            throw new IllegalArgumentException("Размерность матриц " + vectors.length + "*" + columns +
                    " и " + matrix.vectors.length + "*" + matrix.columns + " не совпадает");
        }

        for (int i = 0; i < this.vectors.length; i++) {
            this.vectors[i].subtract(matrix.vectors[i]);
        }
    }

    public static Matrix getSum(Matrix matrix1, Matrix matrix2) {
        if (matrix1.vectors.length != matrix2.vectors.length || matrix1.columns != matrix2.columns) {
            throw new IllegalArgumentException("Размерность матриц " + matrix1.vectors.length + "*" + matrix1.columns +
                    " и " + matrix2.vectors.length + "*" + matrix2.columns + " не совпадает");
        }

        Matrix resultingMatrix = new Matrix(matrix1);
        resultingMatrix.add(matrix2);

        return resultingMatrix;
    }

    public static Matrix getDifference(Matrix matrix1, Matrix matrix2) {
        if (matrix1.vectors.length != matrix2.vectors.length || matrix1.columns != matrix2.columns) {
            throw new IllegalArgumentException("Размерность матриц " + matrix1.vectors.length + "*" + matrix1.columns +
                    " и " + matrix2.vectors.length + "*" + matrix2.columns + " не совпадает");
        }

        Matrix resultingMatrix = new Matrix(matrix1);
        resultingMatrix.subtract(matrix2);

        return resultingMatrix;
    }

    public static Matrix getProduct(Matrix matrix1, Matrix matrix2) {
        if (matrix1.columns != matrix2.vectors.length) {
            throw new IllegalArgumentException("Количество столбцов " + matrix1.columns + " матрицы1" +
                    " не соответствует количеству строк " + matrix2.vectors.length + " матрицы2");
        }

        Matrix resultingMatrix = new Matrix(matrix1.vectors.length, matrix2.columns);

        Vector[] vectorColumns = new Vector[resultingMatrix.columns];

        for (int i = 0; i < resultingMatrix.columns; i++) {
            vectorColumns[i] = matrix2.getColumn(i);
        }

        for (int i = 0; i < resultingMatrix.vectors.length; i++) {
            for (int j = 0; j < resultingMatrix.columns; j++) {
                resultingMatrix.vectors[i].setComponent(j, Vector.getScalarProduct(matrix1.vectors[i], vectorColumns[j]));
            }
        }

        return resultingMatrix;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");

        for (int i = 0; i < vectors.length; i++) {
            sb.append(vectors[i]);

            if (i + 1 != vectors.length) {
                sb.append(",");
            }
        }

        sb.append("}");

        return sb.toString();
    }
}
