package ru.kurochkin.vector_main;

import ru.kurochkin.vector.Vector;

public class Main {
    public static void main(String[] args) {
        Vector vector1 = new Vector(5, new double[]{1, 8, 3, 0});
        Vector vector2 = new Vector(new double[]{3, 4, 5, 1, 2, 3});
        Vector vector3 = new Vector(vector1);

        System.out.println("вектор1 = " + vector1);
        System.out.println("вектор2 = " + vector2);
        System.out.println("вектор3 = " + vector3);

        vector1.add(vector2);
        vector3.subtract(vector1);
        vector2.multiplyByScalar(0.5);

        System.out.println();
        System.out.println("Прибавление к вектору другого вектора:");
        System.out.println("вектор1 + вектор2: " + vector1);

        System.out.println();
        System.out.println("Вычитание из вектора другого вектора:");
        System.out.println("вектор3 - вектор1: " + vector3);

        System.out.println();
        System.out.println("Умножение вектора на скаляр");
        System.out.println("вектор2 * 0.5: " + vector2);

        vector3.reverse();

        System.out.println();
        System.out.println("Разворот вектора:");
        System.out.println("вектор3 разворот: " + vector3);

        System.out.println();
        System.out.println("Получение длины вектора:");
        System.out.println("вектор2 длинна: " + vector2.getLength());

        Vector vector4 = Vector.getSum(vector2, vector3);

        System.out.println();
        System.out.println("Сложение двух векторов:");
        System.out.println("вектор4 = вектор2 + вектор3: " + vector4);

        System.out.println();
        System.out.println("Скалярное произведение векторов:");
        System.out.println("вектор4 и вектор1: " + Vector.getScalarProduct(vector4, vector1));
    }
}