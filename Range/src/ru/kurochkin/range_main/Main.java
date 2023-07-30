package ru.kurochkin.range_main;

import ru.kurochkin.range.Range;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Range range1 = new Range(1, 6);
        Range range2 = new Range(3, 6);

        System.out.println("Диапазоны:");
        System.out.println(range1);
        System.out.println(range2);

        Range[] union = range1.getUnion(range2);

        System.out.println("Объединение:");
        System.out.print(Arrays.toString(union));
        System.out.println();

        Range[] difference = range1.getDifference(range2);

        System.out.println("Разность:");
        System.out.print(Arrays.toString(difference));
        System.out.println();

        Range intersection = range1.getIntersection(range2);

        System.out.println("Пересечение: ");
        System.out.print(intersection);
    }
}