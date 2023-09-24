package ru.kurochkin.arraylisthome;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ArrayListHome {
    public static ArrayList<String> getFileLines(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            ArrayList<String> lines = new ArrayList<>();

            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            return lines;
        }
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
        String inputFileName = "ArrayListHome\\input.txt";

        try {
            ArrayList<String> lines = getFileLines(inputFileName);

            System.out.println("Список строк из файла:");
            System.out.println(lines);
        } catch (FileNotFoundException e) {
            System.out.println("Файл \"" + inputFileName + "\" не найден");
        } catch (IOException e) {
            System.out.println("Функция getFileLines. Ошибка: " + e.getMessage());
        }

        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1, 21, 30, 4, 5, 5, 67, 1, 5, 8));
        removeEvenNumbers(numbers);

        System.out.println();
        System.out.println("Из списка удалены четные элементы:");
        System.out.println(numbers);

        ArrayList<Integer> numbersWithoutDuplicates = getListWithoutDuplicates(numbers);

        System.out.println();
        System.out.println("Копия списка без повторяющихся элементов:");
        System.out.println(numbersWithoutDuplicates);
    }
}