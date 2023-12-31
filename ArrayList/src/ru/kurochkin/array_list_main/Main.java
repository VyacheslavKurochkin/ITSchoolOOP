package ru.kurochkin.array_list_main;

import ru.kurochkin.array_list.*;

import java.util.Arrays;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> lines1 = new ArrayList<>(Arrays.asList("st1", "st2", "st3", null, "22", "st2"));
        ArrayList<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            numbers.add(i);
        }

        numbers.add(1, 2);

        System.out.println("Список строк:");
        System.out.println(lines1);

        System.out.println("Список чисел:");
        System.out.println(numbers);

        System.out.println();
        System.out.println("Метод removeAll:");

        lines1.removeAll(Arrays.asList("11", null, "78", "st2"));
        System.out.println("Список строк: " + lines1);

        System.out.println();
        System.out.println("Список чисел. Метод toArray:");

        Integer[] numbersArray = {12, 13, 14, 15, 77, 88, 99, 11};
        System.out.println("Запуск с параметром массив целых чисел: " + Arrays.toString(numbersArray));

        Integer[] resultNumbersArray = numbers.toArray(numbersArray);
        System.out.println("Результат: " + Arrays.toString(resultNumbersArray));

        System.out.println();
        System.out.println("Список строк. Метод retainAll:");
        System.out.println("Запуск с параметром пустой список: []");

        LinkedList<String> emptyList = new LinkedList<>();
        lines1.retainAll(emptyList);
        System.out.println("Результат: " + lines1);

        System.out.println();
        System.out.println("Список строк. Метод addAll:");

        String[] lines2 = {"line1", "line3 ", "line5", null, "22 "};
        System.out.println("Запуск с параметром список строк: " + Arrays.toString(lines2));

        lines1.addAll(0, Arrays.asList(lines2));
        System.out.println("Результат: " + lines1);

        System.out.println();
        System.out.println("Список строк. Итератор");
        System.out.println("Перебор всех элементов списка для вывода на экран");

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (String item : lines1) {
            sb.append(item).append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append(']');

        System.out.println(sb);

        System.out.println();
        System.out.println("Список строк. Метод lastIndexOf");
        System.out.println("Запуск с параметром: null");
        System.out.println("Результат: " + lines1.lastIndexOf(null));

        lines1.remove(null);

        System.out.println();
        System.out.println("Список строк. Метод replaceAll");
        System.out.println("Запуск с параметром: String::trim");

        lines1.replaceAll(String::trim);
        System.out.println("Результат " + lines1);
    }
}
