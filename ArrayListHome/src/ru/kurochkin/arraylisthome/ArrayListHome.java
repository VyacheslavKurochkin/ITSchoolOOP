package ru.kurochkin.arraylisthome;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ArrayListHome {

    public static void loadFileRows(String fileName, ArrayList<String> rows) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new FileInputStream(fileName))) {
            while (scanner.hasNextLine()) {
                rows.add(scanner.nextLine());
            }
        }
    }

    public static void removeEvenNumbers(ArrayList<Integer> numbers) {
        int index = 0;

        while (index < numbers.size()) {
            if (numbers.get(index) % 2 == 0) {
                numbers.remove(index);
            } else {
                ++index;
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
        ArrayList<String> rows = new ArrayList<>();

        try {
            loadFileRows("ArrayListHome\\input.txt", rows);
        } catch (FileNotFoundException e) {
            System.out.println("Файл input.txt не найден");
        }

        System.out.println("Список строк из файла:");
        System.out.println(rows);

        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1, 21, 30, 4, 5, 5, 67, 1, 5, 8));
        removeEvenNumbers(numbers);

        System.out.println("Из списка удалены четные элементы:");
        System.out.println(numbers);

        System.out.println("Копия списка без повторяющихся элементов:");
        ArrayList<Integer> numbersWithoutDuplicates = getListWithoutDuplicates(numbers);
        System.out.println(numbersWithoutDuplicates);
    }
}