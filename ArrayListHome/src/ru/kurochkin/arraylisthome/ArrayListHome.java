package ru.kurochkin.arraylisthome;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ArrayListHome {
    public static ArrayList<String> getFileLines(String fileName) throws IOException {
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }

    public static void removeEvenNumbers(ArrayList<Integer> numbers) {
        int i = 0;

        while (i < numbers.size()) {
            if (numbers.get(i) % 2 == 0) {
                numbers.remove(i);
            } else {
                ++i;
            }
        }
    }

    public static ArrayList<Integer> getListWithoutDuplicates(ArrayList<Integer> numbers) {
        ArrayList<Integer> numbersWithoutDuplicates = new ArrayList<>(numbers.size());

        for (Integer number : numbers) {
            if (!numbersWithoutDuplicates.contains(number)) {
                numbersWithoutDuplicates.add(number);
            }
        }

        return numbersWithoutDuplicates;
    }

    public static void main(String[] args) {
        ArrayList<String> lines = null;

        try {
            lines = getFileLines("ArrayListHome\\input.txt");
        } catch (FileNotFoundException exception) {
            System.out.println("Файл input.txt не найден");
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        System.out.println("Список строк из файла:");
        System.out.println(lines);

        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1, 21, 30, 4, 5, 5, 67, 1, 5, 8));
        removeEvenNumbers(numbers);

        System.out.println();
        System.out.println("Из списка удалены четные элементы:");
        System.out.println(numbers);

        System.out.println();
        System.out.println("Копия списка без повторяющихся элементов:");
        ArrayList<Integer> numbersWithoutDuplicates = getListWithoutDuplicates(numbers);
        System.out.println(numbersWithoutDuplicates);
    }
}