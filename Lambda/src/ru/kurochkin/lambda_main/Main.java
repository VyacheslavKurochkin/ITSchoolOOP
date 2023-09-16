package ru.kurochkin.lambda_main;

import ru.kurochkin.lambda_person.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Main {
    public static void main(String[] args) {
        ArrayList<Person> persons = new ArrayList<>(10);
        persons.add(new Person(12, "Дмитрий"));
        persons.add(new Person(27, "Олег"));
        persons.add(new Person(43, "Тимур"));
        persons.add(new Person(39, "Владимир"));
        persons.add(new Person(18, "Виталий"));
        persons.add(new Person(15, "Александр"));
        persons.add(new Person(21, "Анна"));
        persons.add(new Person(33, "Олег"));
        persons.add(new Person(29, "Дмитрий"));
        persons.add(new Person(39, "Василий"));

        System.out.println("Список уникальных имен:");
        System.out.println(persons.stream().map(Person::getName).distinct().collect(Collectors.joining(", ")));

        System.out.println();

        System.out.println("Средний возраст людей младше 18: ");
        persons.stream().filter(person -> person.getAge() < 10).mapToDouble(Person::getAge).average().
                ifPresent(System.out::println);

        System.out.println();
        System.out.println("Map, в котором ключи – имена, а значения – средний возраст:");

        Map<String, Double> averageAgesMap = persons.stream().collect(Collectors.groupingBy(Person::getName,
                Collectors.averagingDouble(Person::getAge)));

        for (Map.Entry<String, Double> item : averageAgesMap.entrySet()) {
            System.out.println(item.getKey() + "\t" + item.getValue());
        }

        System.out.println();
        System.out.println("Люди, возвраст котороых от 20 до 45, в порядке убывания возраста:");
        System.out.println(persons.stream().filter(person -> person.getAge() >= 20 && person.getAge() <= 45).
                sorted((person1, person2) -> Integer.compare(person2.getAge(), person1.getAge())).map(Person::getName).
                collect(Collectors.joining(", ")));

        System.out.println();
        System.out.println("Введите количество элементов:");
        Scanner scanner = new Scanner(System.in);
        int countNumbers = scanner.nextInt();

        System.out.println(Arrays.toString(DoubleStream.iterate(0, x -> x + 1).map(Math::sqrt).limit(countNumbers).toArray()));
    }
}