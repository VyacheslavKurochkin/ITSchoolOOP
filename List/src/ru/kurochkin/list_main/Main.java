package ru.kurochkin.list_main;

import ru.kurochkin.list.SinglyLinkedList;

public class Main {
    public static void main(String[] args) {
        SinglyLinkedList<Integer> list1 = new SinglyLinkedList<>(new Integer[]{1, 2, 3, 4, 5});
        System.out.println("Список1: " + list1);

        System.out.println("Добавление элемента:");
        list1.add(3, 34);
        System.out.println("По индексу 3 добавлен элемент со значением 34: " + list1);

        System.out.println("Разворот списка:");
        list1.reverse();
        System.out.println(list1);

        System.out.println("Длинна списка1: " + list1.getSize());

        System.out.println("Копия списка:");
        SinglyLinkedList<Integer> list2 = list1.copy();
        System.out.println("Список2: " + list2);

        System.out.println("Удаление первого элемента:");
        list2.removeHead();
        System.out.println("Список2: " + list2);

        System.out.println("Удаление элемента по индексу:");
        Integer removedData = list2.remove(2);
        System.out.println("Из списка2 удален элемент по индексу 2: " + list2);

        System.out.println("Удаление элемента по значению:");
        boolean isRemoved = list2.remove(Integer.valueOf(34));
        System.out.println("Из списка2 удален элемент по значению 34: " + list2);
    }
}