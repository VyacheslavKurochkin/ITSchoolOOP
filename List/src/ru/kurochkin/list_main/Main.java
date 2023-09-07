package ru.kurochkin.list_main;

import ru.kurochkin.list.SinglyLinkedList;

public class Main {
    public static void main(String[] args) {
        SinglyLinkedList<Integer> list1 = new SinglyLinkedList<>(new Integer[]{1, 2, 3, 4, 5});
        list1.addFirst(null);

        System.out.println("Список1: " + list1);

        System.out.println();
        System.out.println("Добавление элемента:");
        list1.add(3, 34);
        System.out.println("По индексу 3 добавлен элемент со значением 34: " + list1);

        System.out.println();
        System.out.println("Разворот списка:");
        list1.reverse();
        System.out.println(list1);

        System.out.println();
        System.out.println("Длинна списка1: " + list1.getLength());

        System.out.println();
        System.out.println("Копия списка:");
        SinglyLinkedList<Integer> list2 = list1.copy();
        System.out.println("Список2: " + list2);

        System.out.println();
        System.out.println("Удаление первого элемента:");
        list2.removeFirst();
        System.out.println("Список2: " + list2);

        System.out.println();
        System.out.println("Удаление элемента по индексу:");
        list2.remove(2);
        System.out.println("Из списка2 удален элемент по индексу 2: " + list2);

        System.out.println();
        System.out.println("Удаление элемента по значению:");
        list2.remove(Integer.valueOf(34));
        System.out.println("Из списка2 удален элемент по значению 34: " + list2);

        list2.remove(null);
        System.out.println("Из списка2 удален элемент по значению null: " + list2);
    }
}