package ru.kurochkin.hash_table_main;

import ru.kurochkin.hash_table.HashTable;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        HashTable<Integer> numbers = new HashTable<>(10);

        for (int i = 0; i < 11; i++) {
            numbers.add(i);
        }

        System.out.println("Коллекция чисел:");
        System.out.println(numbers);

        System.out.println();
        System.out.println("Метод toArray:");

        Integer[] numbersArray = {1, 0, 2};
        System.out.println("Запуск с параметром массив целых чисел: " + Arrays.toString(numbersArray));

        Integer[] resultNumbersArray = numbers.toArray(numbersArray);
        System.out.println("Результат: " + Arrays.toString(resultNumbersArray));

        System.out.println();
        System.out.println("Метод containsAll");
        System.out.println("Запуск с параметром массив целых чисел: " + Arrays.toString(numbersArray));
        System.out.println("Результат: " + numbers.containsAll(Arrays.asList(numbersArray)));

        Integer numberToRemove = 10;

        System.out.println();
        System.out.println("Метод remove");
        System.out.println("Запуск с параметром число: " + numberToRemove);
        System.out.println("Результат: " + numbers.remove(numberToRemove));

        System.out.println();
        System.out.println("Итератор");
        System.out.println("Перебор всех элементов коллекции для вывода на экран");

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (Integer number : numbers) {
            sb.append(number).append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append(']');

        System.out.println(sb);
    }
}