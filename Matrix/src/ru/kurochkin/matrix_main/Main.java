package ru.kurochkin.matrix_main;

import ru.kurochkin.matrix.Matrix;
import ru.kurochkin.vector.Vector;

public class Main {
    public static void main(String[] args) {
        Matrix matrix221 = new Matrix(new double[][]{{1}});

        Matrix matrix1 = new Matrix(new double[][]{{-4, -2, -7, 8}, {2, 0, 4, 9}, {2, 0, 6, -3}, {6, 4, -10, -4}});
        Matrix matrix2 = new Matrix(new double[][]{{-4, -2, 1}, {2, 7, 1}, {3, 6, 1}, {8, 3, 2}});
        Vector vector1 = new Vector(new double[]{1.0, 2.0, 10.0, -1});

        System.out.println("Матрица1:");
        System.out.println(matrix1);

        System.out.println("Матрица2:");
        System.out.println(matrix2);

        System.out.println("Вектор1:");
        System.out.println(vector1);

        Matrix matrix3 = Matrix.getProduct(matrix1, matrix2);
        System.out.println("Умножение матриц");
        System.out.println("матрица3 = матрица1 * матрица2: " + matrix3);

        System.out.println("Вычитание матриц");
        System.out.println("матрица3 - матрица2: " + Matrix.getDifference(matrix3, matrix2));

        System.out.println("Транспонирование");
        matrix2.transpose();
        System.out.println("матрица2: " + matrix2);

        System.out.println("Детерминант матрица1: " + matrix1.getDeterminant());

        matrix2.multiplyByScalar(0.5);
        System.out.println("Умножение на скаляр");
        System.out.println("матрица2 * 0.5 : " + matrix2);

        System.out.println("Умножение на вектор");
        System.out.println("матрица2 * вектор1: " + matrix2.multiplyByVector(vector1));
    }
}