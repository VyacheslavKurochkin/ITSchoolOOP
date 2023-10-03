package ru.kurochkin.lambda;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(new Person(12, "Дмитрий"),
                new Person(27, "Олег"),
                new Person(43, "Тимур"),
                new Person(39, "Владимир"),
                new Person(18, "Виталий"),
                new Person(15, "Александр"),
                new Person(21, "Анна"),
                new Person(33, "Олег"),
                new Person(29, "Дмитрий"),
                new Person(39, "Василий"));

        List<String> distinctNamesList = persons.stream()
                .map(Person::getName)
                .distinct()
                .toList();

        System.out.println("А) Список уникальных имен:");
        System.out.println(distinctNamesList);

        System.out.println();
        System.out.println("Б) Список уникальных имен в формате:");
        System.out.println(distinctNamesList.stream()
                .collect(Collectors.joining(", ", "Имена: ", ".")));

        List<Person> filteredByYounger18AgePersons = persons.stream()
                .filter(person -> person.getAge() < 18)
                .toList();

        System.out.println();
        System.out.println("В) Средний возраст людей младше 18: ");

        if (filteredByYounger18AgePersons.isEmpty()) {
            System.out.println("Людей младше 18 в списке нет");
        } else {
            OptionalDouble younger18PersonsAverageAge = filteredByYounger18AgePersons.stream()
                    .mapToInt(Person::getAge)
                    .average();

            younger18PersonsAverageAge.ifPresent(System.out::println);
        }

        Map<String, Double> averageAgesByNames = persons.stream()
                .collect(Collectors.groupingBy(Person::getName, Collectors.averagingInt(Person::getAge)));

        System.out.println();
        System.out.println("Г) Map, в котором ключи – имена, а значения – средний возраст:");

        for (Map.Entry<String, Double> item : averageAgesByNames.entrySet()) {
            System.out.println(item.getKey() + "\t" + item.getValue());
        }

        List<Person> filteredByAgePersons = persons.stream()
                .filter(person -> person.getAge() >= 20 && person.getAge() <= 45)
                .sorted((person1, person2) -> Integer.compare(person2.getAge(), person1.getAge()))
                .toList();

        System.out.println();
        System.out.println("Д) Люди, возраст которых от 20 до 45, в порядке убывания возраста:");
        System.out.println(filteredByAgePersons.stream().map(Person::getName).toList());

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("Введите количество элементов:");
        int numbersCount = scanner.nextInt();

        DoubleStream numbersSquareRoots = DoubleStream
                .iterate(0, x -> x + 1)
                .map(Math::sqrt)
                .limit(numbersCount);

        System.out.println("Бесконечный поток корней чисел. Количество элементов " + numbersCount + ":");
        numbersSquareRoots.forEach(System.out::println);
    }
}