package ru.kurochkin.range_main;

import ru.kurochkin.range.Range;

public class Main {
    public static void main(String[] args) {
        Range range1 = new Range(2.2, 2.5);
        Range range2 = new Range(2.3, 2.8);

        System.out.println("Диапазоны:");
        System.out.println(range1);
        System.out.println(range2);

        Range[] union = range1.getUnion(range2);

        System.out.println("Объединение:");

        System.out.printf("%s", "[");

        for (int i = 0; i < union.length; i++) {
            if (i > 0) {
                System.out.printf("%s", ", ");
            }

            System.out.printf("%s", union[i]);
        }

        System.out.println("]");

        Range[] difference = range1.getDifference(range2);

        System.out.println("Разность:");

        System.out.printf("%s", "[");

        for (int i = 0; i < difference.length; i++) {
            if (i > 0) {
                System.out.printf("%s", ",");
            }

            System.out.printf("%s", difference[i]);
        }

        System.out.println("]");

        Range intersection = range1.getIntersection(range2);

        System.out.println("Пересечение: ");

        if (intersection != null) {
            System.out.printf("[%s]", intersection);
        } else {
            System.out.println("[]");
        }
    }
}